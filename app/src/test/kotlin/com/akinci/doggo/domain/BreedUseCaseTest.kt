package com.akinci.doggo.domain

import com.akinci.doggo.core.network.NetworkChecker
import com.akinci.doggo.data.breed.BreedRepository
import com.akinci.doggo.data.breed.local.BreedEntity
import com.akinci.doggo.data.breed.remote.BreedListServiceResponse
import com.akinci.doggo.domain.breed.BreedUseCase
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

    private lateinit var testedClass: BreedUseCase

    @BeforeEach
    fun setup() {
        coEvery { breedRepositoryMock.getBreeds() } returns Result.success(
            BreedListServiceResponse(
                message = mapOf(
                    "hound" to listOf("afghan", "husky"),
                    "bulldog" to listOf("hulk", "bunny")
                )
            )
        )

        coEvery { breedRepositoryMock.getLocalBreeds() } returns Result.success(
            listOf(
                BreedEntity(name = "kangal"),
                BreedEntity(name = "pitbull")
            )
        )

        testedClass = BreedUseCase(
            breedRepository = breedRepositoryMock,
            networkChecker = networkCheckerMock,
        )
    }

    @Test
    fun `should return remote results when connected to network`() = runTest {
        every { networkCheckerMock.isConnected() } returns true

        val result = testedClass.getBreeds()

        result.isSuccess shouldBe true
        result.getOrNull()!!.let {
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

        testedClass.getBreeds()

        coVerify(exactly = 1) { breedRepositoryMock.insert(any()) }
    }

    @Test
    fun `should return failure when repository call fails with connected network`() = runTest {
        every { networkCheckerMock.isConnected() } returns true
        coEvery { breedRepositoryMock.getBreeds() } returns Result.failure(Exception())

        val result = testedClass.getBreeds()

        result.isFailure shouldBe true
    }

    @Test
    fun `should return local results when not connected to network`() = runTest {
        every { networkCheckerMock.isConnected() } returns false

        val result = testedClass.getBreeds()

        result.isSuccess shouldBe true
        result.getOrNull()!!.let {
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

        val result = testedClass.getBreeds()

        result.isFailure shouldBe true
    }
}
