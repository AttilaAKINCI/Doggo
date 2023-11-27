package com.akinci.doggo.domain.breed

import com.akinci.doggo.core.network.NetworkChecker
import com.akinci.doggo.data.breed.BreedRepository
import com.akinci.doggo.data.breed.local.toDomain
import com.akinci.doggo.data.breed.remote.toDomain
import com.akinci.doggo.data.breed.remote.toEntity
import javax.inject.Inject

class BreedUseCase @Inject constructor(
    private val breedRepository: BreedRepository,
    private val networkChecker: NetworkChecker,
) {
    suspend fun getBreeds() = if (networkChecker.isConnected()) {
        // we are connected, ask for remote data
        breedRepository.getBreeds()
            .onSuccess {
                // update breeds with recently fetched breeds
                breedRepository.insert(it.toEntity())
            }
            .map { it.toDomain() }
    } else {
        // we don't have proper network connection, serve local data
        breedRepository.getLocalBreeds()
            .map { it.toDomain() }
    }
}
