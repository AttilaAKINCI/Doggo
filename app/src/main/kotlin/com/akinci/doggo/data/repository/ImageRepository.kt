package com.akinci.doggo.data.repository

import com.akinci.doggo.core.network.toResponse
import com.akinci.doggo.data.mapper.toDomain
import com.akinci.doggo.data.rest.ImageServiceResponse
import com.akinci.doggo.data.room.image.ImageDao
import com.akinci.doggo.data.room.image.ImageEntity
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import javax.inject.Inject

class ImageRepository @Inject constructor(
    private val imageDao: ImageDao,
    private val httpClient: HttpClient,
) {
    // region LOCAL
    suspend fun insert(items: List<ImageEntity>) = runCatching {
        imageDao.insertImages(items)
    }

    suspend fun getLocalImages(breed: String, subBreed: String?) = runCatching {
        if (subBreed.isNullOrBlank()) {
            imageDao.getImages(breed = breed)
        } else {
            imageDao.getImages(breed = breed, subBreed = subBreed)
        }
    }.map { it.toDomain() }
    // endregion

    // region REMOTE
    suspend fun getImage(breed: String, subBreed: String? = null, count: Int = 100) = runCatching {
        val imagesServiceURL = if (subBreed.isNullOrBlank()) {
            "api/breed/$breed/images/random/$count"
        } else {
            "api/breed/$breed/$subBreed/images/random/$count"
        }

        httpClient
            .get(imagesServiceURL)
            .toResponse<ImageServiceResponse>()
            .getOrThrow()
    }.map { it.toDomain(breed = breed, subBreed = subBreed) }
    // endregion
}
