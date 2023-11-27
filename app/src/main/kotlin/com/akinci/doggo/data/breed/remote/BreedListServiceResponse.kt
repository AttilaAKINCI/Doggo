package com.akinci.doggo.data.breed.remote

import com.akinci.doggo.data.breed.local.BreedEntity
import com.akinci.doggo.domain.breed.Breed
import kotlinx.serialization.Serializable

@Serializable
data class BreedListServiceResponse(
    val message: Map<String, List<String>>,
)

fun BreedListServiceResponse.toDomain() = message.keys.map { breed ->
    Breed(name = breed)
}

fun BreedListServiceResponse.toEntity() = message.keys.map { breed ->
    BreedEntity(name = breed)
}