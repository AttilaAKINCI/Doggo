package com.akinci.doggoapp.feature.dashboard.data

import com.akinci.doggoapp.data.local.entity.BreedEntity
import com.akinci.doggoapp.data.local.entity.SubBreedEntity

data class Breed(
    val name: String,
    var selected: Boolean = false
)

fun List<Breed>.convertToBreedListEntity(): List<BreedEntity> {
    return map {
        BreedEntity(name = it.name, selected = it.selected)
    }
}

fun List<Breed>.convertToSubBreedListEntity(ownerBreed:String): List<SubBreedEntity> {
    return map {
        SubBreedEntity(breed = ownerBreed, name = it.name, selected = it.selected)
    }
}