package com.akinci.doggo.ui.features.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akinci.doggo.ui.features.splash.SplashViewContract.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(

) : ViewModel() {

    private val _stateFlow: MutableStateFlow<State> = MutableStateFlow(
        State(isCompleted = false)
    )
    val stateFlow = _stateFlow.asStateFlow()

    init {
        viewModelScope.launch {
            // Simulate app initialization period
            delay(3000)

            _stateFlow.value = State(isCompleted = true)
        }
    }
}