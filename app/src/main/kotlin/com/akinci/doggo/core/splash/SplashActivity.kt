package com.akinci.doggo.core.splash

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.akinci.doggo.ui.features.splash.SplashScreen


/**
 * Wrap Native SplashActivity with transparent one and directly skip it.
 * We have complex animation handled by lottie on [SplashScreen].
 * **/
@SuppressLint("CustomSplashScreen")
abstract class SplashActivity : ComponentActivity() {

    private var initializationCompleted: Boolean by mutableStateOf(true)

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition { !initializationCompleted }
    }
}