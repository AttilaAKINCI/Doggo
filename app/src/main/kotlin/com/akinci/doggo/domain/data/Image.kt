package com.akinci.doggo.domain.data

import android.os.Parcelable
import com.akinci.doggo.domain.ImageListItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class Image(
    val breed: String,
    val subBreed: String?,
    val imageUrl: String
) : Parcelable

fun Image.toList(name: String) = ImageListItem(
    dogName = name,
    imageUrl = imageUrl,
)