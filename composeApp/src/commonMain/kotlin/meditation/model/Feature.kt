package meditation.model

import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.DrawableResource
import prancingpony.composeapp.generated.resources.Res

data class Feature(
    val title: String,
    val iconResource: DrawableResource,
    val lightColor: Color,
    val mediumColor: Color,
    val darkColor: Color
)