package com.akinci.doggo.ui.ds.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val White = Color(0xFFFFFFFF)
val WhiteDark = Color(0xF2FFFFFF)

val Blue = Color(0xFF337BE2)
val BlueDark = Color(0xFF4189EF)

val Gray = Color(0xFFEAEAEA)
val GrayDark = Color(0xFF333333)

val Black = Color(0xFF000000)

val LightColorScheme = lightColorScheme(
    primary = Blue,
    onPrimary = White,
    secondary = Gray,
    onSecondary = White,
    onBackground = Black,
    surface = White,
    onSurface = Black,
)

val DarkColorScheme = darkColorScheme(
    primary = BlueDark,
    onPrimary = WhiteDark,
    secondary = GrayDark,
    onSecondary = WhiteDark,
    onBackground = WhiteDark,
    surface = Black,
    onSurface = WhiteDark,
)