package com.akinci.doggo.ui.feature.detail

import androidx.lifecycle.SavedStateHandle
import com.akinci.doggo.common.helper.state.UIState
import com.akinci.doggo.coroutine.TestContextProvider2
import com.akinci.doggo.data.image.local.ImageDao
import com.akinci.doggo.data.image.remote.ImageServiceResponse
import com.akinci.doggo.data.DoggoRepository
import com.akinci.doggo.jsonresponses.GetBreedResponse
import com.akinci.doggo.ui.features.detail.DetailViewModel
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
    lateinit var contentDao: ImageDao

    @MockK
    lateinit var savedStateHandle: SavedStateHandle

    private lateinit var detailViewModel: DetailViewModel
    private val moshi = Moshi.Builder().build()

    private val coroutineContextProvider = TestContextProvider2()

    @BeforeEach
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        val breed = "hound"
        val subBreed = "afghan"

        coEvery { savedStateHandle.get<String>("breed") } returns breed
        coEvery { savedStateHandle.get<String>("subBreed") } returns subBreed

        val contentData: ImageServiceResponse = moshi.adapter(ImageServiceResponse::class.java).fromJson(GetBreedResponse.contentResponse)!!
        coEvery { doggoRepository.getDoggoContent(breed, subBreed, 15) } returns flow { emit(NetworkResponse.Success(contentData)) }

        detailViewModel = DetailViewModel(coroutineContextProvider, doggoRepository, contentDao, savedStateHandle)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `network warning seen`() = runBlockingTest {
        detailViewModel.networkWarningSeen()
        Truth.assertThat(detailViewModel.isNetworkWarningDialogVisible).isEqualTo(false)
    }

    @Test
    fun `get doggo content returns data`() = runBlockingTest {
        /** getDoggoContent() automatically called when test start **/

        Truth.assertThat(detailViewModel.informer).isInstanceOf(UIState.None::class.java)
        Truth.assertThat(detailViewModel.breedImageListState.size).isNotEqualTo(0) // expect to see some data

        /** call back should be fired. **/
        coVerify (exactly = 1) { doggoRepository.getDoggoContent(breed = any(), subBreed = any(), count = any()) }
        coVerify (exactly = 1) { contentDao.insertImage(any()) }
        confirmVerified(doggoRepository, contentDao)
    }

}