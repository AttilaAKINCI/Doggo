package com.akinci.doggo.core.compose

import kotlinx.coroutines.flow.MutableStateFlow

interface UIState

fun <T : UIState> MutableStateFlow<T>.reduce(reducer: T.() -> T) {
    this.value = reducer(this.value)
}