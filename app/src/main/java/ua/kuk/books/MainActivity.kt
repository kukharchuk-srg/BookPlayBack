package ua.kuk.books

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import org.koin.android.ext.android.inject
import ua.kuk.books.entrypoint.AppScreen
import ua.kuk.data.utils.MockDataProvider
import ua.kuk.ui_kit.ui.theme.BookPlayBackTheme

class MainActivity : ComponentActivity() {
    private val mockDataProvider by inject<MockDataProvider>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BookPlayBackTheme {
                AppScreen(mockDataProvider)
            }
        }
    }
}
