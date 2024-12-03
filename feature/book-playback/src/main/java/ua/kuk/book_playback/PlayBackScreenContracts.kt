package ua.kuk.book_playback

import android.net.Uri
import ua.kuk.book_playback.PlaybackIntent.ChangeSpeed
import ua.kuk.book_playback.PlaybackIntent.Init
import ua.kuk.book_playback.PlaybackIntent.Next
import ua.kuk.book_playback.PlaybackIntent.Pause
import ua.kuk.book_playback.PlaybackIntent.Play
import ua.kuk.book_playback.PlaybackIntent.Previous
import ua.kuk.book_playback.PlaybackIntent.SeekBackward
import ua.kuk.book_playback.PlaybackIntent.SeekForward
import ua.kuk.book_playback.PlaybackIntent.SeekTrack

/**
 * Represents the state of the playback screen, including information about the book's logo,
 * the current key point, playback position, total duration, and playback speed.
 *
 * @param bookLogo The URI of the book's logo.
 * @param keyPointNumber The current key point number being played.
 * @param totalKeyPoint The total number of key points in the book.
 * @param keyPointTitle The title of the current key point.
 * @param playbackPosition The current playback position in milliseconds.
 * @param duration The total duration of the audio book in milliseconds.
 * @param speed The current playback speed.
 * @param isPlaying Boolean indicating whether the playback is currently active.
 */
data class ScreenState(
    val bookLogo: Uri,
    val keyPointNumber: Int,
    val totalKeyPoint: Int,
    val keyPointTitle: String,
    val playbackPosition: Long,
    val duration: Long,
    val speed: Float,
    val isPlaying: Boolean
)

sealed interface PlaybackEvent
data object Error : PlaybackEvent

/**
 * Sealed interface representing various intents for controlling playback in the application.
 * Each intent corresponds to a specific action that the user can perform or a system event
 * that needs to be handled, such as play, pause, seek, and change playback speed.
 *
 * The following intents are included:
 * - [Init] - Initializes playback from a specified key point position.
 * - [Play] - Starts playback.
 * - [Pause] - Pauses the current playback.
 * - [Next] - Skips to the next key point in the playback.
 * - [Previous] - Skips to the previous key point in the playback.
 * - [SeekBackward] - Seeks playback backward by a fixed amount.
 * - [SeekForward] - Seeks playback forward by a fixed amount.
 * - [SeekTrack] - Seeks playback to a specific position (in milliseconds).
 * - [ChangeSpeed] - Changes the playback speed.
 */
sealed interface PlaybackIntent{
    data class Init(val keyPointPosition: Int) : PlaybackIntent
    data object Play : PlaybackIntent
    data object Pause : PlaybackIntent
    data object Next : PlaybackIntent
    data object Previous : PlaybackIntent
    data object SeekBackward : PlaybackIntent
    data object SeekForward : PlaybackIntent
    data class SeekTrack(val position: Long) : PlaybackIntent
    data class ChangeSpeed(val speed: Float) : PlaybackIntent
}






