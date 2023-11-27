package com.akinci.doggo.domain.subBreed

import com.akinci.doggo.core.network.NetworkChecker
import com.akinci.doggo.data.subbreed.SubBreedRepository
import com.akinci.doggo.data.subbreed.local.toDomain
import com.akinci.doggo.data.subbreed.remote.toDomain
import com.akinci.doggo.data.subbreed.remote.toEntity
import javax.inject.Inject

class SubBreedUseCase @Inject constructor(
    private val subBreedRepository: SubBreedRepository,
    private val networkChecker: NetworkChecker,
) {
    suspend fun getSubBreeds(breed: String) = if (networkChecker.isConnected()) {
        // we are connected, ask for remote data
        subBreedRepository.getSubBreeds(breed)
            .onSuccess {
                // save fetched subBreeds to local db.
                subBreedRepository.insert(it.toEntity(breed = breed))
            }
            .map { it.toDomain(breed = breed) }
    } else {
        // we don't have proper network connection, serve local data
        subBreedRepository.getLocalSubBreeds(breed = breed)
            .map { it.toDomain() }
    }
}
