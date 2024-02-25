package com.akinci.doggo.domain

import com.akinci.doggo.core.network.NetworkChecker
import com.akinci.doggo.data.mapper.toData
import com.akinci.doggo.data.repository.BreedRepository
import javax.inject.Inject

class GetBreedsUseCase @Inject constructor(
    private val breedRepository: BreedRepository,
    private val networkChecker: NetworkChecker,
) {
    suspend fun execute() = if (networkChecker.isConnected()) {
        // we have valid network connection.
        // use remote breed data to show on ui
        breedRepository.getBreeds()
            .onSuccess { breeds ->
                // update locally saved breeds with fresh fetched breeds
                breedRepository.insert(
                    breeds.map { it.toData() }
                )
            }
    } else {
        // we don't have proper network connection, serve local data
        breedRepository.getLocalBreeds()
    }
}
