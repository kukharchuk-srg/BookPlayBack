package ua.kuk.book_playback

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import ua.kuk.book_playback.PlaybackIntent.ChangeSpeed
import ua.kuk.book_playback.PlaybackIntent.Init
import ua.kuk.book_playback.PlaybackIntent.Next
import ua.kuk.book_playback.PlaybackIntent.Pause
import ua.kuk.book_playback.PlaybackIntent.Play
import ua.kuk.book_playback.PlaybackIntent.Previous
import ua.kuk.book_playback.PlaybackIntent.SeekBackward
import ua.kuk.book_playback.PlaybackIntent.SeekForward
import ua.kuk.book_playback.PlaybackIntent.SeekTrack
import ua.kuk.common.AppDispatchers
import ua.kuk.common.logger.Logger
import ua.kuk.data.model.BookAudioInfo
import ua.kuk.data.model.PlaybackData
import ua.kuk.data.repository.BookPlayBackRepository
import ua.kuk.data.services.playback.PlaybackState

class PlaybackViewModel(
    private val book: BookAudioInfo,
    private val playBackRepository: BookPlayBackRepository,
    private val dispatchers: AppDispatchers,
    private val logger: Logger,
) : ViewModel() {
    private val userIntent = Channel<PlaybackIntent>(Channel.UNLIMITED)

    private val _events = Channel<PlaybackEvent>()
    val events = _events.receiveAsFlow()

    private val _state: MutableStateFlow<ScreenState> = MutableStateFlow(getInitialScreenState())
    val state = _state.asStateFlow()

    fun init(keyPointPosition: Int) {
        handleIntent()
        sendIntent(Init(keyPointPosition))
    }

    fun sendIntent(intent: PlaybackIntent) {
        viewModelScope.launch(dispatchers.io) {
            userIntent.send(intent)
        }
    }

    fun cancelPlaying() {
        playBackRepository.cancelPlaying()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect {
                when (val intent = it) {
                    is Init -> initPlayingSession(intent.keyPointPosition)
                    Next -> playBackRepository.nextTrack()
                    Pause -> playBackRepository.pause()
                    Play -> playBackRepository.play()
                    Previous -> playBackRepository.previousTrack()
                    SeekBackward -> seekBackward()
                    SeekForward -> seekForward()
                    is SeekTrack -> playBackRepository.seekTrack(intent.position)
                    is ChangeSpeed -> playBackRepository.changeSpeed(intent.speed)
                }
            }
        }
    }

    private fun initPlayingSession(position: Int) {
        val playingData = playBackRepository.initPlaying(book, position)
        observePlaybackData(playingData)
        observePlaybackState()
    }

    private fun observePlaybackState() {
        viewModelScope.launch {
            playBackRepository.getPlayingState().collectLatest { playBackState ->
                when (playBackState) {
                    PlaybackState.Error -> _events.send(Error)
                    PlaybackState.Initial -> Unit
                    PlaybackState.Pause -> setIsPlayingState(false)
                    PlaybackState.Play -> setIsPlayingState(true)
                }
            }
        }
    }

    private fun observePlaybackData(playingData: Flow<PlaybackData>) {
        viewModelScope.launch {
            playingData.collectLatest { data ->
                val keyPoint = book.keyPoints.find { keyPoint ->
                    keyPoint.id == data.currentKeyPointId
                }
                if (keyPoint != null) {
                    val keyPointNumber = book.keyPoints.indexOf(keyPoint) + 1
                    val newState = _state.value.copy(
                        keyPointNumber = keyPointNumber,
                        keyPointTitle = keyPoint.title,
                        playbackPosition = data.position,
                        duration = data.duration,
                        speed = data.speed
                    )
                    _state.value = newState
                } else logger.e(TAG, "Key point is null")
            }
        }
    }

    private fun setIsPlayingState(isPlaying: Boolean) {
        _state.value = _state.value.copy(isPlaying = isPlaying)
    }

    private fun seekBackward() {
        val currentPosition = _state.value.playbackPosition
        val desirePosition = currentPosition + INTERVAL_SEEK_BACKWARD
        val newPosition = if (desirePosition >= 0) desirePosition else 0
        playBackRepository.seekTrack(newPosition)
    }

    private fun seekForward() {
        val duration = _state.value.duration
        val currentPosition = _state.value.playbackPosition
        val desirePosition = currentPosition + INTERVAL_SEEK_FORWARD
        val newPosition = if (desirePosition <= duration) desirePosition else duration
        playBackRepository.seekTrack(newPosition)
    }

    private fun getInitialScreenState(): ScreenState {
        return ScreenState(
            bookLogo = book.logo,
            keyPointNumber = 1,
            totalKeyPoint = book.keyPoints.size,
            keyPointTitle = book.keyPoints.first().title,
            playbackPosition = 0,
            duration = 0,
            speed = 1f,
            isPlaying = false
        )
    }

    private companion object {
        private const val TAG = "PlaybackViewModelTag"

        private const val INTERVAL_SEEK_BACKWARD = -5000L
        private const val INTERVAL_SEEK_FORWARD = 10_000L
    }
}


