package ua.kuk.books.entrypoint

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ua.kuk.book_playback.screens.PlaybackScreen
import ua.kuk.data.utils.MockDataProvider


@Composable
fun AppScreen(mockDataProvider: MockDataProvider) {
    Scaffold { paddingValues ->
        val topPadding = paddingValues.calculateTopPadding()
        val bottomPadding = paddingValues.calculateBottomPadding()
        PlaybackScreen(
            modifier = Modifier
                .padding(
                    top = topPadding,
                    bottom = bottomPadding
                )
                .fillMaxSize(),
            book = mockDataProvider.getBookAudioInfo(),
            selectedKeyPointPosition = 0
        )
    }
}





