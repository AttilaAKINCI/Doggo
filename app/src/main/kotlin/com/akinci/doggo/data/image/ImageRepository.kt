package com.akinci.doggo.data.image

import com.akinci.doggo.core.network.toResponse
import com.akinci.doggo.core.storage.AppDatabase
import com.akinci.doggo.data.image.local.ImageEntity
import com.akinci.doggo.data.image.local.toDomain
import com.akinci.doggo.data.image.remote.ImageServiceResponse
import com.akinci.doggo.data.image.remote.toDomain
import com.akinci.doggo.domain.data.toEntity
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import javax.inject.Inject

class ImageRepository @Inject constructor(
    private val appDatabase: AppDatabase,
    private val httpClient: HttpClient,
) {
    private val imageDao by lazy { appDatabase.getImageDao() }

    // region LOCAL
    private suspend fun insert(items: List<ImageEntity>) = runCatching {
        imageDao.insertImages(items)
    }

    private suspend fun getLocalImages(
        breed: String,
        subBreed: String?,
    ) = if (!subBreed.isNullOrBlank()) {
        imageDao.getImages(breed = breed, subBreed = subBreed)
    } else {
        imageDao.getImages(breed = breed)
    }
    // endregion

    // region REMOTE
    suspend fun getImage(
        breed: String,
        subBreed: String? = null,
        count: Int = 100,
    ) = runCatching {
        val imagesServiceURL = if (!subBreed.isNullOrBlank()) {
            "api/breed/$breed/images/random/$count"
        } else {
            "api/breed/$breed/$subBreed/images/random/$count"
        }

        httpClient
            .get(imagesServiceURL)
            .toResponse<ImageServiceResponse>()
            .map {
                it.toDomain(breed = breed, subBreed = subBreed)
            }
            .fold(
                onSuccess = {
                    // save fetched images to local db.
                    insert(it.toEntity())
                    it
                },
                onFailure = {
                    // we encountered an service error. Fallback to local data we have.
                    getLocalImages(breed = breed, subBreed = subBreed).toDomain()
                }
            )
    }
    // endregion
}
