package com.akinci.doggo.domain

import com.akinci.doggo.core.network.NetworkChecker
import com.akinci.doggo.data.repository.ImageRepository
import com.akinci.doggo.domain.data.Image
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe
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

    private lateinit var testedClass: GetImagesUseCase

    @BeforeEach
    fun setup() {
        every { dogNameProviderMock.getRandomDogName() } returns "Afro"

        coEvery { imageRepositoryMock.getImage(any(), any()) } returns Result.success(
            listOf(
                Image(breed = "hound", subBreed = "afghan", imageUrl = "rest_imageUrl1"),
                Image(breed = "hound", subBreed = "afghan", imageUrl = "rest_imageUrl2"),
                Image(breed = "hound", subBreed = "afghan", imageUrl = "rest_imageUrl3"),
            )
        )

        coEvery { imageRepositoryMock.getLocalImages(any(), any()) } returns Result.success(
            listOf(
                Image(breed = "hound", subBreed = "afghan", imageUrl = "local_imageUrl1"),
                Image(breed = "hound", subBreed = "afghan", imageUrl = "local_imageUrl2"),
                Image(breed = "hound", subBreed = "afghan", imageUrl = "local_imageUrl3"),
            )
        )

        testedClass = GetImagesUseCase(
            imageRepository = imageRepositoryMock,
            networkChecker = networkCheckerMock,
            dogNameProvider = dogNameProviderMock,
        )
    }

    @Test
    fun `should return remote results when connected to network`() = runTest {
        every { networkCheckerMock.isConnected() } returns true

        val result = testedClass.execute(breed = "hound", subBreed = "afghan")

        result shouldBeSuccess {
            it.size shouldBe 3
            it[0].breed shouldBe "hound"
            it[0].subBreed shouldBe "afghan"
            it[0].imageUrl shouldBe "rest_imageUrl1"
            it[0].dogName shouldBe "Afro"
            it[1].imageUrl shouldBe "rest_imageUrl2"
            it[2].imageUrl shouldBe "rest_imageUrl3"
        }

        coVerify(exactly = 1) { imageRepositoryMock.getImage(any(), any()) }
        coVerify(exactly = 0) { imageRepositoryMock.getLocalImages(any(), any()) }
    }

    @Test
    fun `should save new images into local db while fetching from endpoint`() = runTest {
        every { networkCheckerMock.isConnected() } returns true

        testedClass.execute(breed = "hound", subBreed = "afghan")

        coVerify(exactly = 1) { imageRepositoryMock.insert(any()) }
    }

    @Test
    fun `should return failure when repository call fails with connected network`() = runTest {
        every { networkCheckerMock.isConnected() } returns true
        coEvery { imageRepositoryMock.getImage(any(), any()) } returns Result.failure(Exception())

        val result = testedClass.execute(breed = "hound", subBreed = "afghan")

        result.isFailure shouldBe true
    }

    @Test
    fun `should return local results when not connected to network`() = runTest {
        every { networkCheckerMock.isConnected() } returns false

        val result = testedClass.execute(breed = "hound", subBreed = "afghan")

        result shouldBeSuccess {
            it.size shouldBe 3
            it[0].imageUrl shouldBe "local_imageUrl1"
            it[1].imageUrl shouldBe "local_imageUrl2"
            it[2].imageUrl shouldBe "local_imageUrl3"
        }

        coVerify(exactly = 0) { imageRepositoryMock.getImage(any(), any()) }
        coVerify(exactly = 1) { imageRepositoryMock.getLocalImages(any(), any()) }
    }

    @Test
    fun `should return failure when local db call fails without connected network`() = runTest {
        every { networkCheckerMock.isConnected() } returns false
        coEvery { imageRepositoryMock.getLocalImages(any(), any()) } returns
                Result.failure(Exception())

        val result = testedClass.execute(breed = "hound", subBreed = "afghan")

        result.isFailure shouldBe true
    }
}
