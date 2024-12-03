package ua.kuk.book_playback

import android.net.Uri
import androidx.compose.ui.tooling.preview.PreviewParameterProvider


internal class PlaybackScreenStatePreviewProvider : PreviewParameterProvider<ScreenState> {
    override val values: Sequence<ScreenState>
        get() = sequenceOf(
            ScreenState(
                bookLogo = Uri.parse("android.resource://ua.kuk.books/raw/logo_173308550"),
                keyPointNumber = 1,
                totalKeyPoint = 5,
                keyPointTitle = "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters",
                playbackPosition = 3000,
                duration = 60_000,
                speed = 1f,
                isPlaying = true
            )
        )
}
