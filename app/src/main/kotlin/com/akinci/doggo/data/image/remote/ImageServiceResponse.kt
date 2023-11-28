package com.akinci.doggo.data.image.remote

import com.akinci.doggo.data.image.local.ImageEntity
import com.akinci.doggo.domain.data.Image
import kotlinx.serialization.Serializable

@Serializable
data class ImageServiceResponse(
    val message: List<String>
)

fun ImageServiceResponse.toEntity(breed: String, subBreed: String?) = message.map { url ->
    ImageEntity(
        breed = breed,
        subBreed = subBreed,
        imageUrl = url,
    )
}