package com.akinci.doggo.domain.data

import com.akinci.doggo.core.network.NetworkChecker
import com.akinci.doggo.data.image.ImageRepository
import com.akinci.doggo.data.image.remote.toEntity
import com.akinci.doggo.domain.DogNameProvider
import javax.inject.Inject

class ImagesUseCase @Inject constructor(
    private val imageRepository: ImageRepository,
    private val networkChecker: NetworkChecker,
    private val dogNameProvider: DogNameProvider,
) {
    suspend fun getImage(breed: String, subBreed: String? = null) =
        if (networkChecker.isConnected()) {
            imageRepository.getImage(breed = breed, subBreed = subBreed)
                .onSuccess {
                    // update images with recently fetched ones
                    imageRepository.insert(
                        it.toEntity(breed = breed, subBreed = subBreed)
                    )
                }
                .map { response ->
                    response.message.map {
                        Image(
                            breed = breed,
                            subBreed = subBreed,
                            imageUrl = it,
                            dogName = dogNameProvider.getRandomDogName(),
                        )
                    }
                }
        } else {
            imageRepository
                .getLocalImages(breed = breed, subBreed = subBreed)
                .map { entities ->
                    entities.map {
                        Image(
                            breed = it.breed,
                            subBreed = it.subBreed,
                            imageUrl = it.imageUrl,
                            dogName = dogNameProvider.getRandomDogName(),
                        )
                    }
                }
        }
}
