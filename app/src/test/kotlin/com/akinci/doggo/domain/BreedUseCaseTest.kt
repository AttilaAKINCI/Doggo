package com.akinci.doggo.domain

import com.akinci.doggo.core.network.NetworkChecker
import com.akinci.doggo.data.repository.BreedRepository
import com.akinci.doggo.domain.data.Breed
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class BreedUseCaseTest {

    private val breedRepositoryMock: BreedRepository = mockk(relaxed = true)
    private val networkCheckerMock: NetworkChecker = mockk(relaxed = true)

    private lateinit var testedClass: GetBreedsUseCase

    @BeforeEach
    fun setup() {
        coEvery { breedRepositoryMock.getBreeds() } returns Result.success(
            listOf(
                Breed(name = "hound"),
                Breed(name = "bulldog")
            )
        )

        coEvery { breedRepositoryMock.getLocalBreeds() } returns Result.success(
            listOf(
                Breed(name = "kangal"),
                Breed(name = "pitbull")
            )
        )

        testedClass = GetBreedsUseCase(
            breedRepository = breedRepositoryMock,
            networkChecker = networkCheckerMock,
        )
    }

    @Test
    fun `should return remote results when connected to network`() = runTest {
        every { networkCheckerMock.isConnected() } returns true

        val result = testedClass.execute()

        result shouldBeSuccess {
            it.size shouldBe 2
            it[0].name shouldBe "hound"
            it[1].name shouldBe "bulldog"
        }

        coVerify(exactly = 1) { breedRepositoryMock.getBreeds() }
        coVerify(exactly = 0) { breedRepositoryMock.getLocalBreeds() }
    }

    @Test
    fun `should save new breeds into local db while fetching from endpoint`() = runTest {
        every { networkCheckerMock.isConnected() } returns true

        testedClass.execute()

        coVerify(exactly = 1) { breedRepositoryMock.insert(any()) }
    }

    @Test
    fun `should return failure when repository call fails with connected network`() = runTest {
        every { networkCheckerMock.isConnected() } returns true
        coEvery { breedRepositoryMock.getBreeds() } returns Result.failure(Exception())

        val result = testedClass.execute()

        result.isFailure shouldBe true
    }

    @Test
    fun `should return local results when not connected to network`() = runTest {
        every { networkCheckerMock.isConnected() } returns false

        val result = testedClass.execute()

        result shouldBeSuccess {
            it.size shouldBe 2
            it[0].name shouldBe "kangal"
            it[1].name shouldBe "pitbull"
        }

        coVerify(exactly = 0) { breedRepositoryMock.getBreeds() }
        coVerify(exactly = 1) { breedRepositoryMock.getLocalBreeds() }
    }

    @Test
    fun `should return failure when local db call fails without connected network`() = runTest {
        every { networkCheckerMock.isConnected() } returns false
        coEvery { breedRepositoryMock.getLocalBreeds() } returns Result.failure(Exception())

        val result = testedClass.execute()

        result.isFailure shouldBe true
    }
}
