package com.akinci.doggo.domain.breed

import android.os.Parcelable
import com.akinci.doggo.ui.features.dashboard.BreedListItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class Breed(
    val name: String,
) : Parcelable

private fun Breed.toListItem() = BreedListItem(name = name)

fun List<Breed>.toListItem() = map { it.toListItem() }
