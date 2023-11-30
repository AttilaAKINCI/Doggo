package com.akinci.doggo.ui

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.akinci.doggo.core.coroutine.MainDispatcherRule
import com.akinci.doggo.core.coroutine.TestContextProvider
import com.akinci.doggo.domain.data.Image
import com.akinci.doggo.domain.data.ImagesUseCase
import com.akinci.doggo.ui.features.detail.DetailViewModel
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.time.Duration

@ExtendWith(MainDispatcherRule::class)
class DetailViewModelTest {

    private val testContextProvider = TestContextProvider()
    private val imagesUseCaseMock: ImagesUseCase = mockk(relaxed = true)

    @Test
    fun `should return correct title in state when detailScreen gets breed and subBreed as parameter`() =
        runTest {
            coEvery { imagesUseCaseMock.getImage(any(), any()) } returns Result.failure(Exception())

            val testedClass = buildVM()

            testedClass.stateFlow.test {
                with(awaitItem()) {
                    title shouldBe "Hound/Afghan"
                }

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `should return correct title in state when detailScreen gets only breed as parameter`() =
        runTest {
            coEvery { imagesUseCaseMock.getImage(any(), any()) } returns Result.failure(Exception())

            val testedClass = buildVM(
                mapOf("breed" to "Hound")
            )

            testedClass.stateFlow.test {
                with(awaitItem()) {
                    title shouldBe "Hound"
                }

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `should return no data state when empty list is received`() = runTest {
        coEvery { imagesUseCaseMock.getImage(any(), any()) } returns Result.success(listOf())

        val testedClass = buildVM()

        testedClass.stateFlow.test(timeout = Duration.INFINITE) {
            skipItems(1) // skip initial state

            with(awaitItem()) {
                isLoading shouldBe false
                isNoData shouldBe true
                isError shouldBe false
            }

            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `should return error state when imageUseCase returns failure`() = runTest {
        coEvery { imagesUseCaseMock.getImage(any(), any()) } returns Result.failure(Exception())

        val testedClass = buildVM()

        testedClass.stateFlow.test(timeout = Duration.INFINITE) {
            skipItems(1) // skip initial state

            with(awaitItem()) {
                isLoading shouldBe false
                isNoData shouldBe false
                isError shouldBe true
            }

            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `should return correct image list state when image list is received`() = runTest {
        coEvery { imagesUseCaseMock.getImage(any(), any()) } returns Result.success(
            listOf(
                Image(
                    breed = "hound",
                    subBreed = "afghan",
                    dogName = "bill",
                    imageUrl = "imageUrl1"
                ),
                Image(
                    breed = "hound",
                    subBreed = "afghan",
                    dogName = "jack",
                    imageUrl = "imageUrl2"
                ),
            )
        )

        val testedClass = buildVM()

        testedClass.stateFlow.test(timeout = Duration.INFINITE) {
            skipItems(1) // skip initial state

            with(awaitItem()) {
                images.size shouldBeGreaterThan 0
                images[0].imageUrl shouldBe "imageUrl1"
                images[1].imageUrl shouldBe "imageUrl2"
                isLoading shouldBe false
                isNoData shouldBe false
                isError shouldBe false
            }

            ensureAllEventsConsumed()
        }
    }

    private fun buildVM(
        params: Map<String, String> = mapOf(
            "breed" to "Hound",
            "subBreed" to "Afghan",
        )
    ) = DetailViewModel(
        savedStateHandle = SavedStateHandle(params),
        contextProvider = testContextProvider,
        imagesUseCase = imagesUseCaseMock,
    )
}