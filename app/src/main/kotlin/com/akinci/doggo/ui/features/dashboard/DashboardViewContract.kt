package com.akinci.doggo.ui.features.dashboard

import com.akinci.doggo.core.compose.UIState
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

object DashboardViewContract {

    data class State(
        val isNoData: Boolean = false,
        val isLoading: Boolean = false,
        val isConnected:Boolean = true,
        val isDetailButtonActive: Boolean = false,

        val breedList: PersistentList<BreedListItem> = persistentListOf(),
        val subBreedList: PersistentList<BreedListItem> = persistentListOf(),
        val selectedBreed: String? = null,
        val selectedSubBreed: String? = null,
    ) : UIState
}
