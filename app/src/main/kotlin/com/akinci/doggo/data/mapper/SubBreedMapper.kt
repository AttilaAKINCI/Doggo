package com.akinci.doggo.data.mapper

import com.akinci.doggo.data.rest.SubBreedListServiceResponse
import com.akinci.doggo.data.room.subbreed.SubBreedEntity
import com.akinci.doggo.domain.data.SubBreed

fun List<SubBreedEntity>.toDomain() = map { SubBreed(breed = it.breed, name = it.name) }

fun SubBreed.toData() = SubBreedEntity(breed = breed, name = name)

fun SubBreedListServiceResponse.toDomain(breed: String) = message.map { name ->
    SubBreed(
        breed = breed,
        name = name,
    )
}

fun SubBreedListServiceResponse.toData(breed: String) = message.map { name ->
    SubBreedEntity(
        breed = breed,
        name = name,
    )
}