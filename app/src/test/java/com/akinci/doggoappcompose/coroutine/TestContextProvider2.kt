package com.akinci.doggoappcompose.coroutine

import com.akinci.doggoappcompose.common.coroutine.CoroutineContextProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlin.coroutines.CoroutineContext

@ExperimentalCoroutinesApi
class TestContextProvider2(val testCoroutineDispatcher : TestCoroutineDispatcher = TestCoroutineDispatcher()): CoroutineContextProvider() {
    override val Main: CoroutineContext = testCoroutineDispatcher
    override val IO: CoroutineContext = testCoroutineDispatcher
}
