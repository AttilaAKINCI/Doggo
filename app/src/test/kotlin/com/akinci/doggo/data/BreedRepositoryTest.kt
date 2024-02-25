package com.akinci.doggo.data

import com.akinci.doggo.core.application.AppConfig
import com.akinci.doggo.core.network.HttpClientFactory
import com.akinci.doggo.core.network.HttpEngineFactoryMock
import com.akinci.doggo.data.repository.BreedRepository
import com.akinci.doggo.data.room.breed.BreedDao
import com.akinci.doggo.data.room.breed.BreedEntity
import com.akinci.doggo.domain.data.Breed
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

class BreedRepositoryTest {
    private val appConfigMock: AppConfig = mockk(relaxed = true)
    private val breedDaoMock: BreedDao = mockk(relaxed = true)

    private val httpEngineFactory = HttpEngineFactoryMock()
    private val httpClientFactory = HttpClientFactory(
        httpEngineFactory = httpEngineFactory,
        appConfig = appConfigMock,
    )

    private lateinit var testedClass: BreedRepository

    @BeforeEach
    fun setup() {
        testedClass = BreedRepository(
            breedDao = breedDaoMock,
            httpClient = httpClientFactory.create()
        )
    }

    @Test
    fun `should return success when breeds are successfully inserted`() = runTest {
        coEvery { breedDaoMock.insertBreeds(any()) } returns Unit

        val breeds = listOf(BreedEntity(name = "Hound"))

        val result = testedClass.insert(breeds)

        result.isSuccess shouldBe true
    }

    @Test
    fun `should return failure when room db fails on breed insert`() = runTest {
        coEvery { breedDaoMock.insertBreeds(any()) } throws Exception()

        val breeds = listOf(BreedEntity(name = "Hound"))

        val result = testedClass.insert(breeds)

        result.isFailure shouldBe true
    }

    @Test
    fun `should return local breeds when there is a locally saved breeds`() = runTest {
        coEvery { breedDaoMock.getAllBreeds() } returns listOf(
            BreedEntity(name = "Hound"),
            BreedEntity(name = "Bulldog")
        )

        val result = testedClass.getLocalBreeds()

        result shouldBeSuccess {
            it.size shouldBe 2
            it[0].name shouldBe "Hound"
            it[1].name shouldBe "Bulldog"
        }
    }

    @Test
    fun `should return failure when exception occurred during breed fetch room query`() = runTest {
        coEvery { breedDaoMock.getAllBreeds() } throws Exception()

        val result = testedClass.getLocalBreeds()

        result.isFailure shouldBe true
    }

    @Test
    fun `should get data when successful response from network`() = runTest {
        httpEngineFactory.statusCode = HttpStatusCode.OK

        val result = testedClass.getBreeds()

        result.shouldBeSuccess { breeds ->
            breeds shouldContain Breed(name = "bakharwal")
            breeds shouldContain Breed(name = "australian")
            breeds shouldContain Breed(name = "appenzeller")
            breeds shouldContain Breed(name = "akita")
            breeds shouldContain Breed(name = "airedale")
            breeds shouldContain Breed(name = "african")
            breeds shouldContain Breed(name = "affenpinscher")
        }
    }

    @Test
    fun `should return failure when unsuccessful http response`() = runTest {
        httpEngineFactory.statusCode = HttpStatusCode.NotFound

        val result = testedClass.getBreeds()

        result.isFailure shouldBe true
    }

    @Test
    fun `should return failure when unknown exception during network request`() = runTest {
        httpEngineFactory.simulateException = true

        val result = testedClass.getBreeds()

        result shouldBeFailure {
            it.message shouldBe "Simulated Network Exception"
        }
    }
}
