package com.akinci.doggo.domain.breed

import android.os.Parcelable
import com.akinci.doggo.data.breed.local.BreedEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class Breed(
    val name: String,
) : Parcelable

private fun Breed.toEntity() = BreedEntity(name = name)

fun List<Breed>.toEntity() = map { it.toEntity() }
