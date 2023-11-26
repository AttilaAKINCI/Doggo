package com.akinci.doggo.domain.breed

import com.akinci.doggo.data.breed.BreedRepository
import com.akinci.doggo.data.breed.local.toDomain
import javax.inject.Inject

class BreedUseCase @Inject constructor(
    private val breedRepository: BreedRepository,
) {
    suspend fun getBreeds() = breedRepository.getBreeds().fold(
        onSuccess = {
            // save fetched breeds to local db.
            breedRepository.insert(it.toEntity())
            it
        },
        onFailure = {
            // we encountered an service error. Fallback to local data we have.
            breedRepository.getLocalBreeds().toDomain()
        }
    )
}