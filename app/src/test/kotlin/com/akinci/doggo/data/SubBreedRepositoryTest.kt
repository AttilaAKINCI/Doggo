package com.akinci.doggo.data

import com.akinci.doggo.core.application.AppConfig
import com.akinci.doggo.core.network.HttpClientFactory
import com.akinci.doggo.core.network.HttpEngineFactoryMock
import com.akinci.doggo.data.repository.SubBreedRepository
import com.akinci.doggo.data.room.subbreed.SubBreedDao
import com.akinci.doggo.data.room.subbreed.SubBreedEntity
import com.akinci.doggo.domain.data.SubBreed
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe
import io.ktor.http.HttpStatusCode
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SubBreedRepositoryTest {

    private val appConfigMock: AppConfig = mockk(relaxed = true)
    private val subBreedDaoMock: SubBreedDao = mockk(relaxed = true)

    private val httpEngineFactory = HttpEngineFactoryMock()
    private val httpClientFactory = HttpClientFactory(
        httpEngineFactory = httpEngineFactory,
        appConfig = appConfigMock,
    )

    private lateinit var testedClass: SubBreedRepository

    @BeforeEach
    fun setup() {
        testedClass = SubBreedRepository(
            subBreedDao = subBreedDaoMock,
            httpClient = httpClientFactory.create()
        )
    }

    @Test
    fun `should return success when subBreeds are successfully inserted`() = runTest {
        coEvery { subBreedDaoMock.insertSubBreeds(any()) } returns Unit

        val subBreeds = listOf(
            SubBreedEntity(breed = "Hound", name = "Afghan")
        )

        val result = testedClass.insert(subBreeds)

        result.isSuccess shouldBe true
    }

    @Test
    fun `should return failure when room db fails on subBreed insert`() = runTest {
        coEvery { subBreedDaoMock.insertSubBreeds(any()) } throws Exception()

        val subBreeds = listOf(
            SubBreedEntity(breed = "Hound", name = "Afghan")
        )

        val result = testedClass.insert(subBreeds)

        result.isFailure shouldBe true
    }

    @Test
    fun `should return local subBreeds when there is a locally saved subBreeds`() = runTest {
        val breed = "Hound"
        coEvery { subBreedDaoMock.getAllSubBreeds(breed) } returns listOf(
            SubBreedEntity(breed = "Hound", name = "Afghan"),
            SubBreedEntity(breed = "Hound", name = "Husky")
        )

        val result = testedClass.getLocalSubBreeds(breed = breed)

        result shouldBeSuccess {
            it.size shouldBe 2
            it[0].breed shouldBe "Hound"
            it[0].name shouldBe "Afghan"
            it[1].breed shouldBe "Hound"
            it[1].name shouldBe "Husky"
        }
    }

    @Test
    fun `should return failure when exception occurred during subBreed fetch room query`() =
        runTest {
            val breed = "Hound"
            coEvery { subBreedDaoMock.getAllSubBreeds(breed) } throws Exception()

            val result = testedClass.getLocalSubBreeds(breed)

            result.isFailure shouldBe true
        }

    @Test
    fun `should get data when successful response from network`() = runTest {
        val breed = "Hound"
        httpEngineFactory.statusCode = HttpStatusCode.OK

        val result = testedClass.getSubBreeds(breed)

        result shouldBeSuccess {
            it shouldContain SubBreed(breed = "Hound", name = "afghan")
            it shouldContain SubBreed(breed = "Hound", name = "basset")
            it shouldContain SubBreed(breed = "Hound", name = "blood")
            it shouldContain SubBreed(breed = "Hound", name = "english")
            it shouldContain SubBreed(breed = "Hound", name = "ibizan")
            it shouldContain SubBreed(breed = "Hound", name = "plott")
            it shouldContain SubBreed(breed = "Hound", name = "walker")
        }
    }

    @Test
    fun `should return failure when unsuccessful http response`() = runTest {
        val breed = "Hound"
        httpEngineFactory.statusCode = HttpStatusCode.NotFound

        val result = testedClass.getSubBreeds(breed)

        result.isFailure shouldBe true
    }

    @Test
    fun `should return failure when unknown exception during network request`() = runTest {
        val breed = "Hound"
        httpEngineFactory.simulateException = true

        val result = testedClass.getSubBreeds(breed)

        result shouldBeFailure {
            it.message shouldBe "Simulated Network Exception"
        }
    }
}
