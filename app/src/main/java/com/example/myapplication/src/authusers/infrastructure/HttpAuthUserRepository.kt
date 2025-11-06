package com.example.myapplication.src.authusers.infrastructure

import com.example.myapplication.AppContext
import com.example.myapplication.src.authusers.domain.AuthUser
import com.example.myapplication.src.authusers.domain.AuthUserRepository
import com.example.myapplication.src.authusers.domain.valueobjects.AuthUserEmail
import com.example.myapplication.src.authusers.domain.valueobjects.AuthUserName
import com.example.myapplication.src.shared.infrastructure.DataStoreTokenProvider
import com.example.myapplication.src.shared.infrastructure.GlobalHttpRepository
import io.ktor.client.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.*

class HttpAuthUserRepository(client: HttpClient) : GlobalHttpRepository(client),
    AuthUserRepository {

    companion object {
        private const val USERS_ENDPOINT = "/users"
    }

    override suspend fun login(email: String, password: String): String {
        val tokenProvider = DataStoreTokenProvider(AppContext.context)

        val body = mapOf("email" to email, "password" to password)
        val response: Map<String, String> = request(HttpMethod.Post, "$USERS_ENDPOINT/login", body)
        val token = response["token"] ?: throw Exception("Token is missing in response")
        tokenProvider.setToken(token)
        return token
    }

    override suspend fun getUserInfo(): AuthUser {
        val response: HttpResponse = request(HttpMethod.Get, USERS_ENDPOINT)
        val json = Json.parseToJsonElement(response.bodyAsText()).jsonObject

        return AuthUser(
            id = json["id"]?.jsonPrimitive?.int ?: 0,
            name = AuthUserName(json["name"]?.jsonPrimitive?.content ?: ""),
            email = AuthUserEmail(json["email"]?.jsonPrimitive?.content ?: "")
        )
    }
}