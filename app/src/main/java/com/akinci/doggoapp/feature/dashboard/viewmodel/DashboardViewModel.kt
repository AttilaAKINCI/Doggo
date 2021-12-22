package com.akinci.doggoapp.feature.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akinci.doggoapp.common.coroutine.CoroutineContextProvider
import com.akinci.doggoapp.common.helper.NetworkResponse
import com.akinci.doggoapp.data.doggo.repository.DoggoRepository
import com.akinci.doggoapp.feature.dashboard.data.Breed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val coroutineContext: CoroutineContextProvider,
    private val doggoRepository: DoggoRepository
): ViewModel() {

    var selectedBreedPosition = -1
    var selectedSubBreedPosition = -1

    private var breedList = mutableListOf<Breed>()
    private var subBreedList = mutableListOf<Breed>()

    /** Fragments are driven with states **/
    private var _dashboardState = MutableStateFlow<DashboardState<List<Breed>>>(DashboardState.None)
    var dashboardState: StateFlow<DashboardState<List<Breed>>> = _dashboardState

    init {
        Timber.d("DashboardViewModel created..")
    }

    fun getBreedList() {
        if(breedList.isEmpty()){
            viewModelScope.launch(coroutineContext.IO) {
                doggoRepository.getBreedList().collect { networkResponse ->
                    when(networkResponse){
                        is NetworkResponse.Loading -> { _dashboardState.emit(DashboardState.OnBreedLoading) }
                        is NetworkResponse.Error -> { _dashboardState.emit(DashboardState.OnServiceError) }
                        is NetworkResponse.Success -> {
                            networkResponse.data?.let {
                                it.message.keys.toMutableList()
                                    .map { item -> Breed(item) } // service response mapped Breed object
                                    .apply {
                                        breedList.addAll(this)
                                        _dashboardState.emit(DashboardState.OnBreedDataReceived(this))
                                    }
                            }
                        }
                    }
                }
            }
        }
    }

    fun getSubBreedList(breed: String) {
        if(subBreedList.isEmpty()){
            viewModelScope.launch(coroutineContext.IO) {
                doggoRepository.getSubBreedList(breed).collect { networkResponse ->
                    when(networkResponse){
                        is NetworkResponse.Loading -> { _dashboardState.emit(DashboardState.OnSubBreedLoading) }
                        is NetworkResponse.Error -> { _dashboardState.emit(DashboardState.OnServiceError) }
                        is NetworkResponse.Success -> {
                            networkResponse.data?.let {
                                it.message.toMutableList()
                                    .map { item -> Breed(item)}
                                    .apply {
                                        breedList.addAll(this)
                                        _dashboardState.emit(DashboardState.OnSubBreedDataReceived(this))
                                    }
                            }
                        }
                    }
                }
            }
        }
    }
}