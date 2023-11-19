package com.akinci.doggo.ui.feature.dashboard

import com.akinci.doggo.common.helper.state.UIState
import com.akinci.doggo.coroutine.TestContextProvider2
import com.akinci.doggo.data.breed.local.BreedDao
import com.akinci.doggo.data.subbreed.local.SubBreedDao
import com.akinci.doggo.data.breed.remote.BreedListServiceResponse
import com.akinci.doggo.data.subbreed.remote.SubBreedListServiceResponse
import com.akinci.doggo.data.DoggoRepository
import com.akinci.doggo.jsonresponses.GetBreedListResponse
import com.akinci.doggo.jsonresponses.GetSubBreedListResponse
import com.akinci.doggo.ui.features.dashboard.DashboardViewModel
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

    private lateinit var dashboardViewModel: DashboardViewModel
    private val moshi = Moshi.Builder().build()

    private val coroutineContextProvider = TestContextProvider2()

    @BeforeEach
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        val breedData: BreedListServiceResponse = moshi.adapter(BreedListServiceResponse::class.java).fromJson(GetBreedListResponse.breedList)!!
        coEvery { doggoRepository.getBreedList() } returns flow { emit(NetworkResponse.Success(breedData)) }

        dashboardViewModel = DashboardViewModel(coroutineContextProvider, doggoRepository, breedDao, subBreedDao)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `select breed`() = runBlockingTest {
        val subBreedData: SubBreedListServiceResponse = moshi.adapter(SubBreedListServiceResponse::class.java).fromJson(GetSubBreedListResponse.subBreedList)!!
        val breed = "hound"

        coEvery { doggoRepository.getSubBreedList(breed = breed) } returns flow { emit(NetworkResponse.Success(subBreedData)) }

        dashboardViewModel.selectBreed(breedName = breed)

        Truth.assertThat(dashboardViewModel.selectedBreedName).isEqualTo(breed)
        Truth.assertThat(dashboardViewModel.selectedSubBreedName).isEqualTo("")

        dashboardViewModel.getSubBreedList(breed = breed)
        coroutineContextProvider.testCoroutineDispatcher.advanceUntilIdle() // helps if your code has delay()

        Truth.assertThat(dashboardViewModel.subBreedListState.size).isEqualTo(subBreedData.message.size)

        /** call back should be fired. **/
        coVerify (exactly = 1) { doggoRepository.getBreedList() }
        coVerify (atLeast = 1) { doggoRepository.getSubBreedList(any()) }
        coVerify (atLeast = 1) { subBreedDao.insertSubBreed(any()) }
        confirmVerified(doggoRepository, subBreedDao)
    }

    @Test
    fun `select sub breed`() = runBlockingTest {
        val subBreed = "afghan"

        dashboardViewModel.selectSubBreed(subBreedName = subBreed)
        Truth.assertThat(dashboardViewModel.selectedSubBreedName).isEqualTo(subBreed)
    }

    @Test
    fun `network warning seen`() = runBlockingTest {
        dashboardViewModel.networkWarningSeen()
        Truth.assertThat(dashboardViewModel.isNetworkWarningDialogVisible).isEqualTo(false)
    }

    @Test
    fun `validate breed not selected`() = runBlockingTest {
        dashboardViewModel.selectedBreedName = ""
        val validationResult = dashboardViewModel.validate()
        Truth.assertThat(validationResult).isEqualTo(false)
    }

    @Test
    fun `validate breed selected, sub breed not selected, empty sub breed list`() = runBlockingTest {
        val subBreed = ""
        val breed = "boxer"
        dashboardViewModel.selectedBreedName = breed

        val subBreedData = SubBreedListServiceResponse(message = listOf(), status = "success")
        coEvery { doggoRepository.getSubBreedList(breed = breed) } returns flow { emit(NetworkResponse.Success(subBreedData)) }

        dashboardViewModel.getSubBreedList(breed = breed)
        coroutineContextProvider.testCoroutineDispatcher.advanceUntilIdle() // helps if your code has delay()

        val validationResult = dashboardViewModel.validate()
        Truth.assertThat(validationResult).isEqualTo(true)
    }

    @Test
    fun `validate breed selected, sub breed not selected, filled sub breed list`() = runBlockingTest {
        val breed = "hound"
        val subBreed = "afghan"
        dashboardViewModel.selectedBreedName = breed

        val subBreedData: SubBreedListServiceResponse = moshi.adapter(SubBreedListServiceResponse::class.java).fromJson(GetSubBreedListResponse.subBreedList)!!
        coEvery { doggoRepository.getSubBreedList(breed = breed) } returns flow { emit(NetworkResponse.Success(subBreedData)) }

        dashboardViewModel.getSubBreedList(breed = breed)
        coroutineContextProvider.testCoroutineDispatcher.advanceUntilIdle() // helps if your code has delay()

        val validationResult = dashboardViewModel.validate()
        Truth.assertThat(validationResult).isEqualTo(false)  // selectedSubBreedName empty....
    }

    @Test
    fun `validate breed selected, sub breed selected, filled sub breed list`() = runBlockingTest {
        val breed = "hound"
        val subBreed = "afghan"
        dashboardViewModel.selectedBreedName = breed
        dashboardViewModel.selectedSubBreedName = subBreed

        val subBreedData: SubBreedListServiceResponse = moshi.adapter(SubBreedListServiceResponse::class.java).fromJson(GetSubBreedListResponse.subBreedList)!!
        coEvery { doggoRepository.getSubBreedList(breed = breed) } returns flow { emit(NetworkResponse.Success(subBreedData)) }

        dashboardViewModel.getSubBreedList(breed = breed)
        coroutineContextProvider.testCoroutineDispatcher.advanceUntilIdle() // helps if your code has delay()

        val validationResult = dashboardViewModel.validate()
        Truth.assertThat(validationResult).isEqualTo(true)
    }

    @Test
    fun `get breed list returns data`() = runBlockingTest {
        /** getBreedList() automatically called when test start **/

        /** call back should be fired. **/
        coVerify (exactly = 1) { doggoRepository.getBreedList() }
        coVerify (exactly = 1) { breedDao.insertBreed(any()) }
        confirmVerified(doggoRepository, breedDao)
    }

    @Test
    fun `get sub breed list returns data`() = runBlockingTest {
        val subBreedData: SubBreedListServiceResponse = moshi.adapter(SubBreedListServiceResponse::class.java).fromJson(GetSubBreedListResponse.subBreedList)!!
        val breed = "hound"

        coEvery { doggoRepository.getSubBreedList(breed = breed) } returns flow { emit(NetworkResponse.Success(subBreedData)) }

        dashboardViewModel.getSubBreedList(breed = breed)

        Truth.assertThat(dashboardViewModel.informer).isNotInstanceOf(UIState.OnLoading::class.java)
        Truth.assertThat(dashboardViewModel.informer).isNotInstanceOf(UIState.OnServiceError::class.java)
        Truth.assertThat(dashboardViewModel.informer).isInstanceOf(UIState.None::class.java)

        /** call back should be fired. **/
        coVerify (exactly = 1) { doggoRepository.getBreedList() }
        coVerify (atLeast = 1) { doggoRepository.getSubBreedList(any()) }
        coVerify (atLeast = 1) { subBreedDao.insertSubBreed(any()) }
        coVerify (exactly = 1) { breedDao.insertBreed(any()) }
        confirmVerified(doggoRepository, breedDao, subBreedDao)
    }

    @Test
    fun `get sub breed list returns loading`() = runBlockingTest {
        val breed = "hound"
        coEvery { doggoRepository.getSubBreedList(breed = breed) } returns flow { emit(NetworkResponse.Loading()) }

        dashboardViewModel.getSubBreedList(breed = breed)

        Truth.assertThat(dashboardViewModel.informer).isInstanceOf(UIState.OnLoading::class.java)
        Truth.assertThat(dashboardViewModel.informer).isNotInstanceOf(UIState.OnServiceError::class.java)
        Truth.assertThat(dashboardViewModel.informer).isNotInstanceOf(UIState.None::class.java)

        /** call back should be fired. **/
        coVerify (exactly = 1) { doggoRepository.getBreedList() }
        coVerify (atLeast = 1) { doggoRepository.getSubBreedList(any()) }
        coVerify (exactly = 0) { subBreedDao.insertSubBreed(any()) }
        coVerify (exactly = 1) { breedDao.insertBreed(any()) }
        confirmVerified(doggoRepository, breedDao, subBreedDao)
    }

    @Test
    fun `get sub breed list returns error`() = runBlockingTest {
        val breed = "hound"
        coEvery { doggoRepository.getSubBreedList(breed = breed) } returns flow { emit(NetworkResponse.Error("Error")) }

        dashboardViewModel.getSubBreedList(breed = breed)

        Truth.assertThat(dashboardViewModel.informer).isNotInstanceOf(UIState.OnLoading::class.java)
        Truth.assertThat(dashboardViewModel.informer).isInstanceOf(UIState.OnServiceError::class.java)
        Truth.assertThat(dashboardViewModel.informer).isNotInstanceOf(UIState.None::class.java)

        /** call back should be fired. **/
        coVerify (exactly = 1) { doggoRepository.getBreedList() }
        coVerify (atLeast = 1) { doggoRepository.getSubBreedList(any()) }
        coVerify (exactly = 0) { subBreedDao.insertSubBreed(any()) }
        coVerify (exactly = 1) { breedDao.insertBreed(any()) }
        confirmVerified(doggoRepository, breedDao, subBreedDao)
    }
}