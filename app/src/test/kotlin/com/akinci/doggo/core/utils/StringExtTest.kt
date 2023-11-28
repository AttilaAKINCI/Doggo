package com.akinci.doggo.core.utils

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class StringExtTest {

    @Test
    fun `should not change when given string is capitalised`() {
        val stringToTest = "Hound"

        val result = stringToTest.capitalise()

        result shouldBe "Hound"
    }

    @Test
    fun `should capitalise when given string is lower cased`() {
        val stringToTest = "hound"

        val result = stringToTest.capitalise()

        result shouldBe "Hound"
    }
}