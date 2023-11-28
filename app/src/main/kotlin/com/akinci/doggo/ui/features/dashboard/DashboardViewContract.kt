package com.akinci.doggo.ui.features.dashboard

import com.akinci.doggo.core.compose.UIState
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

object DashboardViewContract {

    data class State(
        val isConnected: Boolean = true,
        val isDetailButtonActive: Boolean = false,
        val isBreedLoading: Boolean = false,
        val isBreedNoData: Boolean = false,
        val isBreedError: Boolean = false,
        val isSubBreedError: Boolean = false,

        val breedList: PersistentList<BreedListItem> = persistentListOf(),
        val subBreedList: PersistentList<BreedListItem> = persistentListOf(),
        val selectedBreed: String? = null,
        val selectedSubBreed: String? = null,
    ) : UIState
}
