package com.akinci.doggo.ui.features.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akinci.doggo.core.compose.reduce
import com.akinci.doggo.ui.features.splash.SplashViewContract.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor() : ViewModel() {

    private val _stateFlow: MutableStateFlow<State> = MutableStateFlow(State())
    val stateFlow = _stateFlow.asStateFlow()

    init {
        initialize()
    }

    private fun initialize() {
        viewModelScope.launch {
            // simulate initialization with delay
            delay(3000)

            // complete initial animation, proceed dashboard.
            _stateFlow.reduce {
                copy(isCompleted = true)
            }
        }
    }
}