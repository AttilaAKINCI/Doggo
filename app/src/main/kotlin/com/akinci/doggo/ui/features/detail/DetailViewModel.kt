package com.akinci.doggo.ui.features.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.akinci.doggo.core.coroutine.ContextProvider
import com.akinci.doggo.data.image.ImageRepository
import com.akinci.doggo.ui.features.detail.DetailViewContract.State
import com.akinci.doggo.ui.features.detail.DetailViewContract.ScreenArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val contextProvider: ContextProvider,
    private val imageRepository: ImageRepository,
) : ViewModel() {

    val screenArgs by lazy { "" }

    private val _stateFlow: MutableStateFlow<State> = MutableStateFlow(State())
    val stateFlow = _stateFlow.asStateFlow()

    /*  // pass data to composable ui via states
      var breedImageListState by mutableStateOf(listOf<Content>())
          private set

      private val fragmentArgBreed by lazy { savedStateHandle.get("breed") ?: "" }
      private val fragmentArgSubBreed by lazy { savedStateHandle.get("subBreed") ?: "" }

      */
    /** Network warning popup should seen once **//*
    var isNetworkWarningDialogVisible by mutableStateOf(true)
        private set

    var informer by mutableStateOf<UIState>(UIState.None)
        private set

    fun networkWarningSeen(){
        isNetworkWarningDialogVisible = false
    }

    init {
        Timber.d("DetailViewModel created..")

        getDoggoContent(fragmentArgBreed, fragmentArgSubBreed, count = 15)
    }

    fun getDoggoContent(breed: String, subBreed: String = "", count :Int) {
        viewModelScope.launch(coroutineContext.IO) {
            doggoRepository.getDoggoContent(breed = breed, subBreed = subBreed, count = count).collect { networkResponse ->
                when(networkResponse){
                    is NetworkResponse.Loading -> {
                        Timber.d("DetailViewModel:: Doggo content list loading..")
                        withContext(this@DetailViewModel.coroutineContext.Main) { informer = UIState.OnLoading }
                    }
                    is NetworkResponse.Error -> {
                        Timber.d("DetailViewModel:: Doggo content service error..")
                        withContext(this@DetailViewModel.coroutineContext.Main) { informer = UIState.OnServiceError }
                    }
                    is NetworkResponse.Success -> {
                        networkResponse.data?.message?.let {
                            Timber.d("DetailViewModel:: Doggo content fetched..")
                            // saves fetched data to room db
                            Timber.d("DetailViewModel:: Doggo content is written to ROOM DB")
                            contentDao.insertImage(contentList = it.convertToDoggoContentListEntity(breed = breed, subBreed = subBreed))

                            Timber.d("DetailViewModel:: Doggo content is send as a state to UI")
                            breedImageListState = it.map { imageUrl ->
                                Content(
                                    imageUrl = imageUrl,
                                    dogName = DogNameProvider.getRandomName()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
*/

}