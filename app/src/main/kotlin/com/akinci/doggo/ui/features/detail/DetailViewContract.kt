package com.akinci.doggo.ui.features.detail

import android.os.Parcelable
import com.akinci.doggo.domain.data.Image
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.parcelize.Parcelize

object DetailViewContract {

    @Parcelize
    data class ScreenArgs(
        val breed: String,
        val subBreed: String?,
    ) : Parcelable

    data class State(
        val title: String = "",
        val type: Type = Type.Loading,
    )

    sealed interface Type {
        data object Loading : Type
        data object NoData : Type
        data object Error : Type
        data class Content(
            val images: PersistentList<Image> = persistentListOf()
        ) : Type
    }
}
