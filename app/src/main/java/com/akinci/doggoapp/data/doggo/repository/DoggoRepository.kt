package com.akinci.doggoapp.data.doggo.repository

import com.akinci.doggoapp.common.helper.NetworkResponse
import com.akinci.doggoapp.common.repository.BaseRepository
import com.akinci.doggoapp.data.doggo.api.DoggoServiceDao
import com.akinci.doggoapp.data.doggo.output.BreedResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DoggoRepository @Inject constructor(
    private val doggoServiceDao: DoggoServiceDao,
    private val baseRepository: BaseRepository
) {

    suspend fun getBreeds(): Flow<NetworkResponse<BreedResponse>> =
        baseRepository.callServiceAsFlow { doggoServiceDao.getBreeds() }

}