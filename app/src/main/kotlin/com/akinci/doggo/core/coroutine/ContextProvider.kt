package com.akinci.doggo.core.coroutine

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

/**
 * ContextProviderImpl is provided by ContextProvider to cover unit tests
 *
 * @see [TestContextProvider.kt]
 * **/

interface ContextProvider {
    val main: CoroutineContext
    val io: CoroutineContext
}

class ContextProviderImpl : ContextProvider {
    override val main: CoroutineContext by lazy { Dispatchers.Main }
    override val io: CoroutineContext by lazy { Dispatchers.IO }
}