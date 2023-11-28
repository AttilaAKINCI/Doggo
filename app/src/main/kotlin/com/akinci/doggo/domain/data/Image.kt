package com.akinci.doggo.domain.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Image(
    val breed: String,
    val subBreed: String?,
    val dogName: String,
    val imageUrl: String
) : Parcelable