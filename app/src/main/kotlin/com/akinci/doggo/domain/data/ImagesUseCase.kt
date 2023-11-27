package com.akinci.doggo.domain.data

import com.akinci.doggo.core.network.NetworkChecker
import com.akinci.doggo.data.image.ImageRepository
import com.akinci.doggo.data.image.local.toDomain
import com.akinci.doggo.data.image.remote.toDomain
import com.akinci.doggo.data.image.remote.toEntity
import javax.inject.Inject

class ImagesUseCase @Inject constructor(
    private val imageRepository: ImageRepository,
    private val networkChecker: NetworkChecker,
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
                .map { it.toDomain(breed = breed, subBreed = subBreed) }
        } else {
            imageRepository.getLocalImages(breed = breed, subBreed = subBreed)
                .map { it.toDomain() }
        }
}
