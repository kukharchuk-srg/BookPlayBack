package ua.kuk.data.services.playback

sealed interface PlaybackState {
data object Initial:PlaybackState
data object Play:PlaybackState
data object Pause:PlaybackState
data object Error:PlaybackState
}