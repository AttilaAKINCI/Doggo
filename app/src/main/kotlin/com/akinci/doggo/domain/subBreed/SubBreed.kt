package com.akinci.doggo.domain.subBreed

import android.os.Parcelable
import com.akinci.doggo.ui.features.dashboard.BreedListItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class SubBreed(
    val breed: String,
    val name: String,
) : Parcelable

private fun SubBreed.toListItem() = BreedListItem(name = name)

fun List<SubBreed>.toListItem() = map { it.toListItem() }
