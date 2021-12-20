package com.akinci.doggoapp.feature.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(

): ViewModel() {

    init {
        Timber.d("DashboardViewModel created..")
    }
}