package com.akinci.doggoapp.common.helper.state

sealed class UIState {
    object None: UIState()
    object OnServiceError: UIState()
}