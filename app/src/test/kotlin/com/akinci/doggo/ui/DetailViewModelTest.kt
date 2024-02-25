package com.akinci.doggo.ui

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.akinci.doggo.core.coroutine.MainDispatcherRule
import com.akinci.doggo.core.coroutine.TestContextProvider
import com.akinci.doggo.domain.GetImagesUseCase
import com.akinci.doggo.domain.data.Image
import com.akinci.doggo.ui.features.detail.DetailViewContract
import com.akinci.doggo.ui.features.detail.DetailViewModel
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MainDispatcherRule::class)
class DetailViewModelTest {

    private val testContextProvider = TestContextProvider()
    private val getImagesUseCaseMock: GetImagesUseCase = mockk(relaxed = true)

    @Test
    fun `should return correct title in state when detailScreen gets breed and subBreed as parameter`() =
        runTest {
            coEvery { getImagesUseCaseMock.execute(any(), any()) } returns
                    Result.failure(Exception())

            val testedClass = buildVM()

            testedClass.stateFlow.test {
                with(awaitItem()) {
                    title shouldBe "Hound/Afghan"
                }

                ensureAllEventsConsumed()
            }
        }

    @Test
    fun `should return correct title in state when detailScreen gets only breed as parameter`() =
        runTest {
            coEvery { getImagesUseCaseMock.execute(any(), any()) } returns
                    Result.failure(Exception())

            val testedClass = buildVM(
                mapOf("breed" to "Hound")
            )

            testedClass.stateFlow.test {
                with(awaitItem()) {
                    title shouldBe "Hound"
                }

                ensureAllEventsConsumed()
            }
        }


    @Test
    fun `should return no data state when empty list is received`() = runTest {
        coEvery { getImagesUseCaseMock.execute(any(), any()) } returns
                Result.success(listOf())

        val testedClass = buildVM()

        testScheduler.advanceUntilIdle()

        testedClass.stateFlow.test {
            awaitItem().type shouldBe DetailViewContract.Type.NoData
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `should return error state when imageUseCase returns failure`() = runTest {
        coEvery { getImagesUseCaseMock.execute(any(), any()) } returns Result.failure(Exception())

        val testedClass = buildVM()

        testScheduler.advanceUntilIdle()

        testedClass.stateFlow.test {
            awaitItem().type shouldBe DetailViewContract.Type.Error
            ensureAllEventsConsumed()
        }
    }


    @Test
    fun `should return correct image list state when image list is received`() = runTest {
        coEvery { getImagesUseCaseMock.execute(any(), any()) } returns Result.success(
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

        testScheduler.advanceUntilIdle()

        testedClass.stateFlow.test {
            with(awaitItem()) {
                type shouldBe DetailViewContract.Type.Content(
                    images = persistentListOf(
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
        getImagesUseCase = getImagesUseCaseMock,
    )
}
