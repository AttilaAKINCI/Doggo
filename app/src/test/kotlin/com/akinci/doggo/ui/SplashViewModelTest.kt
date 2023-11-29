package com.akinci.doggo.ui

import app.cash.turbine.test
import com.akinci.doggo.core.coroutine.MainDispatcherRule
import com.akinci.doggo.ui.features.splash.SplashViewContract
import com.akinci.doggo.ui.features.splash.SplashViewModel
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.time.Duration

@ExtendWith(MainDispatcherRule::class)
class SplashViewModelTest {

    private lateinit var testedClass: SplashViewModel

    @BeforeEach
    fun setup(){
        testedClass = SplashViewModel()
    }

    @Test
    fun `should return completed state when initialisation is done`() = runTest {
        val expectedState = SplashViewContract.State(
            isCompleted = true
        )

        testedClass.stateFlow.test(timeout = Duration.INFINITE) {
            skipItems(1)

            awaitItem() shouldBe expectedState

            ensureAllEventsConsumed()
        }
    }
}
