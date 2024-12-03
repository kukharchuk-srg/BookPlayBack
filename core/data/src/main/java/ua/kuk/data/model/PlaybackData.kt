package ua.kuk.data.model


data class PlaybackData(
    val position: Long = 0,
    val duration: Long = 0,
    val currentKeyPointId: Int = 0,
    val speed: Float = 1f
)


