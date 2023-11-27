package com.akinci.doggo.data.subbreed.remote

import com.akinci.doggo.data.subbreed.local.SubBreedEntity
import com.akinci.doggo.domain.subBreed.SubBreed
import kotlinx.serialization.Serializable

@Serializable
data class SubBreedListServiceResponse(
    val message: List<String>
)

fun SubBreedListServiceResponse.toDomain(breed: String) = message.map { name ->
    SubBreed(
        breed = breed,
        name = name,
    )
}

fun SubBreedListServiceResponse.toEntity(breed: String) = message.map { name ->
    SubBreedEntity(
        breed = breed,
        name = name,
    )
}
