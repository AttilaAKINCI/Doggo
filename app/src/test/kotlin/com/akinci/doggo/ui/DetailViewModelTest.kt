package com.akinci.doggo.ui

import androidx.lifecycle.SavedStateHandle
import com.akinci.doggo.core.coroutine.MainDispatcherRule
import com.akinci.doggo.core.coroutine.TestContextProvider
import com.akinci.doggo.domain.data.ImagesUseCase
import com.akinci.doggo.ui.features.detail.DetailViewModel
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MainDispatcherRule::class)
class DetailViewModelTest {

    private val testContextProvider = TestContextProvider()
    private val imagesUseCaseMock: ImagesUseCase = mockk(relaxed = true)

    private lateinit var testedClass: DetailViewModel

    @BeforeEach
    fun setup() {
        testedClass = DetailViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf(
                    "breed" to "hound",
                    "subBreed" to "afghan",
                )
            ),
            contextProvider = testContextProvider,
            imagesUseCase = imagesUseCaseMock,
        )
    }

    @Test
    fun `should e`() = runTest {
        // TODO
    }
}