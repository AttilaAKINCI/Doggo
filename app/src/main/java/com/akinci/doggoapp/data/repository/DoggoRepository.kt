package com.akinci.doggoapp.data.repository

import com.akinci.doggoapp.common.helper.NetworkResponse
import com.akinci.doggoapp.common.network.NetworkChecker
import com.akinci.doggoapp.common.network.NetworkState
import com.akinci.doggoapp.common.repository.BaseRepository
import com.akinci.doggoapp.data.local.dao.BreedDao
import com.akinci.doggoapp.data.local.dao.ContentDao
import com.akinci.doggoapp.data.local.dao.SubBreedDao
import com.akinci.doggoapp.data.mapper.convertToBreedListResponse
import com.akinci.doggoapp.data.mapper.convertToBreedResponse
import com.akinci.doggoapp.data.mapper.convertToSubBreedListResponse
import com.akinci.doggoapp.data.remote.api.DoggoServiceDao
import com.akinci.doggoapp.data.remote.output.BreedListResponse
import com.akinci.doggoapp.data.remote.output.BreedResponse
import com.akinci.doggoapp.data.remote.output.SubBreedListResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DoggoRepository @Inject constructor(
    private val doggoServiceDao: DoggoServiceDao,
    private val breedDao: BreedDao,
    private val subBreedDao: SubBreedDao,
    private val contentDao: ContentDao,
    private val networkChecker: NetworkChecker,
    private val baseRepository: BaseRepository
) {

    /**
     * In this repository according network connectivity,
     * we gonna fetch our data using Network or Local Room Database
     * **/

    suspend fun getBreedList(): Flow<NetworkResponse<BreedListResponse>> =
        if(networkChecker.networkState.value == NetworkState.Connected) {
            baseRepository.callServiceAsFlow { doggoServiceDao.getBreedList() }
        }else{
            flow{ emit(NetworkResponse.Success(data = breedDao.getAllBreeds().convertToBreedListResponse())) }
        }

    suspend fun getSubBreedList(breed: String): Flow<NetworkResponse<SubBreedListResponse>> =
        if(networkChecker.networkState.value == NetworkState.Connected) {
            baseRepository.callServiceAsFlow { doggoServiceDao.getSubBreedList(breed) }
        }else{
            flow{ emit(NetworkResponse.Success(data = subBreedDao.getAllSubBreeds(breed).convertToSubBreedListResponse())) }
        }

    suspend fun getDoggoContent(breed: String, subBreed: String = "", count: Int): Flow<NetworkResponse<BreedResponse>> =
        if(networkChecker.networkState.value == NetworkState.Connected) {
            baseRepository.callServiceAsFlow {
                if(subBreed.isNotBlank()){
                    doggoServiceDao.getSubBreedContent(breed, subBreed, count)
                }else{
                    doggoServiceDao.getBreedContent(breed, count)
                }
            }
        }else{
            flow{
                emit(NetworkResponse.Loading())
                emit(NetworkResponse.Success(data = contentDao.getAllContents(breed, subBreed).convertToBreedResponse()))
            }
        }
}