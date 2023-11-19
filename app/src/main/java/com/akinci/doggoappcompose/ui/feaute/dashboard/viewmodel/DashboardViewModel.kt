package com.akinci.doggoappcompose.ui.feaute.dashboard.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akinci.doggoappcompose.common.coroutine.CoroutineContextProvider
import com.akinci.doggoappcompose.common.helper.state.UIState
import com.akinci.doggoappcompose.common.network.NetworkChecker
import com.akinci.doggoappcompose.common.network.NetworkResponse
import com.akinci.doggoappcompose.data.local.dao.BreedDao
import com.akinci.doggoappcompose.data.local.dao.SubBreedDao
import com.akinci.doggoappcompose.data.mapper.convertToBreedListEntity
import com.akinci.doggoappcompose.data.mapper.convertToSubBreedListEntity
import com.akinci.doggoappcompose.data.repository.DoggoRepository
import com.akinci.doggoappcompose.ui.feaute.dashboard.data.Breed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val coroutineContext: CoroutineContextProvider,
    private val doggoRepository: DoggoRepository,
    private val breedDao: BreedDao,
    private val subBreedDao: SubBreedDao
): ViewModel() {

    var selectedBreedName: String = ""
    var selectedSubBreedName: String = ""

    // pass data to composable ui via states
    var breedListState by mutableStateOf(listOf<Breed>())
        private set

    var subBreedListState by mutableStateOf(listOf<Breed>())
        private set

    /** Network warning popup should seen once **/
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
    }
}