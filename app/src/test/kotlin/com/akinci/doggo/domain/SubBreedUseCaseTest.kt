package com.akinci.doggo.domain

import com.akinci.doggo.core.network.NetworkChecker
import com.akinci.doggo.data.subbreed.SubBreedRepository
import com.akinci.doggo.data.subbreed.local.SubBreedEntity
import com.akinci.doggo.data.subbreed.remote.SubBreedListServiceResponse
import com.akinci.doggo.domain.subBreed.SubBreedUseCase
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SubBreedUseCaseTest {

    private val subBreedRepositoryMock: SubBreedRepository = mockk(relaxed = true)
    private val networkCheckerMock: NetworkChecker = mockk(relaxed = true)

    private lateinit var testedClass: SubBreedUseCase

    @BeforeEach
    fun setup() {
        coEvery { subBreedRepositoryMock.getSubBreeds(breed = "hound") } returns Result.success(
            SubBreedListServiceResponse(
                message = listOf("afghan", "husky")
            )
        )

        coEvery { subBreedRepositoryMock.getLocalSubBreeds(breed = "hound") } returns Result.success(
            listOf(
                SubBreedEntity(breed = "hound", name = "kangal"),
                SubBreedEntity(breed = "hound", name = "pitbull")
            )
        )

        testedClass = SubBreedUseCase(
            subBreedRepository = subBreedRepositoryMock,
            networkChecker = networkCheckerMock,
        )
    }

    @Test
    fun `should return remote results when connected to network`() = runTest {
        every { networkCheckerMock.isConnected() } returns true

        val result = testedClass.getSubBreeds(breed = "hound")

        result.isSuccess shouldBe true
        result.getOrNull()!!.let {
            it.size shouldBe 2
            it[0].breed shouldBe "hound"
            it[0].name shouldBe "afghan"
            it[1].breed shouldBe "hound"
            it[1].name shouldBe "husky"
        }

        coVerify(exactly = 1) { subBreedRepositoryMock.getSubBreeds(any()) }
        coVerify(exactly = 0) { subBreedRepositoryMock.getLocalSubBreeds(any()) }
    }

    @Test
    fun `should save new subBreeds into local db while fetching from endpoint`() = runTest {
        every { networkCheckerMock.isConnected() } returns true

        testedClass.getSubBreeds(breed = "hound")

        coVerify(exactly = 1) { subBreedRepositoryMock.insert(any()) }
    }

    @Test
    fun `should return failure when repository call fails with connected network`() = runTest {
        every { networkCheckerMock.isConnected() } returns true
        coEvery { subBreedRepositoryMock.getSubBreeds(any()) } returns Result.failure(Exception())

        val result = testedClass.getSubBreeds(breed = "hound")

        result.isFailure shouldBe true
    }

    @Test
    fun `should return local results when not connected to network`() = runTest {
        every { networkCheckerMock.isConnected() } returns false

        val result = testedClass.getSubBreeds(breed = "hound")

        result.isSuccess shouldBe true
        result.getOrNull()!!.let {
            it.size shouldBe 2
            it[0].breed shouldBe "hound"
            it[0].name shouldBe "kangal"
            it[1].breed shouldBe "hound"
            it[1].name shouldBe "pitbull"
        }

        coVerify(exactly = 0) { subBreedRepositoryMock.getSubBreeds(any()) }
        coVerify(exactly = 1) { subBreedRepositoryMock.getLocalSubBreeds(any()) }
    }

    @Test
    fun `should return failure when local db call fails without connected network`() = runTest {
        every { networkCheckerMock.isConnected() } returns false
        coEvery { subBreedRepositoryMock.getLocalSubBreeds(any()) } returns
                Result.failure(Exception())

        val result = testedClass.getSubBreeds(breed = "hound")

        result.isFailure shouldBe true
    }
}
