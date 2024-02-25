package com.akinci.doggo.data.mapper

import com.akinci.doggo.data.rest.BreedListServiceResponse
import com.akinci.doggo.data.room.breed.BreedEntity
import com.akinci.doggo.domain.data.Breed

fun List<BreedEntity>.toDomain() = map { Breed(name = it.name) }

fun Breed.toData() = BreedEntity(name = name)

fun BreedListServiceResponse.toDomain() = message.keys.map { breed ->
    Breed(name = breed)
}