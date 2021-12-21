package com.akinci.doggoapp.feature.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akinci.doggoapp.common.coroutine.CoroutineContextProvider
import com.akinci.doggoapp.common.helper.NetworkResponse
import com.akinci.doggoapp.data.doggo.repository.DoggoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val coroutineContext: CoroutineContextProvider,
    private val doggoRepository: DoggoRepository
): ViewModel() {


    init {
        Timber.d("DashboardViewModel created..")
    }

    fun getBreedList() {
        viewModelScope.launch(coroutineContext.IO) {
            doggoRepository.getBreeds().collect { networkResponse ->
                when(networkResponse){
                    is NetworkResponse.Loading -> { }
                    is NetworkResponse.Error -> { }
                    is NetworkResponse.Success -> {

                    }
                }
            }
        }
    }
}