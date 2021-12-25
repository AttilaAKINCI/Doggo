package com.akinci.doggoapp.common.repository

import com.akinci.doggoapp.common.helper.NetworkResponse
import com.akinci.doggoapp.common.network.NetworkChecker
import com.akinci.doggoapp.common.network.NetworkState
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
class BaseRepositoryTest {

    @MockK
    lateinit var networkChecker: NetworkChecker

    private lateinit var repository : BaseRepository

    @MockK
    lateinit var callBackObj : SuspendingCallBack

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        repository = BaseRepository(networkChecker)
    }

    @AfterEach
    fun tearDown() { unmockkAll() }

    class SuspendingCallBack {
        fun serviceActionCallBack() : Response<String> { return Response.success("Rest Call Succeeded") }
    }

    /**
     *  BaseRepositoryImpl class holds base logics of repository layer.
     *  in this class there is callServiceAsFlow function which handles network request responses.
     *
     *  callServiceAsFlow with only retrofitServiceAction directly sends retrofit service response object
     *
     * **/

    @Test
    fun `Network is ok, callServiceAsFlow function is called, returns NetworkResponse-Success for success`() = runBlockingTest {
        every { networkChecker.networkState.value } returns NetworkState.Connected
        coEvery { callBackObj.serviceActionCallBack() } returns Response.success("Rest Call Succeeded")

        val flowResponse = repository.callServiceAsFlow { callBackObj.serviceActionCallBack() }

        launch {
            flowResponse.collect { response ->
                /** ignore first loading state **/
                if(response is NetworkResponse.Loading){ return@collect }

                /** repository function response type should be NetworkResponse.Success **/
                assertThat(response).isInstanceOf(NetworkResponse.Success::class.java)

                /** returned error should be network error message **/
                assertThat((response as NetworkResponse.Success).data).isEqualTo("Rest Call Succeeded")

                /** call back should be fired. **/
                coVerify (exactly = 1) { callBackObj.serviceActionCallBack() }
                confirmVerified(callBackObj)
            }
        }
    }

    @Test
    fun `Network is ok, callServiceAsFlow function is called, returns NetworkResponse-Error on null response`() = runBlockingTest {
        every { networkChecker.networkState.value } returns NetworkState.Connected
        coEvery { callBackObj.serviceActionCallBack() } returns Response.success(null)

        val flowResponse = repository.callServiceAsFlow { callBackObj.serviceActionCallBack() }

        launch {
            flowResponse.collect { response ->
                /** ignore first loading state **/
                if(response is NetworkResponse.Loading){ return@collect }

                /** repository function response type should be NetworkResponse.Error **/
                assertThat(response).isInstanceOf(NetworkResponse.Error::class.java)
                /** returned error should be response body is null **/
                assertThat((response as NetworkResponse.Error).message).isEqualTo("BaseRepository: Service response body is null")

                /** call back should be fired. **/
                coVerify (exactly = 1) { callBackObj.serviceActionCallBack() }
                confirmVerified(callBackObj)
            }
        }
    }

    @Test
    fun `Network is ok, callServiceAsFlow function is called, returns NetworkResponse-Error on unsuccessful response code`() = runBlockingTest {
        every { networkChecker.networkState.value } returns NetworkState.Connected
        coEvery { callBackObj.serviceActionCallBack() } returns Response.error(404, "{\"error\":[\"404 Not Found\"]}"
            .toResponseBody("application/json".toMediaTypeOrNull()))

        val flowResponse = repository.callServiceAsFlow { callBackObj.serviceActionCallBack() }

        launch {
            flowResponse.collect { response ->
                /** ignore first loading state **/
                if(response is NetworkResponse.Loading){ return@collect }

                /** repository function response type should be NetworkResponse.Error **/
                assertThat(response).isInstanceOf(NetworkResponse.Error::class.java)
                /** returned error should be contains response code of error **/
                assertThat((response as NetworkResponse.Error).message).contains(": 404")

                /** call back should be fired. **/
                coVerify (exactly = 1) { callBackObj.serviceActionCallBack() }
                confirmVerified(callBackObj)
            }
        }
    }

    @Test
    fun `Network is not ok, callServiceAsFlow function is called, returns NetworkResponse-Error for network error`() = runBlockingTest {
        /** Network is not connected. **/
        every { networkChecker.networkState.value } returns NetworkState.NotConnected
        coEvery { callBackObj.serviceActionCallBack() } returns Response.success("Rest Call Succeeded")

        val flowResponse = repository.callServiceAsFlow { callBackObj.serviceActionCallBack() }

        launch {
            flowResponse.collect { response ->
                /** ignore first loading state **/
                if(response is NetworkResponse.Loading){ return@collect }

                /** repository function response type should be NetworkResponse.Error **/
                assertThat(response).isInstanceOf(NetworkResponse.Error::class.java)
                /** returned error should be network error message **/
                assertThat((response as NetworkResponse.Error).message).isEqualTo("BaseRepository: Couldn't reached to server. Please check your internet connection")

                /** call back shouldn't be fired. **/
                coVerify (exactly = 0) { callBackObj.serviceActionCallBack() }
                confirmVerified(callBackObj)
            }
        }
    }

    @Test
    fun `An exception is occurred during call Service`() = runBlockingTest {
        every { networkChecker.networkState.value } returns NetworkState.Connected
        coEvery { callBackObj.serviceActionCallBack() } throws Exception()

        val flowResponse = repository.callServiceAsFlow { callBackObj.serviceActionCallBack() }

        launch {
            flowResponse.collect { response ->
                /** ignore first loading state **/
                if(response is NetworkResponse.Loading){ return@collect }

                /** repository function response type should be NetworkResponse.Error **/
                assertThat(response).isInstanceOf(NetworkResponse.Error::class.java)
                /** returned error message should be unexpected error **/
                assertThat((response as NetworkResponse.Error).message).isEqualTo("BaseRepository: UnExpected Service Exception")

                /** call back should be fired. **/
                coVerify (exactly = 1) { callBackObj.serviceActionCallBack() }
                confirmVerified(callBackObj)
            }
        }
    }
}
