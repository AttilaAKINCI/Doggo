package com.akinci.doggoappcompose.data.repository

import com.akinci.doggoappcompose.common.network.NetworkChecker
import com.akinci.doggoappcompose.common.network.NetworkResponse
import com.akinci.doggoappcompose.common.network.NetworkState
import com.akinci.doggoappcompose.common.repository.BaseRepository
import com.akinci.doggoappcompose.data.local.dao.BreedDao
import com.akinci.doggoappcompose.data.local.dao.ContentDao
import com.akinci.doggoappcompose.data.local.dao.SubBreedDao
import com.akinci.doggoappcompose.data.mapper.convertToBreedListEntity
import com.akinci.doggoappcompose.data.mapper.convertToDoggoContentListEntity
import com.akinci.doggoappcompose.data.mapper.convertToSubBreedListEntity
import com.akinci.doggoappcompose.data.remote.api.DoggoServiceDao
import com.akinci.doggoappcompose.data.remote.output.BreedListResponse
import com.akinci.doggoappcompose.data.remote.output.BreedResponse
import com.akinci.doggoappcompose.data.remote.output.SubBreedListResponse
import com.akinci.doggoappcompose.jsonresponses.GetBreedListResponse
import com.akinci.doggoappcompose.jsonresponses.GetBreedResponse
import com.akinci.doggoappcompose.jsonresponses.GetSubBreedListResponse
import com.akinci.doggoappcompose.ui.feaute.dashboard.data.Breed
import com.google.common.truth.Truth
import com.squareup.moshi.Moshi
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Response

class DoggoRepositoryTest {

    @MockK
    lateinit var doggoServiceDao: DoggoServiceDao

    @MockK
    lateinit var breedDao: BreedDao

    @MockK
    lateinit var subBreedDao: SubBreedDao

    @MockK
    lateinit var contentDao: ContentDao

    @MockK
    lateinit var networkChecker: NetworkChecker

