package com.akinci.doggoapp.feature.detail

import app.cash.turbine.test
import com.akinci.doggoapp.common.helper.NetworkResponse
import com.akinci.doggoapp.common.helper.state.ListState
import com.akinci.doggoapp.common.helper.state.UIState
import com.akinci.doggoapp.coroutine.TestContextProvider2
import com.akinci.doggoapp.data.local.dao.ContentDao
import com.akinci.doggoapp.data.remote.output.BreedResponse
import com.akinci.doggoapp.data.repository.DoggoRepository
import com.akinci.doggoapp.feature.detail.viewmodel.DetailViewModel
import com.akinci.doggoapp.jsonresponses.GetBreedResponse
import com.google.common.truth.Truth
import com.squareup.moshi.Moshi
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class DetailViewModelTest {

    @MockK
    lateinit var doggoRepository: DoggoRepository

    @MockK
    lateinit var contentDao: ContentDao

    private lateinit var detailViewModel: DetailViewModel
    private val moshi = Moshi.Builder().build()

    private val coroutineContextProvider = TestContextProvider2()

    @BeforeEach
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        detailViewModel = DetailViewModel(coroutineContextProvider, doggoRepository, contentDao)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `get doggo content returns data`() = runBlockingTest {
        val data: BreedResponse = moshi.adapter(BreedResponse::class.java).fromJson(GetBreedResponse.contentResponse)!!
        val breed = "hound"
        val subBreed = "afghan"

        coEvery { doggoRepository.getDoggoContent(breed, subBreed, 3) } returns flow { emit(NetworkResponse.Success(data)) }

        detailViewModel.getDoggoContent(breed, subBreed, 3)
        coroutineContextProvider.testCoroutineDispatcher.advanceUntilIdle() // helps if your code has delay()

        detailViewModel.breedImageListData.test {
            val item = awaitItem()
            Truth.assertThat(item).isInstanceOf(ListState.OnData::class.java)
            Truth.assertThat((item as ListState.OnData).data).isNotNull()
            Truth.assertThat(item.data!!.size).isEqualTo(data.message.size)
            cancelAndIgnoreRemainingEvents()
        }

        /** call back should be fired. **/
        coVerify (exactly = 1) { doggoRepository.getDoggoContent(any(),any(), 3) }
        coVerify (exactly = 1) { contentDao.insertContent(any()) }
        confirmVerified(doggoRepository, contentDao)
    }

    @Test
    fun `get doggo content returns loading`() = runBlockingTest {
        val breed = "hound"
        val subBreed = "afghan"

        coEvery { doggoRepository.getDoggoContent(breed, subBreed, 3) } returns flow { emit(NetworkResponse.Loading()) }

        detailViewModel.getDoggoContent(breed, subBreed, 3)
        coroutineContextProvider.testCoroutineDispatcher.advanceUntilIdle() // helps if your code has delay()

        detailViewModel.breedImageListData.test {
            val item = awaitItem()
            Truth.assertThat(item).isInstanceOf(ListState.OnLoading::class.java)
            cancelAndIgnoreRemainingEvents()
        }

        /** call back should be fired. **/
        coVerify (exactly = 1) { doggoRepository.getDoggoContent(any(),any(), 3) }
        coVerify (exactly = 0) { contentDao.insertContent(any()) }
        confirmVerified(doggoRepository, contentDao)
    }

    @Test
    fun `get doggo content returns error`() = runBlockingTest {
        val breed = "hound"
        val subBreed = "afghan"

        coEvery { doggoRepository.getDoggoContent(breed, subBreed, 3) } returns flow { emit(NetworkResponse.Error("Error")) }

        detailViewModel.getDoggoContent(breed, subBreed, 3)
        coroutineContextProvider.testCoroutineDispatcher.advanceUntilIdle() // helps if your code has delay()

        detailViewModel.uiState.test {
            val item = awaitItem()
            Truth.assertThat(item).isInstanceOf(UIState.OnServiceError::class.java)
            cancelAndIgnoreRemainingEvents()
        }

        /** call back should be fired. **/
        coVerify (exactly = 1) { doggoRepository.getDoggoContent(any(),any(), 3) }
        coVerify (exactly = 0) { contentDao.insertContent(any()) }
        confirmVerified(doggoRepository, contentDao)
    }

}