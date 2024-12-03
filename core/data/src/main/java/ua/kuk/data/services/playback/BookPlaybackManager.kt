package ua.kuk.data.services.playback

import android.content.ComponentName
import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import ua.kuk.common.logger.Logger
import ua.kuk.data.model.BookAudioInfo
import ua.kuk.data.model.PlaybackData
import ua.kuk.data.services.playback.PlaybackState.Error
import ua.kuk.data.services.playback.PlaybackState.Initial
import ua.kuk.data.services.playback.PlaybackState.Pause
import ua.kuk.data.services.playback.PlaybackState.Play

/**
 * Manages the playback of summary key points, providing control over session initialization, play/pause functionality,
 * speed adjustment, and navigation through tracks.
 *
 * This class interacts with the MediaController to manage playback and provides a flow of playback data,
 * including current playback state, position, speed, and errors.
 */

class BookPlaybackManager(
    private val context: Context,
    private val logger: Logger
) {
    private var mediaController: MediaController? = null

    private val playingData = MutableStateFlow(PlaybackData())

    private val _playingState = MutableStateFlow<PlaybackState>(Initial)
    val playingState = _playingState.asStateFlow()

    fun initSession(item: BookAudioInfo, position: Int): Flow<PlaybackData> {
        _playingState.value = Initial
        val sessionToken = SessionToken(
            context, ComponentName(context, BookPlaybackService::class.java)
        )

        val controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
        controllerFuture.addListener(
            {
                mediaController = controllerFuture.get()
                startPlayback(item, position)
                observePlaying()
            }, MoreExecutors.directExecutor()
        )
        return getPlayingData()
    }

    fun removeMediaController() {
        mediaController?.pause()
        mediaController?.release()
        mediaController = null
    }

    private fun getPlayingData(): Flow<PlaybackData> {
        return playingData.combine(getPositionFlow()) { playStatus, position ->
            playStatus.copy(position = position)
        }
    }

    @OptIn(UnstableApi::class)
    private fun startPlayback(item: BookAudioInfo, position: Int) {
        val mediaController = mediaController ?: return
        if (!mediaController.isConnected) {
            logger.e(TAG, "mediaController is not connected")
            return
        }

        mediaController.clearMediaItems()

        val mediaItems = bookAudioInfoToMediaItems(item)
        mediaController.setMediaItems(mediaItems, position, C.TIME_UNSET)

        mediaController.prepare()
        mediaController.playWhenReady = true
        mediaController.play()
    }

    private fun observePlaying() {
        val listener: Player.Listener =
            object : Player.Listener {
                override fun onPlayerError(error: PlaybackException) {
                    logger.e(TAG, "onPlayerError ${error.message}", error)
                    _playingState.value = Error
                }

                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    _playingState.value = if (isPlaying) Play else Pause
                }

                override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {
                    val speed = playbackParameters.speed
                    playingData.value = playingData.value.copy(speed = speed)
                }

                @OptIn(UnstableApi::class)
                override fun onEvents(player: Player, events: Player.Events) {
                    val mediaItem = player.currentMediaItem
                    val mediaItemId = mediaItem?.mediaId?.toInt()
                    val duration = player.duration

                    mediaItemId?.let {
                        playingData.value =
                            playingData.value.copy(currentKeyPointId = it, duration = duration)
                    }
                }
            }

        mediaController?.addListener(listener)
    }

    private fun getPositionFlow(): Flow<Long> {
        return flow {
            while (true) {
                delay(1000)
                val position = mediaController?.currentPosition
                position?.let { emit(it) }
            }
        }
    }

    fun play() {
        mediaController?.play()
    }

    fun pause() {
        mediaController?.pause()
    }

    fun changeSpeed(speed: Float) {
        mediaController?.setPlaybackSpeed(speed)
    }

    fun previousTrack() {
        mediaController?.seekToPrevious()
        if (_playingState.value is Error) play()
    }

    fun nextTrack() {
        mediaController?.seekToNext()
        if (_playingState.value is Error) play()
    }

    fun rewindTrack(position: Long) {
        mediaController?.seekTo(position)
    }

    private fun bookAudioInfoToMediaItems(book: BookAudioInfo): List<MediaItem> {
        return book.keyPoints.map { keyPoint ->
            MediaItem.Builder()
                .setMediaId(keyPoint.id.toString())
                .setUri(keyPoint.audio)
                .setMediaMetadata(
                    androidx.media3.common.MediaMetadata.Builder()
                        .setTitle(keyPoint.title)
                        .setArtist(book.bookTitle)
                        .setIsBrowsable(false)
                        .setIsPlayable(true)
                        .setArtworkUri(book.logo)
                        .setMediaType(androidx.media3.common.MediaMetadata.MEDIA_TYPE_AUDIO_BOOK_CHAPTER)
                        .build()
                )
                .build()
        }
    }

    private companion object {
        private const val TAG = "BookPlaybackManagerTag"
    }
}