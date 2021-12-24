package com.akinci.doggoapp.common.repository

import com.akinci.doggoapp.common.helper.NetworkResponse
import com.akinci.doggoapp.common.network.NetworkChecker
import com.akinci.doggoapp.common.network.NetworkState
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

class BaseRepository @Inject constructor(
    private val networkChecker: NetworkChecker
) {
    /** Service generic network checker **/

    // CallService generify for JSON Responses
    suspend fun <T> callServiceAsFlow(
            retrofitServiceAction : suspend () -> Response<T>
    ) = flow<NetworkResponse<T>> {
        // emit loading
        emit(NetworkResponse.Loading())

        // check internet connection
        if (networkChecker.networkState.value == NetworkState.Connected) {
            // internet connection is established.
            // invoke service generic part.
            val response = retrofitServiceAction.invoke()
            if (response.isSuccessful) {
                /** 200 -> 299 Error status range **/
                response.body()?.let {
                    // successful response.
                    emit(NetworkResponse.Success(data = it))
                } ?: run {
                    emit(NetworkResponse.Error(message = "BaseRepository: Service response body is null"))
                }
            }else{
                /** 400 -> 599 HTTP Error Status Range **/
                emit(NetworkResponse.Error(message = "BaseRepository: Service response failed with code: ${response.code()}"))
            }
        }else{
            // not connected to internet
            emit(NetworkResponse.Error(message = "BaseRepository: Couldn't reached to server. Please check your internet connection"))
        }
    }.catch { e ->
        Timber.d(e)
        emit(NetworkResponse.Error(message = "BaseRepository: UnExpected Service Exception"))
    }
}