package com.akinci.doggo.domain.subBreed

import android.os.Parcelable
import com.akinci.doggo.data.subbreed.local.SubBreedEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class SubBreed(
    val breed: String,
    val name: String,
) : Parcelable

private fun SubBreed.toEntity() = SubBreedEntity(
    breed = breed,
    name = name,
)

fun List<SubBreed>.toEntity() = map { it.toEntity() }
