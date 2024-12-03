package ua.kuk.book_playback.screens

import androidx.annotation.StringRes
import ua.kuk.book_playback.R
/**
 * Enum class representing the available playback speeds.
 * Each constant includes a float value indicating the speed and
 * a string resource ID to display the speed label in the UI.
 *
 * @param speed The playback speed as a float. The value represents the multiplier for normal speed.
 * @param titleResId The string resource ID that represents the title of the speed setting.
 */

enum class SpeedConst(val speed: Float, @StringRes val titleResId: Int) {
    SPEED_0_8(0.8f, R.string.title_speed_0_8),
    SPEED_1(1f, R.string.title_speed_1),
    SPEED_1_2(1.2f, R.string.title_speed_1_2),
    SPEED_1_5(1.5f, R.string.title_speed_1_5),
}