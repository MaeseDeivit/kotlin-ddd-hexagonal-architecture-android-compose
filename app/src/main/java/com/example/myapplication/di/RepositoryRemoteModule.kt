package com.example.myapplication.di

import com.example.myapplication.src.authusers.domain.AuthUserRepository
import com.example.myapplication.src.authusers.infrastructure.HttpAuthUserRepository
import com.example.myapplication.src.movies.domain.MovieRepository
import com.example.myapplication.src.movies.infrastructure.HttpMovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient

@Module
@InstallIn(SingletonComponent::class)
object RepositoryRemoteModule {

    @Provides
    fun provideMovieRemoteRepository(client: HttpClient): MovieRepository =
        HttpMovieRepository(client)

    @Provides
    fun provideAuthUserRepository(client: HttpClient): AuthUserRepository =
        HttpAuthUserRepository(client)

}