    private lateinit var repository : DoggoRepository
    private val moshi = Moshi.Builder().build()

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        repository = DoggoRepository(
            doggoServiceDao,
            breedDao,
            subBreedDao,
            contentDao,
            networkChecker,
            BaseRepository(networkChecker)
        )
    }

    @AfterEach
    fun tearDown() { unmockkAll() }

    @Test
    fun `Network is ok, getBreedList function is called, returns NetworkResponse-Success for success`() = runBlockingTest {
        val data =  moshi.adapter(BreedListResponse::class.java).fromJson(GetBreedListResponse.breedList)

        every { networkChecker.networkState.value } returns NetworkState.Connected
        coEvery { doggoServiceDao.getBreedList() } returns Response.success(data)

        val flowResponse = repository.getBreedList()

        launch {
            flowResponse.collect { response ->
                /** ignore first loading state **/
                if(response is NetworkResponse.Loading){ return@collect }

                /** repository function response type should be NetworkResponse.Success **/
                Truth.assertThat(response).isInstanceOf(NetworkResponse.Success::class.java)

                val fetchedResponse = (response as NetworkResponse.Success).data
                Truth.assertThat(fetchedResponse).isInstanceOf(BreedListResponse::class.java)

                Truth.assertThat(
                    (fetchedResponse as BreedListResponse).message.size
                ).isEqualTo(
                    data?.message?.size
                )

                /** call back should be fired. **/
                coVerify (exactly = 1) { doggoServiceDao.getBreedList() }
                coVerify (exactly = 0) { breedDao.getAllBreeds() }
                confirmVerified(doggoServiceDao, breedDao)
            }
        }
    }

    @Test
    fun `Network is not ok, getBreedList function is called, returns NetworkResponse-Success for success`() = runBlockingTest {
        val data =  moshi.adapter(BreedListResponse::class.java).fromJson(GetBreedListResponse.breedList)!!

        every { networkChecker.networkState.value } returns NetworkState.NotConnected
        coEvery { breedDao.getAllBreeds() } returns data.message.keys.map { item -> Breed(item) }.convertToBreedListEntity()

        val flowResponse = repository.getBreedList()

        launch {
            flowResponse.collect { response ->
                /** ignore first loading state **/
                if(response is NetworkResponse.Loading){ return@collect }

                /** repository function response type should be NetworkResponse.Success **/
                Truth.assertThat(response).isInstanceOf(NetworkResponse.Success::class.java)

                val fetchedResponse = (response as NetworkResponse.Success).data
                Truth.assertThat(fetchedResponse).isInstanceOf(BreedListResponse::class.java)

                Truth.assertThat(
                    (fetchedResponse as BreedListResponse).message.size
                ).isEqualTo(
                    data?.message?.size
                )

                /** call back should be fired. **/
                coVerify (exactly = 0) { doggoServiceDao.getBreedList() }
                coVerify (exactly = 1) { breedDao.getAllBreeds() }
                confirmVerified(doggoServiceDao, breedDao)
            }
        }
    }

    @Test
    fun `Network is ok, getSubBreedList function is called, returns NetworkResponse-Success for success`() = runBlockingTest {
        val data =  moshi.adapter(SubBreedListResponse::class.java).fromJson(GetSubBreedListResponse.subBreedList)
        val breed = "hound"

        every { networkChecker.networkState.value } returns NetworkState.Connected
        coEvery { doggoServiceDao.getSubBreedList(breed) } returns Response.success(data)

        val flowResponse = repository.getSubBreedList(breed)

        launch {
            flowResponse.collect { response ->
                /** ignore first loading state **/
                if(response is NetworkResponse.Loading){ return@collect }

                /** repository function response type should be NetworkResponse.Success **/
                Truth.assertThat(response).isInstanceOf(NetworkResponse.Success::class.java)

                val fetchedResponse = (response as NetworkResponse.Success).data
                Truth.assertThat(fetchedResponse).isInstanceOf(SubBreedListResponse::class.java)

                Truth.assertThat(
                    (fetchedResponse as SubBreedListResponse).message.size
                ).isEqualTo(
                    data?.message?.size
                )

                /** call back should be fired. **/
                coVerify (exactly = 1) { doggoServiceDao.getSubBreedList(breed) }
                coVerify (exactly = 0) { subBreedDao.getAllSubBreeds(breed) }
                confirmVerified(doggoServiceDao, subBreedDao)
            }
        }
    }

    @Test
    fun `Network is not ok, getSubBreedList function is called, returns NetworkResponse-Success for success`() = runBlockingTest {
        val data =  moshi.adapter(SubBreedListResponse::class.java).fromJson(GetSubBreedListResponse.subBreedList)!!
        val breed = "hound"

        every { networkChecker.networkState.value } returns NetworkState.NotConnected
        coEvery { subBreedDao.getAllSubBreeds(breed) } returns data.message.map { item -> Breed(item) }.convertToSubBreedListEntity(breed)

        val flowResponse = repository.getSubBreedList(breed)

        launch {
            flowResponse.collect { response ->
                /** ignore first loading state **/
                if(response is NetworkResponse.Loading){ return@collect }

                /** repository function response type should be NetworkResponse.Success **/
                Truth.assertThat(response).isInstanceOf(NetworkResponse.Success::class.java)

                val fetchedResponse = (response as NetworkResponse.Success).data
                Truth.assertThat(fetchedResponse).isInstanceOf(SubBreedListResponse::class.java)

                Truth.assertThat(
                    (fetchedResponse as SubBreedListResponse).message.size
                ).isEqualTo(
                    data?.message?.size
                )

                /** call back should be fired. **/
                coVerify (exactly = 0) { doggoServiceDao.getSubBreedList(breed) }
                coVerify (exactly = 1) { subBreedDao.getAllSubBreeds(breed) }
                confirmVerified(doggoServiceDao, subBreedDao)
            }
        }
    }

    @Test
    fun `Network is ok, getDoggoContent function is called with filled sub breed, returns NetworkResponse-Success for success`() = runBlockingTest {
        val data =  moshi.adapter(BreedResponse::class.java).fromJson(GetBreedResponse.contentResponse)
        val breed = "hound"
        val subBreed = "afghan"

        every { networkChecker.networkState.value } returns NetworkState.Connected

        coEvery { doggoServiceDao.getBreedContent(breed, 3) } returns Response.success(data)
        coEvery { doggoServiceDao.getSubBreedContent(breed, subBreed, 3) } returns Response.success(data)

        val flowResponse = repository.getDoggoContent(breed, subBreed, count = 3)

        launch {
            flowResponse.collect { response ->
                /** ignore first loading state **/
                if(response is NetworkResponse.Loading){ return@collect }

                /** repository function response type should be NetworkResponse.Success **/
                Truth.assertThat(response).isInstanceOf(NetworkResponse.Success::class.java)

                val fetchedResponse = (response as NetworkResponse.Success).data
                Truth.assertThat(fetchedResponse).isInstanceOf(BreedResponse::class.java)

                Truth.assertThat(
                    (fetchedResponse as BreedResponse).message.size
                ).isEqualTo(
                    data?.message?.size
                )

                /** call back should be fired. **/
                coVerify (exactly = 0) { doggoServiceDao.getBreedContent(breed,3) }
                coVerify (exactly = 1) { doggoServiceDao.getSubBreedContent(breed, subBreed, 3) }
                coVerify (exactly = 0) { contentDao.getAllContents(breed, subBreed) }
                confirmVerified(doggoServiceDao, subBreedDao, contentDao)
            }
        }
    }

    @Test
    fun `Network is ok, getDoggoContent function is called without sub breed, returns NetworkResponse-Success for success`() = runBlockingTest {
        val data =  moshi.adapter(BreedResponse::class.java).fromJson(GetBreedResponse.contentResponse)
        val breed = "hound"
        val subBreed = ""

        every { networkChecker.networkState.value } returns NetworkState.Connected

        coEvery { doggoServiceDao.getBreedContent(breed, 3) } returns Response.success(data)
        coEvery { doggoServiceDao.getSubBreedContent(breed, subBreed, 3) } returns Response.success(data)

        val flowResponse = repository.getDoggoContent(breed, subBreed, count = 3)

        launch {
            flowResponse.collect { response ->
                /** ignore first loading state **/
                if(response is NetworkResponse.Loading){ return@collect }

                /** repository function response type should be NetworkResponse.Success **/
                Truth.assertThat(response).isInstanceOf(NetworkResponse.Success::class.java)

                val fetchedResponse = (response as NetworkResponse.Success).data
                Truth.assertThat(fetchedResponse).isInstanceOf(BreedResponse::class.java)

                Truth.assertThat(
                    (fetchedResponse as BreedResponse).message.size
                ).isEqualTo(
                    data?.message?.size
                )

                /** call back should be fired. **/
                coVerify (exactly = 1) { doggoServiceDao.getBreedContent(breed,3) }
                coVerify (exactly = 0) { doggoServiceDao.getSubBreedContent(breed, subBreed, 3) }
                coVerify (exactly = 0) { contentDao.getAllContents(breed, subBreed) }
                confirmVerified(doggoServiceDao, subBreedDao, contentDao)
            }
        }
    }

    @Test
    fun `Network is not ok, getDoggoContent function is called, returns NetworkResponse-Success for success`() = runBlockingTest {
        val data =  moshi.adapter(BreedResponse::class.java).fromJson(GetBreedResponse.contentResponse)!!
        val breed = "hound"
        val subBreed = "afghan"

        every { networkChecker.networkState.value } returns NetworkState.NotConnected

        coEvery { doggoServiceDao.getBreedContent(breed, 3) } returns Response.success(data)
        coEvery { doggoServiceDao.getSubBreedContent(breed, subBreed, 3) } returns Response.success(data)
        coEvery { contentDao.getAllContents(breed, subBreed) } returns data.message.map { item -> item }.convertToDoggoContentListEntity(breed, subBreed)

        val flowResponse = repository.getDoggoContent(breed, subBreed, count = 3)

        launch {
            flowResponse.collect { response ->
                /** ignore first loading state **/
                if(response is NetworkResponse.Loading){ return@collect }

                /** repository function response type should be NetworkResponse.Success **/
                Truth.assertThat(response).isInstanceOf(NetworkResponse.Success::class.java)

                val fetchedResponse = (response as NetworkResponse.Success).data
                Truth.assertThat(fetchedResponse).isInstanceOf(BreedResponse::class.java)

                Truth.assertThat(
                    (fetchedResponse as BreedResponse).message.size
                ).isEqualTo(
                    data?.message?.size
                )

                /** call back should be fired. **/
                coVerify (exactly = 0) { doggoServiceDao.getBreedContent(breed,3) }
                coVerify (exactly = 0) { doggoServiceDao.getSubBreedContent(breed, subBreed, 3) }
                coVerify (exactly = 1) { contentDao.getAllContents(breed, subBreed) }
                confirmVerified(doggoServiceDao, subBreedDao, contentDao)
            }
        }
    }



}