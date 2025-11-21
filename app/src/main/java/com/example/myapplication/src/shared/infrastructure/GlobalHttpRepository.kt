package com.example.myapplication.src.shared.infrastructure

import android.util.Log
import com.example.myapplication.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

val globalHttpClient = HttpClient(io.ktor.client.engine.cio.CIO) {
    install(HttpTimeout) {
        requestTimeoutMillis = 5000
        connectTimeoutMillis = 5000
        socketTimeoutMillis = 5000
    }
    HttpResponseValidator {
        handleResponseExceptionWithRequest { exception, _ ->
            println("Error de red: ${exception.message}")
        }
    }
}

abstract class GlobalHttpRepository(protected val client: HttpClient = globalHttpClient) {
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
                println("Request: $method $url Headers: $headers Body: $body")
            }
            println("Status: ${response.status}, Body: ${response.bodyAsText()}")

            if (response.status.isSuccess()) {
                response.body<T>()
            } else {
                Log.d("error", "GlobalHttpRepository ${response.status}: ${response.body<String>()}")
                throw Exception("GlobalHttpRepository ${response.status}: ${response.body<String>()}")
            }
        } catch (e: Exception) {
            println("Excepción en request: ${e.message}")
            throw e
        }
    }
}