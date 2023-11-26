package com.akinci.doggo.ui.features.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akinci.doggo.core.compose.reduce
import com.akinci.doggo.core.coroutine.ContextProvider
import com.akinci.doggo.data.subbreed.SubBreedRepository
import com.akinci.doggo.domain.breed.BreedUseCase
import com.akinci.doggo.ui.features.dashboard.DashboardViewContract.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val contextProvider: ContextProvider,
    private val breedUseCase: BreedUseCase,
    private val subBreedRepository: SubBreedRepository,
) : ViewModel() {

    private val _stateFlow: MutableStateFlow<State> = MutableStateFlow(State())
    val stateFlow = _stateFlow.asStateFlow()

    init {
        fetchBreeds()
    }

    private fun fetchBreeds() {
        viewModelScope.launch {
            // switch ui to shimmer loading mode
            _stateFlow.reduce {
                copy(
                    isShimmerLoading = true,
                    isNoData = false,
                    isError = false,
                )
            }

            val response = withContext(contextProvider.io) {
                breedUseCase.getBreeds()
            }

            if (response.isNotEmpty()) {
                // we don't have breed to show, no data
                _stateFlow.reduce {
                    copy(
                        isShimmerLoading = false,
                        isNoData = false,
                        isError = false,
                        breedList = response
                            .map { it.name }
                            .toPersistentList(),
                    )
                }
            } else {
                // we don't have breed to show, no data
                _stateFlow.reduce {
                    copy(
                        isShimmerLoading = false,
                        isNoData = true,
                        isError = false,
                    )
                }
            }
        }
    }

    private fun fetchSubBreeds() {

    }

    fun selectBreed(name: String) {
        _stateFlow.reduce {
            copy(
                selectedBreed = name,
                selectedSubBreed = null,
            )
        }

        // fetchSubBreed should return flag to set isDetailButtonActive if there is no subBreed
        fetchSubBreeds()
    }

    fun selectSubBreed(name: String) {
        _stateFlow.reduce {
            copy(
                selectedSubBreed = name,
                isDetailButtonActive = true,
            )
        }

    }

    /*var selectedBreedName: String = ""
    var selectedSubBreedName: String = ""

    // pass data to composable ui via states
    var breedListState by mutableStateOf(listOf<Breed>())
        private set

    var subBreedListState by mutableStateOf(listOf<Breed>())
        private set

    */
    /** Network warning popup should seen once **//*
    var isNetworkWarningDialogVisible by mutableStateOf(true)
        private set

    var informer by mutableStateOf<UIState>(UIState.None)
        private set


    init {
        Timber.d("DashboardViewModel created..")
        getBreedList()
    }

    fun selectBreed(breedName: String){
        if(breedName != selectedBreedName){
            Timber.d("Breed selected -> $breedName")
            selectedBreedName = breedName

            selectedSubBreedName = ""       // clear sub breed selection
            subBreedListState = listOf()    // clear sub breed list
            getSubBreedList(breedName)      // fetch new sub breed list
        }
    }
    fun selectSubBreed(subBreedName: String){
        selectedSubBreedName = subBreedName
    }
    fun networkWarningSeen(){
        isNetworkWarningDialogVisible = false
    }

    fun validate(): Boolean{
        if(selectedBreedName.isNotBlank()){
            if(subBreedListState.isNotEmpty()){
                if(selectedSubBreedName.isNotBlank()){
                    return true
                }
            }else{
                return true
            }
        }

        return false
    }

    fun getBreedList() {
        Timber.d("DashboardViewModel:: getBreedList called")
        if(breedListState.isEmpty()){
            viewModelScope.launch(coroutineContext.IO) {
                doggoRepository.getBreedList().collect { networkResponse ->
                    when(networkResponse){
                        is NetworkResponse.Loading -> {
                            Timber.d("DashboardViewModel:: Breed list loading..")
                            withContext(this@DashboardViewModel.coroutineContext.Main) { informer = UIState.OnLoading }
                        }
                        is NetworkResponse.Error -> {
                            Timber.d("DashboardViewModel:: Breed list service error..")
                            withContext(this@DashboardViewModel.coroutineContext.Main) { informer = UIState.OnServiceError }
                        }
                        is NetworkResponse.Success -> {
                            networkResponse.data?.let {
                                it.message.keys.map { item -> Breed(item) } // service response mapped Breed object
                                    .apply {
                                        Timber.d("Breed list fetched size:-> $size")
                                        // saves fetched data to room db
                                        Timber.d("Breed list saved on th e ROOM DB")
                                        breedDao.insertBreed(convertToBreedListEntity())
                                        Timber.d("Breed list send as a state to UI")
                                        breedListState = this
                                    }
                            }
                        }
                    }
                }
            }
        }
    }

    fun getSubBreedList(breed: String) {
        Timber.d("DashboardViewModel:: getSubBreedList called")
        viewModelScope.launch(coroutineContext.IO) {
            doggoRepository.getSubBreedList(breed).collect { networkResponse ->
                when(networkResponse){
                    is NetworkResponse.Loading -> {
                        Timber.d("DashboardViewModel:: Sub Breed list loading..")
                        withContext(this@DashboardViewModel.coroutineContext.Main) { informer = UIState.OnLoading }
                    }
                    is NetworkResponse.Error -> {
                        Timber.d("DashboardViewModel:: Sub Breed list service error..")
                        withContext(this@DashboardViewModel.coroutineContext.Main) { informer = UIState.OnServiceError }
                    }
                    is NetworkResponse.Success -> {
                        networkResponse.data?.let {
                            it.message.map { item -> Breed(item)}.apply {
                                Timber.d("Sub Breed list fetched size:-> $size")
                                // saves fetched data to room db
                                Timber.d("Sub Breed list saved on ROOM DB")
                                subBreedDao.insertSubBreed(convertToSubBreedListEntity(ownerBreed = breed))
                                Timber.d("Sub Breed list send as a state to UI")
                                subBreedListState = this
                            }
                        }
                    }
                }
            }
        }
    }*/
}