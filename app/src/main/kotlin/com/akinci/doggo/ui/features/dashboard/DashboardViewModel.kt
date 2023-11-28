package com.akinci.doggo.ui.features.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akinci.doggo.core.compose.reduce
import com.akinci.doggo.core.coroutine.ContextProvider
import com.akinci.doggo.core.network.NetworkChecker
import com.akinci.doggo.core.utils.capitalise
import com.akinci.doggo.domain.breed.BreedUseCase
import com.akinci.doggo.domain.breed.toListItem
import com.akinci.doggo.domain.subBreed.SubBreedUseCase
import com.akinci.doggo.domain.subBreed.toListItem
import com.akinci.doggo.ui.features.dashboard.DashboardViewContract.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val contextProvider: ContextProvider,
    private val breedUseCase: BreedUseCase,
    private val subBreedUseCase: SubBreedUseCase,
    private val networkChecker: NetworkChecker,
) : ViewModel() {

    private val _stateFlow: MutableStateFlow<State> = MutableStateFlow(State())
    val stateFlow = _stateFlow.asStateFlow()

    init {
        fetchBreeds()
        subscribeToNetworkStatus()
    }

    private fun subscribeToNetworkStatus() {
        networkChecker.state
            .onEach { _stateFlow.reduce { copy(isConnected = it) } }
            .launchIn(viewModelScope)
    }

    private fun fetchBreeds() {
        viewModelScope.launch {
            // switch ui to shimmer loading mode for breeds
            _stateFlow.reduce {
                copy(
                    isLoading = true,
                    isNoData = false,
                )
            }

            // simulate network delay to show proper loading
            delay(1000L)

            withContext(contextProvider.io) {
                breedUseCase.getBreeds()
            }.onSuccess { list ->
                if (list.isNotEmpty()) {
                    // we have breeds to show
                    _stateFlow.reduce {
                        copy(
                            isLoading = false,
                            isNoData = false,
                            breedList = list
                                .toListItem()
                                .map {
                                    it.copy(name = it.name.capitalise())
                                }.toPersistentList(),
                        )
                    }
                } else {
                    // we don't have breed to show, no data
                    _stateFlow.reduce {
                        copy(
                            isLoading = false,
                            isNoData = true,
                        )
                    }
                }
            }.onFailure {
                // todo fix here.
            }
        }
    }

    private fun fetchSubBreeds() {
        val state = stateFlow.value
        if (state.selectedBreed == null) return

        viewModelScope.launch {
            withContext(contextProvider.io) {
                subBreedUseCase.getSubBreeds(breed = state.selectedBreed.lowercase())
            }.onSuccess { list ->
                if (list.isNotEmpty()) {
                    // we have sub breeds to show
                    _stateFlow.reduce {
                        copy(
                            isDetailButtonActive = false,
                            selectedSubBreed = null,
                            subBreedList = list
                                .toListItem()
                                .map {
                                    it.copy(name = it.name.capitalise())
                                }.toPersistentList(),
                        )
                    }
                } else {
                    // we don't have sub breed to show
                    _stateFlow.reduce {
                        copy(
                            isDetailButtonActive = true,
                            selectedSubBreed = null,
                            subBreedList = persistentListOf(),
                        )
                    }
                }
            }.onFailure {
                // todo fix here
            }
        }
    }

    fun selectBreed(name: String) {
        val breedList = stateFlow.value.breedList
            .map {
                it.copy(selected = it.name == name)
            }

        _stateFlow.reduce {
            copy(
                selectedBreed = name,
                breedList = breedList.toPersistentList(),
            )
        }

        fetchSubBreeds()
    }

    fun selectSubBreed(name: String) {
        val subBreedList = stateFlow.value.subBreedList
            .map {
                it.copy(selected = it.name == name)
            }

        _stateFlow.reduce {
            copy(
                isDetailButtonActive = true,
                selectedSubBreed = name,
                subBreedList = subBreedList.toPersistentList(),
            )
        }
    }
}
