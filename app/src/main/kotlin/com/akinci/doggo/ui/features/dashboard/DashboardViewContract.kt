package com.akinci.doggo.ui.features.dashboard

import com.akinci.doggo.core.compose.UIState
import com.akinci.doggo.domain.breed.Breed
import com.akinci.doggo.domain.subBreed.SubBreed
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

object DashboardViewContract {

    data class State(
        val isError: Boolean = false,
        val isNoData: Boolean = false,
        val isShimmerLoading: Boolean = false,
        val shimmerItemCount: Int = 1,
        val isDetailButtonActive: Boolean = true,

        val breedList: PersistentList<String> = persistentListOf(),
        val subBreedList: PersistentList<String> = persistentListOf(),
        val selectedBreed: String? = null,
        val selectedSubBreed: String? = null,
    ) : UIState
}
