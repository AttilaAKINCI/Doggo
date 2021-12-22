package com.akinci.doggoapp.feature.dashboard.viewmodel

sealed class DashboardState<out T> {
    object None: DashboardState<Nothing>()
    object OnBreedLoading: DashboardState<Nothing>()
    object OnSubBreedLoading: DashboardState<Nothing>()
    object OnServiceError: DashboardState<Nothing>()
    class OnBreedDataReceived<T>(val data: T): DashboardState<T>()
    class OnSubBreedDataReceived<T>(val data: T): DashboardState<T>()
}