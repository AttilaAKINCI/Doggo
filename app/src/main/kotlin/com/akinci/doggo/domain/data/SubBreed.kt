package com.akinci.doggo.domain.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SubBreed(
    val breed: String,
    val name: String,
    val selected: Boolean = false,
) : Parcelable
