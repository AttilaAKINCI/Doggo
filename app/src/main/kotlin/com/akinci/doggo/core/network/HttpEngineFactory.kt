package com.akinci.doggo.core.network

import io.ktor.client.engine.okhttp.OkHttp
import javax.inject.Inject

/**
 * HttpEngineFactory encapsulation is created to mock network requests for unit tests
 * **/
open class HttpEngineFactory @Inject constructor() {

    open fun create() = OkHttp.create()
}