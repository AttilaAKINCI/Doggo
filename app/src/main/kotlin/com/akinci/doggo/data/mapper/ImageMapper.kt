package com.akinci.doggo.data.mapper

import com.akinci.doggo.data.rest.ImageServiceResponse
import com.akinci.doggo.data.room.image.ImageEntity
import com.akinci.doggo.domain.data.Image

fun List<ImageEntity>.toDomain() = map {
    Image(
        breed = it.breed,
        subBreed = it.subBreed,
        imageUrl = it.imageUrl,
    )
}

fun ImageServiceResponse.toDomain(breed: String, subBreed: String?) = message.map { url ->
    Image(
        breed = breed,
        subBreed = subBreed,
        imageUrl = url,
    )
}

fun Image.toData() = ImageEntity(
    breed = breed,
    subBreed = subBreed,
    imageUrl = imageUrl,
)