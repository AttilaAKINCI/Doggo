package com.akinci.doggoapp.feature.detail.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akinci.doggoapp.common.coroutine.CoroutineContextProvider
import com.akinci.doggoapp.common.helper.NetworkResponse
import com.akinci.doggoapp.common.helper.state.ListState
import com.akinci.doggoapp.common.helper.state.UIState
import com.akinci.doggoapp.data.doggo.repository.DoggoRepository
import com.akinci.doggoapp.feature.dashboard.data.Breed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val coroutineContext: CoroutineContextProvider,
    private val doggoRepository: DoggoRepository
): ViewModel() {

    /** Fragments are driven with states **/
    private var _breedImageListData = MutableStateFlow<ListState<List<String>>>(ListState.None)
    var breedImageListData: StateFlow<ListState<List<String>>> = _breedImageListData

    /** works like send and forget **/
    private var _uiState = MutableStateFlow<UIState>(UIState.None)
    var uiState: StateFlow<UIState> = _uiState

    init {
        Timber.d("DetailViewModel created..")
    }

    fun getBreeds(breed: String) {
        viewModelScope.launch(coroutineContext.IO) {
            doggoRepository.getBreeds(breed).collect { networkResponse ->
                when(networkResponse){
                    is NetworkResponse.Loading -> { _breedImageListData.emit(ListState.OnLoading) }
                    is NetworkResponse.Error -> { _uiState.emit(UIState.OnServiceError) }
                    is NetworkResponse.Success -> {
                        delay(3000L) // in order to simulate network delay. show shimmer
                        _breedImageListData.emit(ListState.OnData(networkResponse.data?.message))
                    }
                }
            }
        }
    }

    fun getSubBreeds(breed: String, subBreed: String) {
        viewModelScope.launch(coroutineContext.IO) {
            doggoRepository.getSubBreeds(breed, subBreed).collect { networkResponse ->
                when(networkResponse){
                    is NetworkResponse.Loading -> { _breedImageListData.emit(ListState.OnLoading) }
                    is NetworkResponse.Error -> { _uiState.emit(UIState.OnServiceError) }
                    is NetworkResponse.Success -> {
                        delay(3000L) // in order to simulate network delay. show shimmer
                        _breedImageListData.emit(ListState.OnData(networkResponse.data?.message))
                    }
                }
            }
        }
    }

}