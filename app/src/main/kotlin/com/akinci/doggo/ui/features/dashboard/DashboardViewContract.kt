package com.akinci.doggo.ui.features.dashboard

import com.akinci.doggo.domain.data.Chip
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

object DashboardViewContract {

    data class State(
        val isConnected: Boolean = true,
        val isDetailButtonActive: Boolean = false,

        val breedStateType: BreedStateType = BreedStateType.Loading,
        val subBreedStateType: SubBreedStateType = SubBreedStateType.NoData,
    ) {
        fun getBreedList() = (breedStateType as? BreedStateType.Content)?.breedList
        fun getSubBreedList() = (subBreedStateType as? SubBreedStateType.Content)?.subBreedList
    }

    sealed interface Effect {
        data class NavigateToDetails(
            val breed: String,
            val subBreed: String?,
        ) : Effect
    }

    sealed interface BreedStateType {
        data object Loading : BreedStateType
        data object NoData : BreedStateType
        data object Error : BreedStateType
        data class Content(
            val breedList: PersistentList<Chip> = persistentListOf(),
        ) : BreedStateType
    }

    sealed interface SubBreedStateType {
        data object Error : SubBreedStateType
        data object NoData : SubBreedStateType
        data class Content(
            val subBreedList: PersistentList<Chip> = persistentListOf(),
        ) : SubBreedStateType
    }
}
