package ui

import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import prancingpony.composeapp.generated.resources.Res
import prancingpony.composeapp.generated.resources.gothica1_regular
import org.jetbrains.compose.resources.Font
import prancingpony.composeapp.generated.resources.gothica1_black
import prancingpony.composeapp.generated.resources.gothica1_bold
import prancingpony.composeapp.generated.resources.gothica1_medium
import prancingpony.composeapp.generated.resources.gothica1_semibold

@Composable
fun gothicA1() = FontFamily(
    listOf(
        Font( resource = Res.font.gothica1_regular),
        Font(resource = Res.font.gothica1_medium, FontWeight.Medium),
        Font(resource = Res.font.gothica1_semibold, FontWeight.SemiBold),
        Font(resource = Res.font.gothica1_bold, FontWeight.Bold),
        Font(resource = Res.font.gothica1_black, FontWeight.Black),
    )
)

// Set of Material typography styles to start with
@Composable
fun Typography() = Typography(
    body1 = TextStyle(
        color = AquaBlue,
        fontFamily = gothicA1(),
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    h1 = TextStyle(
        color = TextWhite,
        fontFamily = gothicA1(),
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp
    ),
    h2 = TextStyle(
        color = TextWhite,
        fontFamily = gothicA1(),
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    )
)