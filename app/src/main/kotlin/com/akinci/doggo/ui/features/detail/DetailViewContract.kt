package com.akinci.doggo.ui.features.detail

import android.os.Parcelable
import com.akinci.doggo.core.compose.UIState
import com.akinci.doggo.domain.data.Image
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.parcelize.Parcelize

object DetailViewContract {

    data class State(
        val isLoading: Boolean = false,

        val title: String,
        val images: PersistentList<Image> = persistentListOf(),
    ) : UIState

    @Parcelize
    data class ScreenArgs(
        val breed: String,
        val subBreed: String?,
    ) : Parcelable
}
