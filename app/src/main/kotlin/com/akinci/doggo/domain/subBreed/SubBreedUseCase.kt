package com.akinci.doggo.domain.subBreed

import com.akinci.doggo.data.subbreed.SubBreedRepository
import com.akinci.doggo.data.subbreed.local.toDomain
import javax.inject.Inject

class SubBreedUseCase @Inject constructor(
    private val subBreedRepository: SubBreedRepository,
) {
    suspend fun getSubBreeds(breed: String) = subBreedRepository.getSubBreeds(breed)
        .onSuccess {
            // save fetched subBreeds to local db.
            subBreedRepository.insert(it.toEntity())
        }.fold(
            onSuccess = { it },
            onFailure = {
                // we encountered an service error. Fallback to local data we have.
                subBreedRepository.getLocalSubBreeds(breed = breed).toDomain()
            }
        )
}
