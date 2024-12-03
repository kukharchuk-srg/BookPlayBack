package ua.kuk.data.repository

import kotlinx.coroutines.flow.Flow
import ua.kuk.data.model.BookAudioInfo
import ua.kuk.data.model.PlaybackData
import ua.kuk.data.services.playback.PlaybackState
/**
 * Interface for managing audio playback functionality related to a summary.
 * This repository provides methods for controlling playback, such as starting, pausing,
 * changing playback speed, and navigating between tracks.
 */
interface BookPlayBackRepository {
    /**
     * Initializes audio playback for a specific audiobook item at the given position.
     *
     * @param item The audiobook item containing the necessary metadata to start playback.
     * @param position The starting position of selected key point.
     * @return A [Flow] emitting [PlaybackData] that contains the current playback information.
     */
    fun initPlaying(item: BookAudioInfo, position: Int): Flow<PlaybackData>
    fun play()
    fun pause()

    /**
     * Returns the current playback state as a [Flow] emitting a [PlaybackState].
     * The [PlaybackState] contains information about the current state of the playback,
     * such as playing, paused, or stopped.
     *
     * @return A [Flow] emitting the current playback state.
     */
    fun getPlayingState(): Flow<PlaybackState>
    fun changeSpeed(speed: Float)
    fun previousTrack()
    fun nextTrack()
    fun seekTrack(position: Long)
    fun cancelPlaying()
}