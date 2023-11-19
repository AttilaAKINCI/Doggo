package com.akinci.doggoappcompose.common.network

sealed class NetworkResponse<out T> {
    data class Success<T>(val data: T?) : NetworkResponse<T>()
    data class Error(val message: String) : NetworkResponse<Nothing>()
    data class Loading(val message: String = "") : NetworkResponse<Nothing>()
}