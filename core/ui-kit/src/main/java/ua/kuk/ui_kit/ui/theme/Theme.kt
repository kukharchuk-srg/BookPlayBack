package ua.kuk.ui_kit.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Blue100,
    onPrimary = Color.White,
    background = Jet100,
    onBackground = Color.White,
    surface = Grey100,
    surfaceContainer = White20,
    onSurface = White40
)

private val LightColorScheme = lightColorScheme(
    primary = Blue100,
    onPrimary = Color.White,
    background = Beige100,
    onBackground = Color.Black,
    surface = Color.White,
    surfaceContainer = Black10,
    onSurface = Black40,
)

@Composable
fun BookPlayBackTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}