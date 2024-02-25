package com.akinci.doggo.ui.features.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akinci.doggo.core.coroutine.ContextProvider
import com.akinci.doggo.core.network.NetworkChecker
import com.akinci.doggo.core.utils.capitalise
import com.akinci.doggo.domain.GetBreedsUseCase
import com.akinci.doggo.domain.GetSubBreedsUseCase
import com.akinci.doggo.domain.data.Chip
import com.akinci.doggo.ui.features.dashboard.DashboardViewContract.BreedStateType
import com.akinci.doggo.ui.features.dashboard.DashboardViewContract.Effect
import com.akinci.doggo.ui.features.dashboard.DashboardViewContract.State
import com.akinci.doggo.ui.features.dashboard.DashboardViewContract.SubBreedStateType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val contextProvider: ContextProvider,
    private val getBreedsUseCase: GetBreedsUseCase,
    private val getSubBreedsUseCase: GetSubBreedsUseCase,
    private val networkChecker: NetworkChecker,
) : ViewModel() {

    private val _stateFlow: MutableStateFlow<State> = MutableStateFlow(State())
    val stateFlow = _stateFlow.asStateFlow()

    private val _effect by lazy { Channel<Effect>() }
    val effect: Flow<Effect> by lazy { _effect.receiveAsFlow() }

    init {
        fetchBreeds()
        subscribeToNetworkStatus()
    }

    private fun subscribeToNetworkStatus() {
        networkChecker.state
            .onEach {
                _stateFlow.update { state -> state.copy(isConnected = it) }
            }
            .launchIn(viewModelScope)
    }

    private fun fetchBreeds() {
        viewModelScope.launch {
            // switch ui to shimmer loading mode for breeds
            _stateFlow.update {
                it.copy(breedStateType = BreedStateType.Loading)
            }

            // simulate network delay to show proper loading
            delay(1000L)

            withContext(contextProvider.io) {
                getBreedsUseCase.execute()
            }.onSuccess { list ->
                if (list.isNotEmpty()) {
                    // we have breeds to show
                    _stateFlow.update { state ->
                        state.copy(
                            breedStateType = BreedStateType.Content(
                                breedList = list.map {
                                    Chip(name = it.name.capitalise())
                                }.toPersistentList()
                            ),
                        )
                    }
                } else {
                    // we don't have breed to show, no data
                    _stateFlow.update { state ->
                        state.copy(breedStateType = BreedStateType.NoData)
                    }
                }
            }.onFailure {
                // encountered an internal error while fetching breeds
                _stateFlow.update { state ->
                    state.copy(breedStateType = BreedStateType.Error)
                }
            }
        }
    }

    private fun fetchSubBreeds() {
        val selectedBreed = stateFlow.value.getBreedList()
            ?.firstOrNull { breed -> breed.selected }
            ?: return

        viewModelScope.launch {
            withContext(contextProvider.io) {
                getSubBreedsUseCase.execute(breed = selectedBreed.name.lowercase())
            }.onSuccess { list ->
                if (list.isNotEmpty()) {
                    // we have sub breeds to show
                    _stateFlow.update { state ->
                        state.copy(
                            isDetailButtonActive = false,
                            subBreedStateType = SubBreedStateType.Content(
                                subBreedList = list.map {
                                    Chip(name = it.name.capitalise())
                                }.toPersistentList()
                            ),
                        )
                    }
                } else {
                    // we don't have sub breed to show
                    _stateFlow.update { state ->
                        state.copy(
                            isDetailButtonActive = true,
                            subBreedStateType = SubBreedStateType.NoData,
                        )
                    }
                }
            }.onFailure {
                // encountered an internal error while fetching sub breeds
                _stateFlow.update { state ->
                    state.copy(
                        isDetailButtonActive = false,
                        subBreedStateType = SubBreedStateType.Error,
                    )
                }
            }
        }
    }

    fun selectBreed(name: String) {
        val breedList = stateFlow.value.getBreedList()?.map {
            it.copy(selected = it.name == name)
        } ?: return

        _stateFlow.update { state ->
            state.copy(
                breedStateType = BreedStateType.Content(
                    breedList = breedList.toPersistentList(),
                ),
            )
        }

        fetchSubBreeds()
    }

    fun selectSubBreed(name: String) {
        val subBreedList = stateFlow.value.getSubBreedList()?.map {
            it.copy(selected = it.name == name)
        } ?: return

        _stateFlow.update { state ->
            state.copy(
                isDetailButtonActive = true,
                subBreedStateType = SubBreedStateType.Content(
                    subBreedList = subBreedList.toPersistentList()
                ),
            )
        }
    }

    fun onDetailButtonClick() {
        val selectedBreedName =
            stateFlow.value.getBreedList()?.firstOrNull { it.selected }?.name ?: return
        val selectedSubBreedName =
            stateFlow.value.getSubBreedList()?.firstOrNull { it.selected }?.name

        viewModelScope.launch {
            _effect.send(
                Effect.NavigateToDetails(
                    breed = selectedBreedName.lowercase(),
                    subBreed = selectedSubBreedName?.lowercase(),
                )
            )
        }
    }
}
