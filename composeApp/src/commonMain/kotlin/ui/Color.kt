package ui

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val PrimaryColor = Color(0xff9c27b0) // (Основной цвет)
val PrimaryDarkColor = Color(0xff7b1fa2) // (Темный оттенок основного цвета)
val PrimaryLightColor = Color(0xffe1bee7) // (Светлый оттенок основного цвета)
val PrimaryVeryLightColor = Color(0xfff5e4f5)
val PrimaryVeryDarkColor = Color(0xff290229)

val Gold = Color(0xffFFD700)

val SecondaryColor = Color(0xff757575) //(Вторичный цвет)
val AccentColor = Color(0xff00BCD4) //(Акцентный цвет)
val AccentColorBreigth = Color(0xff0cf7d8) //(Акцентный цвет)
val BackgroundColor = Color(0xffF5F5F5) //(Цвет фона)
val TextColor = Color(0xff212121) //(Цвет текста)
val DisabledColor = Color(0xffBDBDBD) //(Цвет для отключенных элементов)
val TextPrimary = Color(0xff212121)
val TextSecondary = Color(0xff757575)

val Gradient1 = Color(0xff9442a1)
val Gradient2 = Color(0xffa150ad)
val Gradient3 = Color(0xffae63ba)
val Gradient4 = Color(0xffb573bf)
val Gradient5 = Color(0xffbc84c4)
val Gradient6 = Color(0xffca9dd1)
val Gradient7 = Color(0xffd4b2d9)
val Gradient8 = Color(0xfff7daf7)

val TextWhite = Color(0xffFFFFFF)
val DeepBlue = Color(0xff06164c)
val ButtonBlue = Color(0xff505cf3)
val DarkerButtonBlue = Color(0xff566894)
val LightRed = Color(0xfffc879a)
val AquaBlue = Color(0xff9aa5c4)
val OrangeYellow1 = Color(0xfff0bd28)
val OrangeYellow2 = Color(0xfff1c746)
val OrangeYellow3 = Color(0xfff4cf65)
val Beige1 = Color(0xfffdbda1)
val Beige2 = Color(0xfffcaf90)
val Beige3 = Color(0xfff9a27b)
val LightGreen1 = Color(0xff54e1b6)
val LightGreen2 = Color(0xff36ddab)
val LightGreen3 = Color(0xff11d79b)
val BlueViolet1 = Color(0xffaeb4fd)
val BlueViolet2 = Color(0xff9fa5fe)
val BlueViolet3 = Color(0xff8f98fd)

//Gradient for rings
val RedGradient1 = Color(0xffff512f)
val RedGradient2 = Color(0xffdd2476)

val BlueGradient1 = Color(0xffd16ba5)
val BlueGradient2 = Color(0xff86a8e7)
val BlueGradient3 = Color(0xff5ffbf1)

val VioletGradient1 = Color(0xfff1a7f1)
val VioletGradient2 = Color(0xfffad0c4)

val ViewingGradient1 = Color(0xff4158d0)
val ViewingGradient2 = Color(0xffc850c0)
val ViewingGradient3 = Color(0xffffcc70)


val brushRedGradient = listOf(RedGradient1, RedGradient2)
val brushBlueGradient = listOf(BlueGradient1, BlueGradient2, BlueGradient3)
val brushVioletGradient = listOf(VioletGradient1, VioletGradient2)
val brushViewingGradient = listOf(ViewingGradient1, ViewingGradient2, ViewingGradient3)

val brushTools = listOf(
    Brush.linearGradient(brushViewingGradient),
    Brush.linearGradient(brushBlueGradient),
    Brush.linearGradient(brushVioletGradient)
)