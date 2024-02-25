package com.akinci.doggo.domain

import com.akinci.doggo.core.network.NetworkChecker
import com.akinci.doggo.data.mapper.toData
import com.akinci.doggo.data.repository.ImageRepository
import javax.inject.Inject

class GetImagesUseCase @Inject constructor(
    private val imageRepository: ImageRepository,
    private val networkChecker: NetworkChecker,
    private val dogNameProvider: DogNameProvider,
) {
    suspend fun execute(breed: String, subBreed: String? = null) =
        if (networkChecker.isConnected()) {
            // we have valid network connection.
            // use remote image data to show on ui
            imageRepository.getImage(breed = breed, subBreed = subBreed)
                .onSuccess { images ->
                    // update locally images with fresh images
                    imageRepository.insert(
                        images.map { it.toData() }
                    )
                }
                .map { images ->
                    // insert random dog name to show on ui
                    images.map {
                        it.copy(dogName = dogNameProvider.getRandomDogName())
                    }
                }
        } else {
            imageRepository.getLocalImages(breed = breed, subBreed = subBreed)
                .map { images ->
                    // insert random dog name to show on ui
                    images.map {
                        it.copy(dogName = dogNameProvider.getRandomDogName())
                    }
                }
        }
}
