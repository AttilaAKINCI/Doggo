package com.akinci.doggo.ui.ds.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale

@Composable
fun TiledBackground(
    modifier: Modifier = Modifier,
    painter: Painter,
    content: @Composable BoxScope.() -> Unit
) {
    val tintColor = if (isSystemInDarkTheme()) {
        Color(0xFF444444)
    } else {
        Color(0xFF555555)
    }

    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painter,
            contentDescription = "",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            colorFilter = ColorFilter.tint(
                color = tintColor,
            ),
        )

        content()
    }
}