package com.akinci.doggo.ui.features.detail

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

object DetailViewContract {

    data class State(
        val asd: String = ""
    )

    @Parcelize
    data class ScreenArgs(
        val breed: String,
        val subBreed: String?,
    ) : Parcelable
}
