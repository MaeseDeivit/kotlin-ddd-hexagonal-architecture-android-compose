package com.example.myapplication.di

import com.example.myapplication.src.events.domain.EventLocalRepositoryInterface
import com.example.myapplication.src.events.infrastructure.local.room.EventDao
import com.example.myapplication.src.events.infrastructure.local.room.EventLocalRepository
import com.example.myapplication.src.movies.domain.MovieLocalRepositoryInterface
import com.example.myapplication.src.movies.infrastructure.local.MovieLocalRepository
import com.example.myapplication.src.movies.infrastructure.local.room.MovieDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryLocalModule {
    @Provides
    fun provideEventLocalRepository(eventDao: EventDao): EventLocalRepositoryInterface =
        EventLocalRepository(eventDao)
    @Provides
    fun provideMovieLocalRepository(movieDao: MovieDao, eventRepository: EventLocalRepository): MovieLocalRepositoryInterface =
        MovieLocalRepository(movieDao, eventRepository)
}