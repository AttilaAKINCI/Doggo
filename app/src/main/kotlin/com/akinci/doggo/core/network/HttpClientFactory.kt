package com.akinci.doggo.core.network

import com.akinci.doggo.core.application.AppConfig
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

class HttpClientFactory(
    private val httpEngineFactory: HttpEngineFactory,
    private val appConfig: AppConfig,
) {

    @OptIn(ExperimentalSerializationApi::class)
    fun create() = HttpClient(httpEngineFactory.create()) {
        install(DefaultRequest) {
            url(appConfig.getServiceEndpointBaseUrl())
            contentType(ContentType.Application.Json)
        }

        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                    explicitNulls = false
                },
            )
        }

        install(HttpTimeout) {
            requestTimeoutMillis = 45000
            connectTimeoutMillis = 10000
        }

        if (appConfig.isDebugMode()) {
            install(Logging) {
                logger = Logger.SIMPLE
                level = LogLevel.ALL
            }
        }
    }
}