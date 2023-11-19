package com.akinci.doggo.data.breed.remote

import com.akinci.doggo.domain.data.Breed
import kotlinx.serialization.Serializable

@Serializable
data class BreedListServiceResponse(
    val message: Map<String, List<String>>,
)

fun BreedListServiceResponse.toDomain() = message.keys.map { breed ->
    Breed(name = breed)
}