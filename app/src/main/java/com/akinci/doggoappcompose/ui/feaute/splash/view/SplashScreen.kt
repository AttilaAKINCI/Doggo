package com.akinci.doggoappcompose.ui.feaute.splash.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.akinci.doggoappcompose.R
import com.akinci.doggoappcompose.ui.theme.DoggoAppComposeTheme
import kotlinx.coroutines.delay

private const val SplashWaitTime: Long = 3000

/**
 * Stateful version of the Podcast player
 */
@Composable
fun SplashScreen(
    onTimeout: ()->Unit
){
    SplashScreenBody(onTimeout = onTimeout)
}

/**
 * Stateless version of the Player screen
 */
@Composable
private fun SplashScreenBody(
    onTimeout: ()->Unit,
    modifier: Modifier = Modifier
) {
    Surface(modifier) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            val currentOnTimeout by rememberUpdatedState(onTimeout)
            LaunchedEffect(true) {
                delay(SplashWaitTime) // Simulates loading things
                currentOnTimeout()
            }

            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.doggo))
            LottieAnimation(
                composition,
                iterations = Int.MAX_VALUE
            )
        }
    }
}
