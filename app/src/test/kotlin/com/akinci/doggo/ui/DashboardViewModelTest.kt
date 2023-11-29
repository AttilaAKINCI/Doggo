package com.akinci.doggo.ui

import com.akinci.doggo.core.coroutine.MainDispatcherRule
import com.akinci.doggo.core.coroutine.TestContextProvider
import com.akinci.doggo.core.network.NetworkChecker
import com.akinci.doggo.domain.breed.BreedUseCase
import com.akinci.doggo.domain.subBreed.SubBreedUseCase
import com.akinci.doggo.ui.features.dashboard.DashboardViewModel
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MainDispatcherRule::class)
class DashboardViewModelTest {

    private val testContextProvider = TestContextProvider()
    private val breedUseCaseMock: BreedUseCase = mockk(relaxed = true)
    private val subBreedUseCaseMock: SubBreedUseCase = mockk(relaxed = true)
    private val networkCheckerMock: NetworkChecker = mockk(relaxed = true)

    private lateinit var testedClass: DashboardViewModel

    @BeforeEach
    fun setup() {
        testedClass = DashboardViewModel(
            contextProvider = testContextProvider,
            breedUseCase = breedUseCaseMock,
            subBreedUseCase = subBreedUseCaseMock,
            networkChecker = networkCheckerMock,
        )
    }

    @Test
    fun `should e`() = runTest {
        // TODO
    }
}
