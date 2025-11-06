package com.example.myapplication.src.shared.infrastructure

import com.example.myapplication.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

abstract class GlobalHttpRepository(protected val client: HttpClient) {
    protected suspend inline fun <reified T> request(
        method: HttpMethod,
        endpoint: String,
        body: Any? = null,
        headers: Map<String, String> = emptyMap()
    ): T {
        val url = "${BuildConfig.BASE_URL}$endpoint"
        return try {
            val response: HttpResponse = client.request(url) {
                this.method = method

                headers.forEach { (key, value) ->
                    header(key, value)
                }

                if (body != null) {
                    contentType(ContentType.Application.Json)
                    setBody(body)
                }
            }

            if (response.status.isSuccess()) {
                response.body<T>()
            } else {
                throw Exception("GlobalHttpRepository ${response.status}: ${response.body<String>()}")
            }
        } catch (e: Exception) {
            throw e
        }
    }
}