package com.akinci.doggo.ui.features.dashboard

import com.akinci.doggo.domain.data.Breed
import com.akinci.doggo.domain.data.SubBreed
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

object DashboardViewContract {

    data class State(
        val isDetailButtonActive: Boolean = false,
        val selectedBreed: String? = null,
        val selectedSubBreed: String? = null,
        val breedList: PersistentList<Breed> = persistentListOf(),
        val subBreedList: PersistentList<SubBreed> = persistentListOf()
    )
}
