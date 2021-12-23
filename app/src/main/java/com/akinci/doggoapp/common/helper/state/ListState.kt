package com.akinci.doggoapp.common.helper.state

sealed class ListState<out T> {
    object None: ListState<Nothing>()
    object OnLoading: ListState<Nothing>()
    class OnData<T>(val data: T?): ListState<T>()
}