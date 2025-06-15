package com.example.myapplication.di

import com.example.myapplication.AppContext
import com.example.myapplication.src.authusers.application.AuthUserServices
import com.example.myapplication.src.authusers.domain.AuthUserRepository
import com.example.myapplication.src.authusers.infrastructure.HttpAuthUserRepository
import com.example.myapplication.src.movies.application.MovieServices
import com.example.myapplication.src.movies.domain.MovieRepository
import com.example.myapplication.src.movies.infrastructure.HttpMovieRepository
import com.example.myapplication.src.shared.infrastructure.DataStoreTokenProvider
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import kotlinx.serialization.json.Json
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json

object ServiceLocator {
    private val client: HttpClient = HttpClient(CIO) {
        val tokenProvider = DataStoreTokenProvider(AppContext.context)

        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
        defaultRequest {
            tokenProvider.getToken()?.let { token ->
                header("Authorization", "Bearer $token")
            }
        }
    }

    val authUserServices: AuthUserServices by lazy {
        val repository: AuthUserRepository = HttpAuthUserRepository(client)
        AuthUserServices(repository)
    }

    val movieServices: MovieServices by lazy {
        val repository: MovieRepository = HttpMovieRepository(client)
        MovieServices(repository)
    }
}