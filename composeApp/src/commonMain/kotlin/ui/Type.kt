package ui

import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import prancingpony.composeapp.generated.resources.Onest_Bold
import prancingpony.composeapp.generated.resources.Onest_ExtraBold
import prancingpony.composeapp.generated.resources.Onest_Light
import prancingpony.composeapp.generated.resources.Onest_Medium
import prancingpony.composeapp.generated.resources.Onest_Regular
import prancingpony.composeapp.generated.resources.Onest_Thin
import prancingpony.composeapp.generated.resources.Res

@Composable
fun robotoA1() = FontFamily(
    listOf(
        Font(resource = Res.font.Onest_Thin, weight = FontWeight.Thin),
        Font(resource = Res.font.Onest_Light, weight = FontWeight.Light),
        Font(resource = Res.font.Onest_Regular, weight = FontWeight.Normal),
        Font(resource = Res.font.Onest_Medium, weight = FontWeight.Medium),
        Font(resource = Res.font.Onest_Bold, weight = FontWeight.Bold),
        Font(resource = Res.font.Onest_ExtraBold, weight = FontWeight.Bold)
    )
)

// Set of Material typography styles to start with
@Composable
fun Typography() = Typography(
    h1 = TextStyle(
        color = TextPrimary,
        fontFamily = robotoA1(),
        fontWeight = FontWeight.ExtraBold,
        fontSize = 32.sp
    ),
    h2 = TextStyle(
        color = TextPrimary,
        fontFamily = robotoA1(),
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp
    ),
    h3 = TextStyle(
        color = TextPrimary,
        fontFamily = robotoA1(),
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    ),
    h4 = TextStyle(
        color = TextPrimary,
        fontFamily = robotoA1(),
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp
    ),
    h5 = TextStyle(
        color = TextPrimary,
        fontFamily = robotoA1(),
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    h6 = TextStyle(
        color = TextPrimary,
        fontFamily = robotoA1(),
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    subtitle1 = TextStyle(
        color = TextSecondary,
        fontFamily = robotoA1(),
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    subtitle2 = TextStyle(
        color = TextSecondary,
        fontFamily = robotoA1(),
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    body1 = TextStyle(
        color = TextPrimary,
        fontFamily = robotoA1(),
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    body2 = TextStyle(
        color = TextPrimary,
        fontFamily = robotoA1(),
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    button = TextStyle(
        color = Color.White, // или любой другой цвет для текста кнопки
        fontFamily = robotoA1(),
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        color = TextSecondary,
        fontFamily = robotoA1(),
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    ),
    overline = TextStyle(
        color = TextSecondary,
        fontFamily = robotoA1(),
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp
    )
)