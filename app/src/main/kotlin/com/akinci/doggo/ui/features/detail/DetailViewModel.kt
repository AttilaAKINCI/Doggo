package com.akinci.doggo.ui.features.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akinci.doggo.core.coroutine.ContextProvider
import com.akinci.doggo.domain.GetImagesUseCase
import com.akinci.doggo.ui.features.detail.DetailViewContract.ScreenArgs
import com.akinci.doggo.ui.features.detail.DetailViewContract.State
import com.akinci.doggo.ui.features.detail.DetailViewContract.Type
import com.akinci.doggo.ui.features.navArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val contextProvider: ContextProvider,
    private val getImagesUseCase: GetImagesUseCase,
) : ViewModel() {

    private val screenArgs by lazy { savedStateHandle.navArgs<ScreenArgs>() }

    private val _stateFlow: MutableStateFlow<State> = MutableStateFlow(State())
    val stateFlow = _stateFlow.asStateFlow()

    init {
        // set detail screen title
        _stateFlow.update { state ->
            state.copy(
                title = buildString {
                    append(screenArgs.breed)
                    screenArgs.subBreed?.let {
                        append("/${it}")
                    }
                }
            )
        }

        fetchContent(
            breed = screenArgs.breed.lowercase(),
            subBreed = screenArgs.subBreed?.lowercase(),
        )
    }

    private fun fetchContent(breed: String, subBreed: String?) {
        viewModelScope.launch {

            // simulate network delay to show proper loading
            delay(1000L)

            withContext(contextProvider.io) {
                getImagesUseCase.execute(
                    breed = breed,
                    subBreed = subBreed,
                )
            }.onSuccess { list ->
                if (list.isNotEmpty()) {
                    _stateFlow.update {
                        it.copy(type = Type.Content(list.toPersistentList()))
                    }
                } else {
                    _stateFlow.update {
                        it.copy(type = Type.NoData)
                    }
                }
            }.onFailure {
                _stateFlow.update {
                    it.copy(type = Type.Error)
                }
            }
        }
    }
}
