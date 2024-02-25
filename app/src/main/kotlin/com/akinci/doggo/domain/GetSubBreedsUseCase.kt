package com.akinci.doggo.domain

import com.akinci.doggo.core.network.NetworkChecker
import com.akinci.doggo.data.mapper.toData
import com.akinci.doggo.data.repository.SubBreedRepository
import javax.inject.Inject

class GetSubBreedsUseCase @Inject constructor(
    private val subBreedRepository: SubBreedRepository,
    private val networkChecker: NetworkChecker,
) {
    suspend fun execute(breed: String) = if (networkChecker.isConnected()) {
        // we have valid network connection.
        // use remote sub breed data to show on ui
        subBreedRepository.getSubBreeds(breed)
            .onSuccess { subBreeds ->
                // update locally saved sub breeds with fresh fetched sub breeds
                subBreedRepository.insert(
                    subBreeds.map { it.toData() }
                )
            }
    } else {
        // we don't have proper network connection, serve local data
        subBreedRepository.getLocalSubBreeds(breed = breed)
    }
}
