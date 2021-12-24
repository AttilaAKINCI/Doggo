package com.akinci.doggoapp.data.repository

import com.akinci.doggoapp.common.helper.NetworkResponse
import com.akinci.doggoapp.common.repository.BaseRepository
import com.akinci.doggoapp.data.remote.api.DoggoServiceDao
import com.akinci.doggoapp.data.remote.output.BreedListResponse
import com.akinci.doggoapp.data.remote.output.BreedResponse
import com.akinci.doggoapp.data.remote.output.SubBreedListResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DoggoRepository @Inject constructor(
    private val doggoServiceDao: DoggoServiceDao,
    private val baseRepository: BaseRepository
) {

    suspend fun getBreedList(): Flow<NetworkResponse<BreedListResponse>> =
        baseRepository.callServiceAsFlow { doggoServiceDao.getBreedList() }

    suspend fun getSubBreedList(breed: String): Flow<NetworkResponse<SubBreedListResponse>> =
        baseRepository.callServiceAsFlow { doggoServiceDao.getSubBreedList(breed) }

    suspend fun getBreeds(breed: String, count: Int = (10..30).random()): Flow<NetworkResponse<BreedResponse>> =
        baseRepository.callServiceAsFlow { doggoServiceDao.getBreeds(breed, count) }

    suspend fun getSubBreeds(breed: String, subBreed: String, count: Int = (10..30).random()): Flow<NetworkResponse<BreedResponse>> =
        baseRepository.callServiceAsFlow { doggoServiceDao.getSubBreeds(breed, subBreed, count) }

}