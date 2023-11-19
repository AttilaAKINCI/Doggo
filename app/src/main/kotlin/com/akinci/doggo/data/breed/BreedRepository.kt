package com.akinci.doggo.data.breed

import com.akinci.doggo.core.network.toResponse
import com.akinci.doggo.core.storage.AppDatabase
import com.akinci.doggo.data.breed.local.BreedEntity
import com.akinci.doggo.data.breed.local.toDomain
import com.akinci.doggo.data.breed.remote.BreedListServiceResponse
import com.akinci.doggo.data.breed.remote.toDomain
import com.akinci.doggo.domain.data.toEntity
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import javax.inject.Inject

class BreedRepository @Inject constructor(
    private val appDatabase: AppDatabase,
    private val httpClient: HttpClient,
) {
    private val breedDao by lazy { appDatabase.getBreedDao() }

    // region LOCAL
    private suspend fun insert(items: List<BreedEntity>) = runCatching {
        breedDao.insertBreeds(items)
    }

    private suspend fun getLocalBreeds() = breedDao.getAllBreeds()
    // endregion

    // region REMOTE
    suspend fun getBreeds() = runCatching {
        httpClient
            .get("api/breeds/list/all")
            .toResponse<BreedListServiceResponse>()
            .map { it.toDomain() }
            .fold(
                onSuccess = {
                    // save fetched breeds to local db.
                    insert(it.toEntity())
                    it
                },
                onFailure = {
                    // we encountered an service error. Fallback to local data we have.
                    getLocalBreeds().toDomain()
                }
            )
    }
    // endregion
}
