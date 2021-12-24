package com.akinci.doggoapp.data.repository

import com.akinci.doggoapp.common.helper.NetworkResponse
import com.akinci.doggoapp.common.network.NetworkChecker
import com.akinci.doggoapp.common.repository.BaseRepository
import com.akinci.doggoapp.data.local.dao.BreedDao
import com.akinci.doggoapp.data.local.dao.ContentDao
import com.akinci.doggoapp.data.local.dao.SubBreedDao
import com.akinci.doggoapp.data.remote.api.DoggoServiceDao
import com.akinci.doggoapp.data.remote.output.BreedListResponse
import com.akinci.doggoapp.data.remote.output.BreedResponse
import com.akinci.doggoapp.data.remote.output.SubBreedListResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DoggoRepository @Inject constructor(
    private val doggoServiceDao: DoggoServiceDao,
    private val breedDao: BreedDao,
    private val subBreedDao: SubBreedDao,
    private val contentDao: ContentDao,
    private val networkChecker: NetworkChecker,
    private val baseRepository: BaseRepository
) {

    suspend fun getBreedList(): Flow<NetworkResponse<BreedListResponse>> =
        baseRepository.callServiceAsFlow { doggoServiceDao.getBreedList() }

    suspend fun getSubBreedList(breed: String): Flow<NetworkResponse<SubBreedListResponse>> =
        baseRepository.callServiceAsFlow { doggoServiceDao.getSubBreedList(breed) }

    suspend fun getContent(breed: String, subBreed: String = "", count: Int = (10..30).random()): Flow<NetworkResponse<BreedResponse>> =
        baseRepository.callServiceAsFlow {
            if(subBreed.isNotBlank()){
                doggoServiceDao.getSubBreedContent(breed, subBreed, count)
            }else{
                doggoServiceDao.getBreedContent(breed, count)
            }
        }

}