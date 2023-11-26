package com.akinci.doggo.data.subbreed.remote

import com.akinci.doggo.domain.subBreed.SubBreed
import kotlinx.serialization.Serializable

@Serializable
data class SubBreedListServiceResponse(
    val message: List<String>
)

fun SubBreedListServiceResponse.toDomain(
    breed: String,
) = message.map { name ->
    SubBreed(
        breed = breed,
        name = name,
    )
}

