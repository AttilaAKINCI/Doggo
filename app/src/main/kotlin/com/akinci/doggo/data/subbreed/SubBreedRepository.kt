package com.akinci.doggo.data.subbreed

import com.akinci.doggo.core.network.toResponse
import com.akinci.doggo.core.storage.AppDatabase
import com.akinci.doggo.data.subbreed.local.SubBreedEntity
import com.akinci.doggo.data.subbreed.remote.SubBreedListServiceResponse
import com.akinci.doggo.data.subbreed.remote.toDomain
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import javax.inject.Inject

class SubBreedRepository @Inject constructor(
    private val appDatabase: AppDatabase,
    private val httpClient: HttpClient,
) {
    private val subBreedDao by lazy { appDatabase.getSubBreedDao() }

    // region LOCAL
    suspend fun insert(items: List<SubBreedEntity>) = runCatching {
        subBreedDao.insertSubBreeds(items)
    }

    suspend fun getLocalSubBreeds(breed: String) =
        runCatching { subBreedDao.getAllSubBreeds(breed = breed) }.getOrDefault(listOf())
    // endregion

    // region REMOTE
    suspend fun getSubBreeds(breed: String) = runCatching {
        httpClient
            .get("api/breed/$breed/list")
            .toResponse<SubBreedListServiceResponse>()
            .map { it.toDomain(breed = breed) }
            .getOrThrow()
    }
    // endregion
}
