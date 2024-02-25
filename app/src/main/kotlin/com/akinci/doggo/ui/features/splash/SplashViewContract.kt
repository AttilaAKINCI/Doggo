package com.akinci.doggo.ui.features.splash

object SplashViewContract {
    sealed interface Effect {
        data object Completed : Effect
    }
}
