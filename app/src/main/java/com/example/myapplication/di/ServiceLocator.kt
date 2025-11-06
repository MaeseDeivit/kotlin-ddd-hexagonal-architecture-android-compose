package com.example.myapplication.di

import androidx.room.Room
import com.example.myapplication.AppContext
import com.example.myapplication.src.authusers.application.AuthUserServices
import com.example.myapplication.src.authusers.domain.AuthUserRepository
import com.example.myapplication.src.authusers.infrastructure.HttpAuthUserRepository
import com.example.myapplication.src.movies.application.MovieServices
import com.example.myapplication.src.movies.domain.MovieRepository
import com.example.myapplication.src.movies.infrastructure.HttpMovieRepository
import com.example.myapplication.src.movies.infrastructure.local.room.AppDatabase
import com.example.myapplication.src.movies.infrastructure.local.room.MovieDao
import com.example.myapplication.src.movies.infrastructure.local.room.MovieLocalRepository
import com.example.myapplication.src.shared.infrastructure.DataStoreTokenProvider
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object ServiceLocator {
    private val db: AppDatabase = Room.databaseBuilder(
        AppContext.context,
        AppDatabase::class.java,
        "jetpack-compose-app-database"
    ).build()

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
        val movieDao: MovieDao = db.movieDao()
        val localRepository: MovieRepository = MovieLocalRepository(movieDao)
        val remoteRepository: MovieRepository = HttpMovieRepository(client)
        MovieServices(localRepository, remoteRepository)
    }
}