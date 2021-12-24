package com.akinci.doggoapp.data.remote.api

import com.akinci.doggoapp.common.network.RestConfig
import com.akinci.doggoapp.data.remote.output.BreedListResponse
import com.akinci.doggoapp.data.remote.output.BreedResponse
import com.akinci.doggoapp.data.remote.output.SubBreedListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface DoggoServiceDao {

    @GET(RestConfig.BREED_LIST)
    suspend fun getBreedList() : Response<BreedListResponse>

    @GET(RestConfig.SUB_BREED_LIST)
    suspend fun getSubBreedList(@Path("breed") breed: String) : Response<SubBreedListResponse>

    @GET(RestConfig.GET_BREED)
    suspend fun getBreeds(
        @Path("breed") breed: String,
        @Path("count") count: Int
    ) : Response<BreedResponse>

    @GET(RestConfig.GET_SUB_BREED)
    suspend fun getSubBreeds(
        @Path("breed") breed: String,
        @Path("subBread") subBread: String,
        @Path("count") count: Int
    ) : Response<BreedResponse>

}