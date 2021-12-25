package com.akinci.doggoapp.feature.dashboard

import app.cash.turbine.test
import com.akinci.doggoapp.common.helper.NetworkResponse
import com.akinci.doggoapp.common.helper.state.ListState
import com.akinci.doggoapp.common.helper.state.UIState
import com.akinci.doggoapp.common.network.NetworkChecker
import com.akinci.doggoapp.coroutine.TestContextProvider2
import com.akinci.doggoapp.data.local.dao.BreedDao
import com.akinci.doggoapp.data.local.dao.ContentDao
import com.akinci.doggoapp.data.local.dao.SubBreedDao
import com.akinci.doggoapp.data.remote.output.BreedListResponse
import com.akinci.doggoapp.data.remote.output.BreedResponse
import com.akinci.doggoapp.data.remote.output.SubBreedListResponse
import com.akinci.doggoapp.data.repository.DoggoRepository
import com.akinci.doggoapp.feature.dashboard.data.Breed
import com.akinci.doggoapp.feature.dashboard.viewmodel.DashboardViewModel
import com.akinci.doggoapp.feature.detail.viewmodel.DetailViewModel
import com.akinci.doggoapp.jsonresponses.GetBreedListResponse
import com.akinci.doggoapp.jsonresponses.GetBreedResponse
import com.akinci.doggoapp.jsonresponses.GetSubBreedListResponse
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
class DashboardViewModelTest {

    @MockK
    lateinit var doggoRepository: DoggoRepository

    @MockK
    lateinit var breedDao: BreedDao

    @MockK
    lateinit var subBreedDao: SubBreedDao

    @MockK
    lateinit var networkChecker: NetworkChecker

    private lateinit var dashboardViewModel: DashboardViewModel
    private val moshi = Moshi.Builder().build()

    private val coroutineContextProvider = TestContextProvider2()

    @BeforeEach
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        dashboardViewModel = DashboardViewModel(coroutineContextProvider, doggoRepository, breedDao, subBreedDao, networkChecker)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `select breed`() = runBlockingTest {
        val data: BreedListResponse = moshi.adapter(BreedListResponse::class.java).fromJson(GetBreedListResponse.breedList)!!
        val breed = "hound"

        coEvery { doggoRepository.getBreedList() } returns flow { emit(NetworkResponse.Success(data)) }

        // breed list is filled.
        dashboardViewModel.getBreedList()

        dashboardViewModel.selectBreed(Breed(breed, false))
        coroutineContextProvider.testCoroutineDispatcher.advanceUntilIdle() // helps if your code has delay()

        dashboardViewModel.breedListData.test {
            val item = awaitItem()
            Truth.assertThat(item).isInstanceOf(ListState.OnData::class.java)
            Truth.assertThat((item as ListState.OnData).data).isNotNull()

            // validate selected items state
            val foundedItemInList = item.data!!.first { it.name == breed }
            Truth.assertThat(foundedItemInList.selected).isEqualTo(true)

            cancelAndIgnoreRemainingEvents()
        }


        /** call back should be fired. **/
        coVerify (exactly = 1) { doggoRepository.getBreedList() }
        coVerify (exactly = 1) { breedDao.insertBreed(any()) }
        confirmVerified(doggoRepository, breedDao)
    }

    @Test
    fun `get breed list returns data`() = runBlockingTest {
        val data: BreedListResponse = moshi.adapter(BreedListResponse::class.java).fromJson(GetBreedListResponse.breedList)!!

        coEvery { doggoRepository.getBreedList() } returns flow { emit(NetworkResponse.Success(data)) }

        dashboardViewModel.getBreedList()
        coroutineContextProvider.testCoroutineDispatcher.advanceUntilIdle() // helps if your code has delay()

        dashboardViewModel.breedListData.test {
            val item = awaitItem()
            Truth.assertThat(item).isInstanceOf(ListState.OnData::class.java)
            Truth.assertThat((item as ListState.OnData).data).isNotNull()
            Truth.assertThat(item.data!!.size).isEqualTo(data.message.size)
            cancelAndIgnoreRemainingEvents()
        }

        /** call back should be fired. **/
        coVerify (exactly = 1) { doggoRepository.getBreedList() }
        coVerify (exactly = 1) { breedDao.insertBreed(any()) }
        confirmVerified(doggoRepository, breedDao)
    }

    @Test
    fun `get breed list returns loading`() = runBlockingTest {
        coEvery { doggoRepository.getBreedList() } returns flow { emit(NetworkResponse.Loading()) }

        dashboardViewModel.getBreedList()
        coroutineContextProvider.testCoroutineDispatcher.advanceUntilIdle() // helps if your code has delay()

        dashboardViewModel.breedListData.test {
            val item = awaitItem()
            Truth.assertThat(item).isInstanceOf(ListState.OnLoading::class.java)
            cancelAndIgnoreRemainingEvents()
        }

        /** call back should be fired. **/
        coVerify (exactly = 1) { doggoRepository.getBreedList() }
        coVerify (exactly = 0) { breedDao.insertBreed(any()) }
        confirmVerified(doggoRepository, breedDao)
    }

