package ua.kuk.book_playback

import kotlin.math.round
/**
 * Extension function for the Float type that formats the sum of the current float and the given value,
 * rounding the result to one decimal place.
 *
 * @param value The float value to add to the current float.
 * @return A formatted float, rounded to one decimal place.
 */
fun Float.formatSpeed(value: Float): Float {
    return round((this + value) * 10) / 10f
}

/**
 * Formats the given float value into a string representation.
 * If the float value is a whole number, it returns it as an integer string.
 * Otherwise, it returns the value as a string with decimal places.
 *
 * @param value The float value to format.
 * @return The string representation of the formatted float.
 */
fun formatSpeed(value: Float): String {
    return if (value % 1 == 0f) {
        value.toInt().toString()
    } else {
        value.toString()
    }
}

/**
 * Formats a given long value representing time in milliseconds into a string representation of minutes and seconds.
 * The time is displayed in the format "mm:ss".
 *
 * @return The formatted time string in the "mm:ss" format.
 */
fun Long.formatToTimeContent(): String {
    val totalSeconds = this / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds - (minutes * 60)
    return "%02d:%02d".format(minutes, seconds)
}