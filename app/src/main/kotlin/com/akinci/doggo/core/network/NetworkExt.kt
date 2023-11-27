package com.akinci.doggo.core.network

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode

suspend inline fun <reified T> HttpResponse.toResponse() =
    when (status) {
        HttpStatusCode.OK -> Result.success(body<T>())
        else -> Result.failure(Throwable("Serverside error with code: ${status.value}"))
    }