    @Test
    fun `get breed list returns error`() = runBlockingTest {
        coEvery { doggoRepository.getBreedList() } returns flow { emit(NetworkResponse.Error("Error")) }

        dashboardViewModel.getBreedList()
        coroutineContextProvider.testCoroutineDispatcher.advanceUntilIdle() // helps if your code has delay()

        dashboardViewModel.uiState.test {
            val item = awaitItem()
            Truth.assertThat(item).isInstanceOf(UIState.OnServiceError::class.java)
            cancelAndIgnoreRemainingEvents()
        }

        /** call back should be fired. **/
        coVerify (exactly = 1) { doggoRepository.getBreedList() }
        coVerify (exactly = 0) { breedDao.insertBreed(any()) }
        confirmVerified(doggoRepository, breedDao)
    }

    @Test
    fun `select sub breed`() = runBlockingTest {
        val data: SubBreedListResponse = moshi.adapter(SubBreedListResponse::class.java).fromJson(GetSubBreedListResponse.subBreedList)!!
        val breed = "hound"
        val subBreed = "afghan"

        coEvery { doggoRepository.getSubBreedList(breed) } returns flow { emit(NetworkResponse.Success(data)) }

        // breed list is filled.
        dashboardViewModel.getSubBreedList(breed)

        dashboardViewModel.selectSubBreed(Breed(subBreed, false))
        coroutineContextProvider.testCoroutineDispatcher.advanceUntilIdle() // helps if your code has delay()

        dashboardViewModel.subBreedListData.test {
            val item = awaitItem()
            Truth.assertThat(item).isInstanceOf(ListState.OnData::class.java)
            Truth.assertThat((item as ListState.OnData).data).isNotNull()

            // validate selected items state
            val foundedItemInList = item.data!!.first { it.name == subBreed }
            Truth.assertThat(foundedItemInList.selected).isEqualTo(true)

            cancelAndIgnoreRemainingEvents()
        }

        /** call back should be fired. **/
        coVerify (exactly = 1) { doggoRepository.getSubBreedList(any()) }
        coVerify (exactly = 1) { subBreedDao.insertSubBreed(any()) }
        confirmVerified(doggoRepository, subBreedDao)
    }

    @Test
    fun `get sub breed list returns data`() = runBlockingTest {
        val data: SubBreedListResponse = moshi.adapter(SubBreedListResponse::class.java).fromJson(GetSubBreedListResponse.subBreedList)!!
        val breed = "hound"

        coEvery { doggoRepository.getSubBreedList(breed) } returns flow { emit(NetworkResponse.Success(data)) }

        dashboardViewModel.getSubBreedList(breed)
        coroutineContextProvider.testCoroutineDispatcher.advanceUntilIdle() // helps if your code has delay()

        dashboardViewModel.subBreedListData.test {
            val item = awaitItem()
            Truth.assertThat(item).isInstanceOf(ListState.OnData::class.java)
            Truth.assertThat((item as ListState.OnData).data).isNotNull()
            Truth.assertThat(item.data!!.size).isEqualTo(data.message.size)
            cancelAndIgnoreRemainingEvents()
        }

        /** call back should be fired. **/
        coVerify (exactly = 1) { doggoRepository.getSubBreedList(any()) }
        coVerify (exactly = 1) { subBreedDao.insertSubBreed(any()) }
        confirmVerified(doggoRepository, subBreedDao)
    }

    @Test
    fun `get sub breed list returns loading`() = runBlockingTest {
        val breed = "hound"

        coEvery { doggoRepository.getSubBreedList(breed) } returns flow { emit(NetworkResponse.Loading()) }

        dashboardViewModel.getSubBreedList(breed)
        coroutineContextProvider.testCoroutineDispatcher.advanceUntilIdle() // helps if your code has delay()

        dashboardViewModel.subBreedListData.test {
            val item = awaitItem()
            Truth.assertThat(item).isInstanceOf(ListState.OnLoading::class.java)
            cancelAndIgnoreRemainingEvents()
        }

        /** call back should be fired. **/
        coVerify (exactly = 1) { doggoRepository.getSubBreedList(any()) }
        coVerify (exactly = 0) { subBreedDao.insertSubBreed(any()) }
        confirmVerified(doggoRepository, subBreedDao)
    }

    @Test
    fun `get sub breed list returns error`() = runBlockingTest {
        val breed = "hound"

        coEvery { doggoRepository.getSubBreedList(breed) } returns flow { emit(NetworkResponse.Error("Error")) }

        dashboardViewModel.getSubBreedList(breed)
        coroutineContextProvider.testCoroutineDispatcher.advanceUntilIdle() // helps if your code has delay()

        dashboardViewModel.uiState.test {
            val item = awaitItem()
            Truth.assertThat(item).isInstanceOf(UIState.OnServiceError::class.java)
            cancelAndIgnoreRemainingEvents()
        }

        /** call back should be fired. **/
        coVerify (exactly = 1) { doggoRepository.getSubBreedList(any()) }
        coVerify (exactly = 0) { subBreedDao.insertSubBreed(any()) }
        confirmVerified(doggoRepository, subBreedDao)
    }
}