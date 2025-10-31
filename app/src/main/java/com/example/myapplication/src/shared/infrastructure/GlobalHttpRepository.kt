package com.example.myapplication.src.shared.infrastructure

import android.util.Log
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
        Log.d("GlobalHttpRepository", "Request URL: $url")
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
                Log.d("GlobalHttpRepository", "Request body: $body")
            }

            if (response.status.isSuccess()) {
                response.body<T>()
            } else {
                if (response.status.value == 401) {
                    Log.d(
                        "GlobalHttpRepository",
                        "Unauthorized access, token might be invalid or expired."
                    )

                } else {
                    throw Exception("GlobalHttpRepository ${response.status}: ${response.body<String>()}")
                }
            }
        } catch (e: Exception) {
            throw e
        } as T
    }
}