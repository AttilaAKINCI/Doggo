package com.akinci.doggo.data.repository

import com.akinci.doggo.core.network.toResponse
import com.akinci.doggo.data.mapper.toDomain
import com.akinci.doggo.data.rest.BreedListServiceResponse
import com.akinci.doggo.data.room.breed.BreedDao
import com.akinci.doggo.data.room.breed.BreedEntity
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import javax.inject.Inject

class BreedRepository @Inject constructor(
    private val breedDao: BreedDao,
    private val httpClient: HttpClient,
) {
    // region LOCAL
    suspend fun insert(items: List<BreedEntity>) = runCatching {
        breedDao.insertBreeds(items)
    }

    suspend fun getLocalBreeds() = runCatching {
        breedDao.getAllBreeds()
    }.map { it.toDomain() }
    // endregion

    // region REMOTE
    suspend fun getBreeds() = runCatching {
        httpClient
            .get("api/breeds/list/all")
            .toResponse<BreedListServiceResponse>()
            .getOrThrow()
    }.map { it.toDomain() }
    // endregion
}
