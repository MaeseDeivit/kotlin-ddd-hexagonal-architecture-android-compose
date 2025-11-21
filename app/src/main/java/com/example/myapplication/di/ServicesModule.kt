package com.example.myapplication.di

import com.example.myapplication.src.authusers.application.AuthUserServices
import com.example.myapplication.src.authusers.domain.AuthUserRepository
import com.example.myapplication.src.events.application.EventServices
import com.example.myapplication.src.events.infrastructure.local.room.EventLocalRepository
import com.example.myapplication.src.movies.application.MovieServices
import com.example.myapplication.src.movies.application.MovieSyncService
import com.example.myapplication.src.movies.domain.MovieLocalRepositoryInterface
import com.example.myapplication.src.movies.domain.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ServicesModule {
    @Provides
    fun provideEventServices(
        eventRepository: EventLocalRepository
    ): EventServices = EventServices(eventRepository)
    @Provides
    fun provideMovieServices(
        localRepository: MovieLocalRepositoryInterface,
        remoteRepository: MovieRepository,
        eventServices: EventServices
    ): MovieServices = MovieServices(localRepository, remoteRepository, eventServices)

    @Provides
    fun provideAuthUserServices(
      authUserRepository: AuthUserRepository
    ): AuthUserServices = AuthUserServices(authUserRepository)

    @Provides
    fun provideMovieSyncService(
        localRepository: MovieLocalRepositoryInterface,
        remoteRepository: MovieRepository
    ): MovieSyncService = MovieSyncService(localRepository, remoteRepository)
}