package com.akinci.doggo.data.image.remote

import com.akinci.doggo.domain.data.Image
import kotlinx.serialization.Serializable

@Serializable
data class ImageServiceResponse(
    val message: List<String>
)

fun ImageServiceResponse.toDomain(
    breed: String,
    subBreed: String?,
) = message.map { url ->
    Image(
        breed = breed,
        subBreed = subBreed,
        imageUrl = url,
    )
}
