package com.akinci.doggo.data.image

import com.akinci.doggo.core.network.toResponse
import com.akinci.doggo.core.storage.AppDatabase
import com.akinci.doggo.data.image.local.ImageEntity
import com.akinci.doggo.data.image.remote.ImageServiceResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import javax.inject.Inject

class ImageRepository @Inject constructor(
    private val appDatabase: AppDatabase,
    private val httpClient: HttpClient,
) {
    private val imageDao by lazy { appDatabase.getImageDao() }

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
    }
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
    }
    // endregion
}
