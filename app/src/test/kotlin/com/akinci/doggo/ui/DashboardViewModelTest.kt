package com.akinci.doggo.ui

import app.cash.turbine.test
import com.akinci.doggo.core.coroutine.MainDispatcherRule
import com.akinci.doggo.core.coroutine.TestContextProvider
import com.akinci.doggo.core.network.NetworkChecker
import com.akinci.doggo.domain.breed.Breed
import com.akinci.doggo.domain.breed.BreedUseCase
import com.akinci.doggo.domain.subBreed.SubBreed
import com.akinci.doggo.domain.subBreed.SubBreedUseCase
import com.akinci.doggo.ui.features.dashboard.DashboardViewModel
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.time.Duration

@ExtendWith(MainDispatcherRule::class)
class DashboardViewModelTest {

    private val testContextProvider = TestContextProvider()
    private val breedUseCaseMock: BreedUseCase = mockk(relaxed = true)
    private val subBreedUseCaseMock: SubBreedUseCase = mockk(relaxed = true)
    private val networkCheckerMock: NetworkChecker = mockk(relaxed = true)

    @Test
    fun `should return correct network state when device is connected`() = runTest {
        every { networkCheckerMock.state } returns MutableStateFlow(true)
        coEvery { breedUseCaseMock.getBreeds() } returns Result.failure(Exception())

        val testedClass = buildVM()

        testedClass.stateFlow.test {
            with(awaitItem()) {
                isConnected shouldBe true
            }

            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `should return correct network state when device is not connected`() = runTest {
        every { networkCheckerMock.state } returns MutableStateFlow(false)
        coEvery { breedUseCaseMock.getBreeds() } returns Result.failure(Exception())

        val testedClass = buildVM()

        testedClass.stateFlow.test {
            with(awaitItem()) {
                isConnected shouldBe false
            }

            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `should return no data state when empty list is received`() = runTest {
        every { networkCheckerMock.state } returns MutableStateFlow(true)
        coEvery { breedUseCaseMock.getBreeds() } returns Result.success(listOf())

        val testedClass = buildVM()

        testedClass.stateFlow.test(timeout = Duration.INFINITE) {
            skipItems(1) // skip initial state

            with(awaitItem()) {
                isBreedLoading shouldBe true
                isBreedNoData shouldBe true
                isBreedError shouldBe false
            }
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `should return correct breed list state when breed list is received`() = runTest {
        every { networkCheckerMock.state } returns MutableStateFlow(true)
        coEvery { breedUseCaseMock.getBreeds() } returns Result.success(
            listOf(
                Breed(name = "hound"),
                Breed(name = "bulldog"),
            )
        )

        val testedClass = buildVM()

        testedClass.stateFlow.test(timeout = Duration.INFINITE) {
            skipItems(1) // skip initial state

            with(awaitItem()) {
                breedList.size shouldBeGreaterThan 0
                breedList[0].name shouldBe "Hound"
                isBreedLoading shouldBe false
                isBreedNoData shouldBe false
                isBreedError shouldBe false
            }
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `should return error state when breed use case returns failure`() = runTest {
        every { networkCheckerMock.state } returns MutableStateFlow(true)
        coEvery { breedUseCaseMock.getBreeds() } returns Result.failure(Exception())

        val testedClass = buildVM()

        testedClass.stateFlow.test(timeout = Duration.INFINITE) {
            skipItems(1) // skip initial state

            with(awaitItem()) {
                isBreedLoading shouldBe false
                isBreedNoData shouldBe false
                isBreedError shouldBe true
            }
            ensureAllEventsConsumed()
        }
    }


    @Test
    fun `should return selected breed in state when any breed selected`() = runTest {
        every { networkCheckerMock.state } returns MutableStateFlow(true)
        coEvery { subBreedUseCaseMock.getSubBreeds(any()) } returns Result.failure(Exception())
        coEvery { breedUseCaseMock.getBreeds() } returns Result.success(
            listOf(
                Breed(name = "hound"),
                Breed(name = "bulldog"),
            )
        )

        val testedClass = buildVM()

        testedClass.stateFlow.test(timeout = Duration.INFINITE) {
            skipItems(1) // skip initial state
            skipItems(1) // skip breed list state

            testedClass.selectBreed("Hound")

            with(awaitItem()) {
                selectedBreed shouldBe "Hound"
                breedList.firstOrNull { it.selected }?.name shouldBe "Hound"
            }

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should return correct subBreed list state when a breed selected`() = runTest {
        every { networkCheckerMock.state } returns MutableStateFlow(true)
        coEvery { breedUseCaseMock.getBreeds() } returns Result.success(
            listOf(
                Breed(name = "hound"),
                Breed(name = "bulldog"),
            )
        )
        coEvery { subBreedUseCaseMock.getSubBreeds("hound") } returns Result.success(
            listOf(
                SubBreed(breed = "hound", name = "afghan"),
                SubBreed(breed = "hound", name = "husky"),
            )
        )

        val testedClass = buildVM()

        testedClass.stateFlow.test(timeout = Duration.INFINITE) {
            skipItems(1) // skip initial state
            skipItems(1) // skip breed list state

            testedClass.selectBreed("Hound")

            skipItems(1) // skip breed selection state

            with(awaitItem()) {
                isDetailButtonActive shouldBe false
                isSubBreedError shouldBe false
                selectedSubBreed shouldBe null
                subBreedList.size shouldBeGreaterThan 0
            }

            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `should return no data state when for empty list after a breed selected`() = runTest {
        every { networkCheckerMock.state } returns MutableStateFlow(true)
        coEvery { breedUseCaseMock.getBreeds() } returns Result.success(
            listOf(
                Breed(name = "hound"),
                Breed(name = "bulldog"),
            )
        )
        coEvery { subBreedUseCaseMock.getSubBreeds("hound") } returns
                Result.success(listOf())

        val testedClass = buildVM()

        testedClass.stateFlow.test(timeout = Duration.INFINITE) {
            skipItems(1) // skip initial state
            skipItems(1) // skip breed list state

            testedClass.selectBreed("Hound")

            skipItems(1) // skip breed selection state

            with(awaitItem()) {
                isDetailButtonActive shouldBe true
                isSubBreedError shouldBe false
                selectedSubBreed shouldBe null
                subBreedList.size shouldBe 0
            }

            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `should return error state when for subBreedUseCase response fails after a breed selected`() =
        runTest {
            every { networkCheckerMock.state } returns MutableStateFlow(true)
            coEvery { breedUseCaseMock.getBreeds() } returns Result.success(
                listOf(
                    Breed(name = "hound"),
                    Breed(name = "bulldog"),
                )
            )
            coEvery { subBreedUseCaseMock.getSubBreeds("hound") } returns
                    Result.failure(Exception())

            val testedClass = buildVM()

            testedClass.stateFlow.test(timeout = Duration.INFINITE) {
                skipItems(1) // skip initial state
                skipItems(1) // skip breed list state

                testedClass.selectBreed("Hound")

                skipItems(1) // skip breed selection state

                with(awaitItem()) {
                    isDetailButtonActive shouldBe false
                    isSubBreedError shouldBe true
                    selectedSubBreed shouldBe null
                    subBreedList.size shouldBe 0
                }

                ensureAllEventsConsumed()
            }
        }

    @Test
    fun `should return correct subBreed list state when a subBreed selected`() =
        runTest {
            every { networkCheckerMock.state } returns MutableStateFlow(true)
            coEvery { breedUseCaseMock.getBreeds() } returns Result.success(
                listOf(
                    Breed(name = "hound"),
                    Breed(name = "bulldog"),
                )
            )
            coEvery { subBreedUseCaseMock.getSubBreeds("hound") } returns Result.success(
                listOf(
                    SubBreed(breed = "hound", name = "afghan"),
                    SubBreed(breed = "hound", name = "husky"),
                )
            )

            val testedClass = buildVM()

            testedClass.stateFlow.test(timeout = Duration.INFINITE) {
                skipItems(1) // skip initial state
                skipItems(1) // skip breed list state

                testedClass.selectBreed("Hound")

                skipItems(1) // skip breed selection state
                skipItems(1) // skip subBreed list state

                testedClass.selectSubBreed("Afghan")

                with(awaitItem()) {
                    isDetailButtonActive shouldBe true
                    selectedBreed shouldBe "Hound"
                    selectedSubBreed shouldBe "Afghan"
                    breedList.firstOrNull { it.selected }?.name shouldBe "Hound"
                    subBreedList.firstOrNull { it.selected }?.name shouldBe "Afghan"
                }

                ensureAllEventsConsumed()
            }
        }


    private fun buildVM() = DashboardViewModel(
        contextProvider = testContextProvider,
        breedUseCase = breedUseCaseMock,
        subBreedUseCase = subBreedUseCaseMock,
        networkChecker = networkCheckerMock,
    )
}
