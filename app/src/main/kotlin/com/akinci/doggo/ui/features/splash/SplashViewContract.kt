package com.akinci.doggo.ui.features.splash

import com.akinci.doggo.core.compose.UIState

object SplashViewContract {

    data class State(
        val isCompleted: Boolean = false
    ): UIState
}