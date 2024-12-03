package ua.kuk.data.repository

import kotlinx.coroutines.flow.Flow
import ua.kuk.common.logger.Logger
import ua.kuk.data.model.BookAudioInfo
import ua.kuk.data.model.PlaybackData
import ua.kuk.data.services.playback.BookPlaybackManager
import ua.kuk.data.services.playback.PlaybackState

class BookPlayBackRepositoryImpl(
    private val playbackManager: BookPlaybackManager,
    private val logger: Logger
) : BookPlayBackRepository {

    override fun initPlaying(item: BookAudioInfo, position: Int): Flow<PlaybackData> {
        logger.d(TAG, "Book info for playing $item")

        if (item.keyPoints.isEmpty()) {
            throw IllegalArgumentException("Empty key points list in book with id ${item.id}")
        }

        val selectedKeyPointPosition =
            if (item.keyPoints.any { it.position == position }) position else {
                logger.e(TAG, "Illegal selected key point position")
                0
            }
        return playbackManager.initSession(item, selectedKeyPointPosition)
    }

    override fun play() {
        playbackManager.play()
    }

    override fun pause() {
        playbackManager.pause()
    }

    override fun getPlayingState(): Flow<PlaybackState> {
        return playbackManager.playingState
    }

    override fun changeSpeed(speed: Float) {
        playbackManager.changeSpeed(speed)
    }

    override fun previousTrack() {
        playbackManager.previousTrack()
    }

    override fun nextTrack() {
        playbackManager.nextTrack()
    }

    override fun seekTrack(position: Long) {
        playbackManager.rewindTrack(position)
    }

    override fun cancelPlaying() {
        playbackManager.removeMediaController()
    }


    private companion object {
        private const val TAG = "BookPlayBackRepositoryImplTag"
    }
}