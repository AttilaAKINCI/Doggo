package com.akinci.doggo.domain

import com.akinci.doggo.core.network.NetworkChecker
import com.akinci.doggo.data.breed.local.BreedEntity
import com.akinci.doggo.data.breed.remote.BreedListServiceResponse
import com.akinci.doggo.data.image.ImageRepository
import com.akinci.doggo.data.image.local.ImageEntity
import com.akinci.doggo.data.image.remote.ImageServiceResponse
import com.akinci.doggo.domain.data.ImagesUseCase
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ImageUseCaseTest {

    private val imageRepositoryMock: ImageRepository = mockk(relaxed = true)
    private val networkCheckerMock: NetworkChecker = mockk(relaxed = true)
    private val dogNameProviderMock: DogNameProvider = mockk(relaxed = true)

    private lateinit var testedClass: ImagesUseCase

    @BeforeEach
    fun setup() {
        every { dogNameProviderMock.getRandomDogName() } returns "Afro"

        coEvery { imageRepositoryMock.getImage(any(), any()) } returns Result.success(
            ImageServiceResponse(
                message = listOf("imageUrl1","imageUrl2","imageUrl3")
            )
        )

        coEvery { imageRepositoryMock.getLocalImages(any(), any()) } returns Result.success(
            listOf(
                ImageEntity(breed = "hound", subBreed = "afghan", imageUrl = "imageUrl1"),
                ImageEntity(breed = "hound", subBreed = "afghan", imageUrl = "imageUrl2"),
                ImageEntity(breed = "hound", subBreed = "afghan", imageUrl = "imageUrl3"),
            )
        )

        testedClass = ImagesUseCase(
            imageRepository = imageRepositoryMock,
            networkChecker = networkCheckerMock,
            dogNameProvider = dogNameProviderMock,
        )
    }

    @Test
    fun `should return remote results when connected to network`() = runTest {
        every { networkCheckerMock.isConnected() } returns true

        val result = testedClass.getImage(breed = "hound", subBreed = "afghan")

        result.isSuccess shouldBe true
        result.getOrNull()!!.let {
            it.size shouldBe 3
            it[0].breed shouldBe "hound"
            it[0].subBreed shouldBe "afghan"
            it[0].imageUrl shouldBe "imageUrl1"
            it[0].dogName shouldBe "Afro"
            it[1].imageUrl shouldBe "imageUrl2"
            it[2].imageUrl shouldBe "imageUrl3"
        }

        coVerify(exactly = 1) { imageRepositoryMock.getImage(any(), any()) }
        coVerify(exactly = 0) { imageRepositoryMock.getLocalImages(any(), any()) }
    }

    @Test
    fun `should save new images into local db while fetching from endpoint`() = runTest {
        every { networkCheckerMock.isConnected() } returns true

        testedClass.getImage(breed = "hound", subBreed = "afghan")

        coVerify(exactly = 1) { imageRepositoryMock.insert(any()) }
    }

    @Test
    fun `should return failure when repository call fails with connected network`() = runTest {
        every { networkCheckerMock.isConnected() } returns true
        coEvery { imageRepositoryMock.getImage(any(), any()) } returns Result.failure(Exception())

        val result = testedClass.getImage(breed = "hound", subBreed = "afghan")

        result.isFailure shouldBe true
    }

    @Test
    fun `should return local results when not connected to network`() = runTest {
        every { networkCheckerMock.isConnected() } returns false

        val result = testedClass.getImage(breed = "hound", subBreed = "afghan")

        result.isSuccess shouldBe true
        result.getOrNull()!!.let {
            it.size shouldBe 3
            it[0].imageUrl shouldBe "imageUrl1"
            it[1].imageUrl shouldBe "imageUrl2"
            it[2].imageUrl shouldBe "imageUrl3"
        }

        coVerify(exactly = 0) { imageRepositoryMock.getImage(any(), any()) }
        coVerify(exactly = 1) { imageRepositoryMock.getLocalImages(any(), any()) }
    }

    @Test
    fun `should return failure when local db call fails without connected network`() = runTest {
        every { networkCheckerMock.isConnected() } returns false
        coEvery { imageRepositoryMock.getLocalImages(any(), any()) } returns
                Result.failure(Exception())

        val result = testedClass.getImage(breed = "hound", subBreed = "afghan")

        result.isFailure shouldBe true
    }
}
