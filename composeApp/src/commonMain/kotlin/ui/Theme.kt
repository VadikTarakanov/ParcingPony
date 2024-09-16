package ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun MeditationUIYouTubeTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    MaterialTheme(
        typography = Typography(),
        shapes = Shapes,
        content = content,
//        colors = Colors(
//            primary = PrimaryColor,
//            primaryVariant = Color.White,
//            secondary = SecondaryColor,
//            secondaryVariant = SecondaryColor,
//            background = TextWhite,
//            surface = BackgroundColor,
//            error = LightRed,
//            onPrimary = PrimaryColor,
//            onBackground = BackgroundColor,
//            onSecondary = SecondaryColor,
//            onSurface = BackgroundColor,
//            onError = LightRed,
//            isLight = true
//        )
    )
}