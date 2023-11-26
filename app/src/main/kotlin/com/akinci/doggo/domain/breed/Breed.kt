package com.akinci.doggo.domain.breed

import android.os.Parcelable
import com.akinci.doggo.data.breed.local.BreedEntity
import com.akinci.doggo.domain.BreedListItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class Breed(
    val name: String,
) : Parcelable

private fun Breed.toEntity() = BreedEntity(name = name)
private fun Breed.toListItem() = BreedListItem(name = name)

fun List<Breed>.toEntity() = map { it.toEntity() }
fun List<Breed>.toListItem() = map { it.toListItem() }
