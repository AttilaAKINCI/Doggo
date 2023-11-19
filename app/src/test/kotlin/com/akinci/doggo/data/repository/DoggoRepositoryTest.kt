package com.akinci.doggo.data.repository

import com.akinci.doggo.common.network.NetworkChecker
import com.akinci.doggo.common.network.NetworkResponse
import com.akinci.doggo.common.network.NetworkState
import com.akinci.doggo.common.repository.BaseRepository
import com.akinci.doggo.data.DoggoRepository
import com.akinci.doggo.data.breed.local.BreedDao
import com.akinci.doggo.data.image.local.ImageDao
import com.akinci.doggo.data.subbreed.local.SubBreedDao
import com.akinci.doggo.data.mapper.convertToBreedListEntity
import com.akinci.doggo.data.mapper.convertToDoggoContentListEntity
import com.akinci.doggo.data.mapper.convertToSubBreedListEntity
import com.akinci.doggo.data.remote.api.DoggoServiceDao
import com.akinci.doggo.data.breed.remote.BreedListServiceResponse
import com.akinci.doggo.data.image.remote.ImageServiceResponse
import com.akinci.doggo.data.subbreed.remote.SubBreedListServiceResponse
import com.akinci.doggo.jsonresponses.GetBreedListResponse
import com.akinci.doggo.jsonresponses.GetBreedResponse
import com.akinci.doggo.jsonresponses.GetSubBreedListResponse
import com.akinci.doggo.ui.features.dashboard.data.Breed
import com.google.common.truth.Truth
import com.squareup.moshi.Moshi
import io.mockk.*
import io.mockk.impl.annotations.MockK
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
    lateinit var contentDao: ImageDao

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
        val data =  moshi.adapter(BreedListServiceResponse::class.java).fromJson(GetBreedListResponse.breedList)

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
                Truth.assertThat(fetchedResponse).isInstanceOf(BreedListServiceResponse::class.java)

                Truth.assertThat(
                    (fetchedResponse as BreedListServiceResponse).message.size
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
        val data =  moshi.adapter(BreedListServiceResponse::class.java).fromJson(GetBreedListResponse.breedList)!!

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
                Truth.assertThat(fetchedResponse).isInstanceOf(BreedListServiceResponse::class.java)

                Truth.assertThat(
                    (fetchedResponse as BreedListServiceResponse).message.size
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
        val data =  moshi.adapter(SubBreedListServiceResponse::class.java).fromJson(GetSubBreedListResponse.subBreedList)
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
                Truth.assertThat(fetchedResponse).isInstanceOf(SubBreedListServiceResponse::class.java)

                Truth.assertThat(
                    (fetchedResponse as SubBreedListServiceResponse).message.size
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
        val data =  moshi.adapter(SubBreedListServiceResponse::class.java).fromJson(GetSubBreedListResponse.subBreedList)!!
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
                Truth.assertThat(fetchedResponse).isInstanceOf(SubBreedListServiceResponse::class.java)

                Truth.assertThat(
                    (fetchedResponse as SubBreedListServiceResponse).message.size
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
        val data =  moshi.adapter(ImageServiceResponse::class.java).fromJson(GetBreedResponse.contentResponse)
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
                Truth.assertThat(fetchedResponse).isInstanceOf(ImageServiceResponse::class.java)

                Truth.assertThat(
                    (fetchedResponse as ImageServiceResponse).message.size
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
        val data =  moshi.adapter(ImageServiceResponse::class.java).fromJson(GetBreedResponse.contentResponse)
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
                Truth.assertThat(fetchedResponse).isInstanceOf(ImageServiceResponse::class.java)

                Truth.assertThat(
                    (fetchedResponse as ImageServiceResponse).message.size
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
        val data =  moshi.adapter(ImageServiceResponse::class.java).fromJson(GetBreedResponse.contentResponse)!!
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
                Truth.assertThat(fetchedResponse).isInstanceOf(ImageServiceResponse::class.java)

                Truth.assertThat(
                    (fetchedResponse as ImageServiceResponse).message.size
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