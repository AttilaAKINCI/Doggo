package com.akinci.doggo.data

import com.akinci.doggo.core.application.AppConfig
import com.akinci.doggo.core.network.HttpClientFactory
import com.akinci.doggo.core.network.HttpEngineFactoryMock
import com.akinci.doggo.data.repository.ImageRepository
import com.akinci.doggo.data.room.image.ImageDao
import com.akinci.doggo.data.room.image.ImageEntity
import com.akinci.doggo.domain.data.Image
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe
import io.ktor.http.HttpStatusCode
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ImageRepositoryTest {

    private val appConfigMock: AppConfig = mockk(relaxed = true)
    private val imageDaoMock: ImageDao = mockk(relaxed = true)

    private val httpEngineFactory = HttpEngineFactoryMock()
    private val httpClientFactory = HttpClientFactory(
        httpEngineFactory = httpEngineFactory,
        appConfig = appConfigMock,
    )

    private lateinit var testedClass: ImageRepository

    @BeforeEach
    fun setup() {
        testedClass = ImageRepository(
            imageDao = imageDaoMock,
            httpClient = httpClientFactory.create()
        )
    }

    @Test
    fun `should return success when images are successfully inserted `() = runTest {
        coEvery { imageDaoMock.insertImages(any()) } returns Unit

        val images = listOf(
            ImageEntity(breed = "Hound", subBreed = "Afghan", imageUrl = "sampleImageUrl1"),
            ImageEntity(breed = "Hound", subBreed = "Afghan", imageUrl = "sampleImageUrl2")
        )

        val result = testedClass.insert(images)

        result.isSuccess shouldBe true
    }

    @Test
    fun `should return failure when room db fails on breed insert`() = runTest {
        coEvery { imageDaoMock.insertImages(any()) } throws Exception()

        val images = listOf(
            ImageEntity(breed = "Hound", subBreed = "Afghan", imageUrl = "sampleImageUrl1"),
            ImageEntity(breed = "Hound", subBreed = "Afghan", imageUrl = "sampleImageUrl2")
        )

        val result = testedClass.insert(images)

        result.isFailure shouldBe true
    }

    @Test
    fun `should return local images when without subBreed`() = runTest {
        coEvery { imageDaoMock.getImages(any()) } returns listOf(
            ImageEntity(breed = "Hound", subBreed = "Afghan", imageUrl = "sampleImageUrl1"),
            ImageEntity(breed = "Hound", subBreed = "Afghan", imageUrl = "sampleImageUrl2"),
            ImageEntity(breed = "Hound", subBreed = "Husky", imageUrl = "sampleImageUrl3")
        )

        val result = testedClass.getLocalImages(breed = "Hound", subBreed = null)

        coVerify(exactly = 0) { imageDaoMock.getImages(any(), any()) }
        result shouldBeSuccess {
            it.size shouldBe 3
            it[2].breed shouldBe "Hound"
            it[2].subBreed shouldBe "Husky"
            it[2].imageUrl shouldBe "sampleImageUrl3"
        }
    }

    @Test
    fun `should return failure when fetching local images without subBreed`() = runTest {
        coEvery { imageDaoMock.getImages(any()) } throws Exception()

        val result = testedClass.getLocalImages(breed = "Hound", subBreed = null)

        coVerify(exactly = 0) { imageDaoMock.getImages(any(), any()) }
        result.isFailure shouldBe true
    }

    @Test
    fun `should return local images when with subBreed`() = runTest {
        coEvery { imageDaoMock.getImages(any(), any()) } returns listOf(
            ImageEntity(breed = "Hound", subBreed = "Afghan", imageUrl = "sampleImageUrl1"),
            ImageEntity(breed = "Hound", subBreed = "Afghan", imageUrl = "sampleImageUrl2"),
            ImageEntity(breed = "Hound", subBreed = "Husky", imageUrl = "sampleImageUrl3")
        )

        val result = testedClass.getLocalImages(breed = "Hound", subBreed = "Afghan")

        coVerify(exactly = 0) { imageDaoMock.getImages(any()) }
        result shouldBeSuccess {
            it.size shouldBe 3
            it[2].breed shouldBe "Hound"
            it[2].subBreed shouldBe "Husky"
            it[2].imageUrl shouldBe "sampleImageUrl3"
        }
    }

    @Test
    fun `should return failure when fetching local images with subBreed`() = runTest {
        coEvery { imageDaoMock.getImages(any(), any()) } throws Exception()

        val result = testedClass.getLocalImages(breed = "Hound", subBreed = "Afghan")

        coVerify(exactly = 0) { imageDaoMock.getImages(any()) }
        result.isFailure shouldBe true
    }

    @Test
    fun `should get data when successful response from network`() = runTest {
        httpEngineFactory.statusCode = HttpStatusCode.OK

        val result = testedClass.getImage(breed = "Hound", subBreed = "Afghan")

        result shouldBeSuccess {
            it.size shouldBe 1
            it shouldContain Image(
                breed = "Hound",
                subBreed = "Afghan",
                imageUrl = "https://images.dog.ceo/breeds/hound-afghan/afghan.jpg"
            )
        }
    }

    @Test
    fun `should return failure when unsuccessful http response`() = runTest {
        httpEngineFactory.statusCode = HttpStatusCode.NotFound

        val result = testedClass.getImage(breed = "Hound", subBreed = "Afghan")

        result.isFailure shouldBe true
    }

    @Test
    fun `should return failure when unknown exception during network request`() = runTest {
        httpEngineFactory.simulateException = true

        val result = testedClass.getImage(breed = "Hound", subBreed = "Afghan")

        result shouldBeFailure {
            it.message shouldBe "Simulated Network Exception"
        }
    }
}
