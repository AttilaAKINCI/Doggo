package com.akinci.doggoapp.data.doggo.api

import com.akinci.doggoapp.common.network.RestConfig
import com.akinci.doggoapp.data.doggo.output.BreedResponse
import retrofit2.Response
import retrofit2.http.GET

interface DoggoServiceDao {

    @GET(RestConfig.BREED_LIST)
    suspend fun getBreeds() : Response<BreedResponse>

}