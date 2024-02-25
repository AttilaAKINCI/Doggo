package com.akinci.doggo.data.repository

import com.akinci.doggo.core.network.toResponse
import com.akinci.doggo.data.mapper.toDomain
import com.akinci.doggo.data.rest.SubBreedListServiceResponse
import com.akinci.doggo.data.room.subbreed.SubBreedDao
import com.akinci.doggo.data.room.subbreed.SubBreedEntity
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import javax.inject.Inject

class SubBreedRepository @Inject constructor(
    private val subBreedDao: SubBreedDao,
    private val httpClient: HttpClient,
) {
    // region LOCAL
    suspend fun insert(items: List<SubBreedEntity>) = runCatching {
        subBreedDao.insertSubBreeds(items)
    }

    suspend fun getLocalSubBreeds(breed: String) = runCatching {
        subBreedDao.getAllSubBreeds(breed = breed)
    }.map { it.toDomain() }
    // endregion

    // region REMOTE
    suspend fun getSubBreeds(breed: String) = runCatching {
        httpClient
            .get("api/breed/$breed/list")
            .toResponse<SubBreedListServiceResponse>()
            .getOrThrow()
    }.map { it.toDomain(breed = breed) }
    // endregion
}
