package com.akinci.doggo.core.network

import com.akinci.doggo.core.network.json.BreedListServiceResponse
import com.akinci.doggo.core.network.json.ImageServiceResponse
import com.akinci.doggo.core.network.json.SubBreedListServiceResponse
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.http.headersOf
import java.nio.charset.Charset

class HttpEngineFactoryMock : HttpEngineFactory() {

    private val responseHeaders = headersOf(HttpHeaders.ContentType, "application/json")
    var statusCode: HttpStatusCode = OK
    var simulateException = false

    override fun create(): MockEngine {
        return MockEngine { request ->
            if (simulateException) throw Throwable("Simulated Network Exception")

            val path = request.url.encodedPath
            val content = when {
                path.contains("api/breeds/list/all") -> getBreedListContent(statusCode)
                path.contains("api/breed/") -> getSubBreedListContent(statusCode)
                path.contains("/images/random/") -> getImageContent(statusCode)
                else -> throw IllegalStateException("Unsupported path")
            }

            respond(
                content = content,
                status = statusCode,
                headers = responseHeaders,
            )
        }
    }

    private fun getBreedListContent(statusCode: HttpStatusCode): ByteArray {
        return when (statusCode) {
            OK -> BreedListServiceResponse.success.toByteArray(Charset.defaultCharset())
            else -> statusCode.description.toByteArray()
        }
    }

    private fun getSubBreedListContent(statusCode: HttpStatusCode): ByteArray {
        return when (statusCode) {
            OK -> SubBreedListServiceResponse.success.toByteArray(Charset.defaultCharset())
            else -> statusCode.description.toByteArray()
        }
    }

    private fun getImageContent(statusCode: HttpStatusCode): ByteArray {
        return when (statusCode) {
            OK -> ImageServiceResponse.success.toByteArray(Charset.defaultCharset())
            else -> statusCode.description.toByteArray()
        }
    }
}