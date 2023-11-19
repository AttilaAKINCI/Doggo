package com.akinci.doggo.core.network

import com.akinci.doggo.core.network.exception.NotFound
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess

suspend inline fun <reified T> HttpResponse.toResponse(): Result<T> {
    return if (status.isSuccess()) {
        Result.success(body<T>())
    } else {
        Result.failure(
            when (status) {
                HttpStatusCode.NotFound -> NotFound("Response not found with code: ${status.value}")
                else -> Throwable("Serverside error with code: ${status.value}")
            }
        )
    }
}