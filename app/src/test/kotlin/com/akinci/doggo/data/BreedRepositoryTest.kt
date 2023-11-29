package com.akinci.doggo.data

import com.akinci.doggo.core.application.AppConfig
import com.akinci.doggo.core.network.HttpClientFactory
import com.akinci.doggo.core.network.HttpEngineFactoryMock
import com.akinci.doggo.core.storage.AppDatabase
import com.akinci.doggo.data.breed.BreedRepository
import com.akinci.doggo.data.breed.local.BreedDao
import com.akinci.doggo.data.breed.local.BreedEntity
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import io.ktor.http.HttpStatusCode
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class BreedRepositoryTest {

    private val appDatabaseMock: AppDatabase = mockk(relaxed = true)
    private val appConfigMock: AppConfig = mockk(relaxed = true)

    private val httpEngineFactory = HttpEngineFactoryMock()
    private val httpClientFactory = HttpClientFactory(
        httpEngineFactory = httpEngineFactory,
        appConfig = appConfigMock,
    )

    private lateinit var testedClass: BreedRepository
    private lateinit var dao: BreedDao

    @BeforeEach
    fun setup() {
        dao = appDatabaseMock.getBreedDao()

        testedClass = BreedRepository(
            appDatabase = appDatabaseMock,
            httpClient = httpClientFactory.create()
        )
    }

    @Test
    fun `should return success when breeds are successfully inserted`() = runTest {
        coEvery { dao.insertBreeds(any()) } returns Unit

        val breeds = listOf(BreedEntity(name = "Hound"))

        val result = testedClass.insert(breeds)

        result.isSuccess shouldBe true
    }

    @Test
    fun `should return failure when room db fails on breed insert`() = runTest {
        coEvery { dao.insertBreeds(any()) } throws Exception()

        val breeds = listOf(BreedEntity(name = "Hound"))

        val result = testedClass.insert(breeds)

        result.isFailure shouldBe true
    }

    @Test
    fun `should return local breeds when there is a locally saved breeds`() = runTest {
        coEvery { dao.getAllBreeds() } returns listOf(
            BreedEntity(name = "Hound"),
            BreedEntity(name = "Bulldog")
        )

        val result = testedClass.getLocalBreeds()

        result.isSuccess shouldBe true
        result.getOrNull()!!.let {
            it.size shouldBe 2
            it[0].name shouldBe "Hound"
            it[1].name shouldBe "Bulldog"
        }
    }

    @Test
    fun `should return failure when exception occurred during breed fetch room query`() = runTest {
        coEvery { dao.getAllBreeds() } throws Exception()

        val result = testedClass.getLocalBreeds()

        result.isFailure shouldBe true
    }

    @Test
    fun `should get data when successful response from network`() = runTest {
        httpEngineFactory.statusCode = HttpStatusCode.OK

        val result = testedClass.getBreeds()

        result.isSuccess shouldBe true
        result.getOrNull()!!.let {
            it.message.keys shouldContain "bakharwal"
            it.message.keys shouldContain "australian"
            it.message.keys shouldContain "appenzeller"
            it.message.keys shouldContain "akita"
            it.message.keys shouldContain "airedale"
            it.message.keys shouldContain "african"
            it.message.keys shouldContain "affenpinscher"
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

        result.isFailure shouldBe true
        result.exceptionOrNull()!!.let {
            it.message shouldBe "Simulated Network Exception"
        }
    }
}
