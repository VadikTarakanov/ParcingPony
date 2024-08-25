package twine.presentation.model

import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.DrawableResource

data class Feature(
    val title: String,
    val iconResource: DrawableResource,
    val lightColor: Color,
    val mediumColor: Color,
    val darkColor: Color
)