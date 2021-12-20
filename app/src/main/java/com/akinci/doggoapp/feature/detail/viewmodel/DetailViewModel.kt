package com.akinci.doggoapp.feature.detail.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(

): ViewModel() {

    init {
        Timber.d("DetailViewModel created..")
    }
}