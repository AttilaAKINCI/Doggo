package com.akinci.doggo.domain

import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DogNameProviderTest {

    private lateinit var testedClass: DogNameProvider

    @BeforeEach
    fun setup() {
        testedClass = DogNameProvider()
    }

    @Test
    fun `should return random dog name when `() = runTest {
        val result = testedClass.getRandomDogName()

        result shouldNotBe null
        result shouldNotBe ""
    }
}