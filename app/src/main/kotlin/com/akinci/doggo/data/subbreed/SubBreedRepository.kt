package com.akinci.doggo.data.subbreed

import com.akinci.doggo.core.network.toResponse
import com.akinci.doggo.core.storage.AppDatabase
import com.akinci.doggo.data.subbreed.local.SubBreedEntity
import com.akinci.doggo.data.subbreed.local.toDomain
import com.akinci.doggo.data.subbreed.remote.SubBreedListServiceResponse
import com.akinci.doggo.data.subbreed.remote.toDomain
import com.akinci.doggo.domain.subBreed.toEntity
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import javax.inject.Inject

class SubBreedRepository @Inject constructor(
    private val appDatabase: AppDatabase,
    private val httpClient: HttpClient,
) {
    private val subBreedDao by lazy { appDatabase.getSubBreedDao() }

    // region LOCAL
    private suspend fun insert(items: List<SubBreedEntity>) = runCatching {
        subBreedDao.insertSubBreeds(items)
    }

    private suspend fun getLocalSubBreeds(breed: String) =
        subBreedDao.getAllSubBreeds(breed = breed)
    // endregion

    // region REMOTE
    suspend fun getSubBreeds(breed: String) = runCatching {
        httpClient
            .get("api/breeds/$breed/list")
            .toResponse<SubBreedListServiceResponse>()
            .map {
                it.toDomain(breed = breed)
            }.fold(
                onSuccess = {
                    // save fetched subBreeds to local db.
                    insert(it.toEntity())
                    it
                },
                onFailure = {
                    // we encountered an service error. Fallback to local data we have.
                    getLocalSubBreeds(breed = breed).toDomain()
                }
            )
    }
    // endregion
}
