package com.akinci.doggo.domain.data

import android.os.Parcelable
import com.akinci.doggo.data.image.local.ImageEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class Image(
    val breed: String,
    val subBreed: String?,
    val imageUrl: String
) : Parcelable

private fun Image.toEntity() = ImageEntity(
    breed = breed,
    subBreed = subBreed,
    imageUrl = imageUrl,
)

fun List<Image>.toEntity() = map { it.toEntity() }