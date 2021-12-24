package com.akinci.doggoapp.feature.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akinci.doggoapp.common.coroutine.CoroutineContextProvider
import com.akinci.doggoapp.common.helper.NetworkResponse
import com.akinci.doggoapp.common.helper.state.ListState
import com.akinci.doggoapp.common.helper.state.UIState
import com.akinci.doggoapp.common.network.NetworkChecker
import com.akinci.doggoapp.data.local.dao.BreedDao
import com.akinci.doggoapp.data.local.dao.SubBreedDao
import com.akinci.doggoapp.data.mapper.convertToBreedListEntity
import com.akinci.doggoapp.data.mapper.convertToSubBreedListEntity
import com.akinci.doggoapp.data.repository.DoggoRepository
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
    private val doggoRepository: DoggoRepository,
    private val breedDao: BreedDao,
    private val subBreedDao: SubBreedDao,
    val networkChecker: NetworkChecker
): ViewModel() {

    var firstLoading = true

    var selectedBreed: Breed? = null
    var selectedSubBreed: Breed? = null

    private var breedList = listOf<Breed>()
    private var subBreedList = listOf<Breed>()

    /** Fragments are driven with states **/
    private var _breedListData = MutableStateFlow<ListState<List<Breed>>>(ListState.None)
    var breedListData: StateFlow<ListState<List<Breed>>> = _breedListData

    private var _subBreedListData = MutableStateFlow<ListState<List<Breed>>>(ListState.None)
    var subBreedListData: StateFlow<ListState<List<Breed>>> = _subBreedListData

    private var _continueButtonState = MutableStateFlow(false)
    var continueButtonState: StateFlow<Boolean> = _continueButtonState

    /** works like send and forget **/
    private var _uiState = MutableStateFlow<UIState>(UIState.None)
    var uiState: StateFlow<UIState> = _uiState

    init {
        Timber.d("DashboardViewModel created..")
    }

    fun selectBreed(breed: Breed){
        viewModelScope.launch(coroutineContext.IO) {
            val newList = mutableListOf<Breed>()
            breedList.forEach { item -> newList.add(item.copy().apply {
                selected = (name == breed.name)
            }) }

            breedList = newList
            selectedBreed = breed
            selectedSubBreed = null
            _breedListData.emit(ListState.OnData(newList))
        }
    }

    fun selectSubBreed(breed: Breed){
        viewModelScope.launch(coroutineContext.IO) {
            val newList = mutableListOf<Breed>()
            subBreedList.forEach { item -> newList.add(item.copy().apply {
                selected = (name == breed.name)
            }) }

            subBreedList = newList
            selectedSubBreed = breed
            _subBreedListData.emit(ListState.OnData(newList))
            _continueButtonState.emit(true)
        }
    }

    fun getBreedList() {
        if(breedList.isEmpty()){
            viewModelScope.launch(coroutineContext.IO) {
                doggoRepository.getBreedList().collect { networkResponse ->
                    when(networkResponse){
                        is NetworkResponse.Loading -> { _breedListData.emit(ListState.OnLoading) }
                        is NetworkResponse.Error -> { _uiState.emit(UIState.OnServiceError) }
                        is NetworkResponse.Success -> {
                            networkResponse.data?.let {
                                it.message.keys.map { item -> Breed(item) } // service response mapped Breed object
                                    .apply {
                                        breedList = this
                                        // saves fetched data to room db
                                        breedDao.insertBreed(convertToBreedListEntity())
                                        _breedListData.emit(ListState.OnData(breedList))
                                    }
                            }
                        }
                    }
                }
            }
        }
    }

    fun getSubBreedList(breed: String) {
        viewModelScope.launch(coroutineContext.IO) {
            doggoRepository.getSubBreedList(breed).collect { networkResponse ->
                when(networkResponse){
                    is NetworkResponse.Loading -> { _subBreedListData.emit(ListState.OnLoading) }
                    is NetworkResponse.Error -> { _uiState.emit(UIState.OnServiceError) }
                    is NetworkResponse.Success -> {
                        networkResponse.data?.let {
                            it.message.map { item -> Breed(item)}.apply {
                                subBreedList = this
                                // saves fetched data to room db
                                subBreedDao.insertSubBreed(convertToSubBreedListEntity(ownerBreed = breed))
                                _subBreedListData.emit(ListState.OnData(subBreedList))
                                if(isEmpty()){
                                    /** we fetched sub breed according to breed but there is no sub breed
                                     *  we need to continue.
                                     * **/
                                    _continueButtonState.emit(true)
                                }else{
                                    _continueButtonState.emit(false)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}