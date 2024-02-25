package com.akinci.doggo.ui

import app.cash.turbine.test
import com.akinci.doggo.core.coroutine.MainDispatcherRule
import com.akinci.doggo.core.coroutine.TestContextProvider
import com.akinci.doggo.core.network.NetworkChecker
import com.akinci.doggo.domain.GetBreedsUseCase
import com.akinci.doggo.domain.GetSubBreedsUseCase
import com.akinci.doggo.domain.data.Breed
import com.akinci.doggo.domain.data.Chip
import com.akinci.doggo.domain.data.SubBreed
import com.akinci.doggo.ui.features.dashboard.DashboardViewContract
import com.akinci.doggo.ui.features.dashboard.DashboardViewModel
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MainDispatcherRule::class)
class DashboardViewModelTest {

    private val testContextProvider = TestContextProvider()
    private val getBreedsUseCaseMock: GetBreedsUseCase = mockk(relaxed = true)
    private val getSubBreedsUseCaseMock: GetSubBreedsUseCase = mockk(relaxed = true)
    private val networkCheckerMock: NetworkChecker = mockk(relaxed = true)

    @Test
    fun `should return correct network state when device is connected`() = runTest {
        every { networkCheckerMock.state } returns MutableStateFlow(true)
        coEvery { getBreedsUseCaseMock.execute() } returns Result.failure(Exception())

        val testedClass = buildVM()

        testedClass.stateFlow.test {
            awaitItem().isConnected shouldBe true
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `should return correct network state when device is not connected`() = runTest {
        every { networkCheckerMock.state } returns MutableStateFlow(false)
        coEvery { getBreedsUseCaseMock.execute() } returns Result.failure(Exception())

        val testedClass = buildVM()

        testedClass.stateFlow.test {
            awaitItem().isConnected shouldBe false
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `should return no data state when empty list is received`() = runTest {
        every { networkCheckerMock.state } returns MutableStateFlow(true)
        coEvery { getBreedsUseCaseMock.execute() } returns Result.success(listOf())

        val testedClass = buildVM()

        testScheduler.advanceUntilIdle()

        testedClass.stateFlow.test {
            awaitItem().breedStateType shouldBe DashboardViewContract.BreedStateType.NoData
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `should return correct breed list state when breed list is received`() = runTest {
        every { networkCheckerMock.state } returns MutableStateFlow(true)
        coEvery { getBreedsUseCaseMock.execute() } returns Result.success(
            listOf(
                Breed(name = "hound"),
                Breed(name = "bulldog"),
            )
        )

        val testedClass = buildVM()

        testScheduler.advanceUntilIdle()

        testedClass.stateFlow.test {
            awaitItem().breedStateType shouldBe DashboardViewContract.BreedStateType.Content(
                breedList = persistentListOf(
                    Chip(name = "Hound"),
                    Chip(name = "Bulldog"),
                )
            )
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `should return error state when breed use case returns failure`() = runTest {
        every { networkCheckerMock.state } returns MutableStateFlow(true)
        coEvery { getBreedsUseCaseMock.execute() } returns Result.failure(Exception())

        val testedClass = buildVM()

        testScheduler.advanceUntilIdle()

        testedClass.stateFlow.test {
            awaitItem().breedStateType shouldBe DashboardViewContract.BreedStateType.Error
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `should return selected breed in state when any breed selected`() = runTest {
        every { networkCheckerMock.state } returns MutableStateFlow(true)
        coEvery { getSubBreedsUseCaseMock.execute(any()) } returns Result.failure(Exception())
        coEvery { getBreedsUseCaseMock.execute() } returns Result.success(
            listOf(
                Breed(name = "hound"),
                Breed(name = "bulldog"),
            )
        )

        val testedClass = buildVM()

        testScheduler.advanceUntilIdle()

        testedClass.stateFlow.test {
            testedClass.selectBreed("Hound")
            skipItems(1)

            awaitItem().breedStateType shouldBe DashboardViewContract.BreedStateType.Content(
                breedList = persistentListOf(
                    Chip(name = "Hound", selected = true),
                    Chip(name = "Bulldog"),
                )
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should return correct subBreed list state when a breed selected`() = runTest {
        every { networkCheckerMock.state } returns MutableStateFlow(true)
        coEvery { getBreedsUseCaseMock.execute() } returns Result.success(
            listOf(
                Breed(name = "hound"),
                Breed(name = "bulldog"),
            )
        )
        coEvery { getSubBreedsUseCaseMock.execute("hound") } returns Result.success(
            listOf(
                SubBreed(breed = "hound", name = "afghan"),
                SubBreed(breed = "hound", name = "husky"),
            )
        )

        val testedClass = buildVM()

        testScheduler.advanceUntilIdle()

        testedClass.stateFlow.test {
            testedClass.selectBreed("Hound")

            skipItems(2) // skip initial state

            with(awaitItem()) {
                isDetailButtonActive shouldBe false
                subBreedStateType shouldBe DashboardViewContract.SubBreedStateType.Content(
                    subBreedList = persistentListOf(
                        Chip(name = "Afghan", selected = false),
                        Chip(name = "Husky", selected = false),
                    )
                )
            }

            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `should return no data state when for empty list after a breed selected`() = runTest {
        every { networkCheckerMock.state } returns MutableStateFlow(true)
        coEvery { getBreedsUseCaseMock.execute() } returns Result.success(
            listOf(
                Breed(name = "hound"),
                Breed(name = "bulldog"),
            )
        )
        coEvery { getSubBreedsUseCaseMock.execute("hound") } returns
                Result.success(listOf())

        val testedClass = buildVM()

        testScheduler.advanceUntilIdle()

        testedClass.stateFlow.test {
            testedClass.selectBreed("Hound")

            skipItems(2)

            with(awaitItem()) {
                isDetailButtonActive shouldBe true
                subBreedStateType shouldBe DashboardViewContract.SubBreedStateType.NoData
            }

            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `should return error state when for subBreedUseCase response fails after a breed selected`() =
        runTest {
            every { networkCheckerMock.state } returns MutableStateFlow(true)
            coEvery { getBreedsUseCaseMock.execute() } returns Result.success(
                listOf(
                    Breed(name = "hound"),
                    Breed(name = "bulldog"),
                )
            )
            coEvery { getSubBreedsUseCaseMock.execute("hound") } returns
                    Result.failure(Exception())

            val testedClass = buildVM()

            testScheduler.advanceUntilIdle()

            testedClass.stateFlow.test {
                testedClass.selectBreed("Hound")

                skipItems(2)

                with(awaitItem()) {
                    isDetailButtonActive shouldBe false
                    subBreedStateType shouldBe DashboardViewContract.SubBreedStateType.Error
                }

                ensureAllEventsConsumed()
            }
        }

    @Test
    fun `should return correct subBreed list state when a subBreed selected`() = runTest {
        every { networkCheckerMock.state } returns MutableStateFlow(true)
        coEvery { getBreedsUseCaseMock.execute() } returns Result.success(
            listOf(
                Breed(name = "hound"),
                Breed(name = "bulldog"),
            )
        )
        coEvery { getSubBreedsUseCaseMock.execute("hound") } returns Result.success(
            listOf(
                SubBreed(breed = "hound", name = "afghan"),
                SubBreed(breed = "hound", name = "husky"),
            )
        )

        val testedClass = buildVM()

        testScheduler.advanceUntilIdle()

        testedClass.stateFlow.test {
            testedClass.selectBreed("Hound")
            testedClass.selectSubBreed("Afghan")

            skipItems(3)

            with(awaitItem()) {
                isDetailButtonActive shouldBe true
                breedStateType shouldBe DashboardViewContract.BreedStateType.Content(
                    breedList = persistentListOf(
                        Chip(name = "Hound", selected = true),
                        Chip(name = "Bulldog", selected = false),
                    )
                )
                subBreedStateType shouldBe DashboardViewContract.SubBreedStateType.Content(
                    subBreedList = persistentListOf(
                        Chip(name = "Afghan", selected = true),
                        Chip(name = "Husky", selected = false),
                    )
                )
            }

            ensureAllEventsConsumed()
        }
    }

    private fun buildVM() = DashboardViewModel(
        contextProvider = testContextProvider,
        getBreedsUseCase = getBreedsUseCaseMock,
        getSubBreedsUseCase = getSubBreedsUseCaseMock,
        networkChecker = networkCheckerMock,
    )
}
