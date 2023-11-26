package com.akinci.doggo.ui.ds.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val WhiteLight = Color(0xFFFFFFFF)
val WhiteDark = Color(0xF2FFFFFF)

val BlueLight = Color(0xFF337BE2)
val BlueDark = Color(0xFF4189EF)

val GreyLight = Color(0xFFDDDDDD)
val GreyDark = Color(0xFF555555)

val Teal = Color(0xD903DAC5)

val Black = Color(0xFF131313)

val LightColorScheme = lightColorScheme(
    primary = BlueLight,
    onPrimary = WhiteLight,
    secondary = GreyLight,
    background = WhiteLight,
    onBackground = Black,
    surface = WhiteLight,
    onSurface = Black,
    surfaceVariant = Teal,
    onSurfaceVariant = Black,
)

val DarkColorScheme = darkColorScheme(
    primary = BlueDark,
    onPrimary = WhiteDark,
    secondary = GreyDark,
    background = Black,
    onBackground = WhiteDark,
    surface = Black,
    onSurface = WhiteDark,
    surfaceVariant = Teal,
    onSurfaceVariant = WhiteDark,
)
