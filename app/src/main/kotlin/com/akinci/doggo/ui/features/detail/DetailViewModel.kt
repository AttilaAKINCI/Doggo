package com.akinci.doggo.ui.features.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akinci.doggo.core.compose.reduce
import com.akinci.doggo.core.coroutine.ContextProvider
import com.akinci.doggo.domain.data.ImagesUseCase
import com.akinci.doggo.ui.features.detail.DetailViewContract.ScreenArgs
import com.akinci.doggo.ui.features.detail.DetailViewContract.State
import com.akinci.doggo.ui.features.navArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val contextProvider: ContextProvider,
    private val imagesUseCase: ImagesUseCase,
) : ViewModel() {

    private val screenArgs by lazy { savedStateHandle.navArgs<ScreenArgs>() }

    private val _stateFlow: MutableStateFlow<State> = MutableStateFlow(
        State(
            title = buildString {
                append(screenArgs.breed)
                screenArgs.subBreed?.let {
                    append("/${screenArgs.breed}")
                }
            },
        )
    )
    val stateFlow = _stateFlow.asStateFlow()

    init {
        fetchContent()
    }

    private fun fetchContent() {
        viewModelScope.launch {

            // simulate network delay to show proper loading
            delay(1000L)

            withContext(contextProvider.io) {
                imagesUseCase.getImage(
                    breed = screenArgs.breed.lowercase(),
                    subBreed = screenArgs.subBreed?.lowercase(),
                )
            }.onSuccess { list ->
                if (list.isNotEmpty()) {
                    _stateFlow.reduce {
                        copy(
                            isLoading = false,
                            images = list.toPersistentList()
                        )
                    }
                } else {
                    _stateFlow.reduce {
                        copy(
                            isLoading = false,
                            isNoData = true,
                            isError = false,
                        )
                    }
                }
            }.onFailure {
                _stateFlow.reduce {
                    copy(
                        isLoading = false,
                        isNoData = false,
                        isError = true,
                    )
                }
            }
        }
    }
}