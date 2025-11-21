package com.example.myapplication.di

import android.content.Context
import androidx.room.Room
import com.example.myapplication.src.events.infrastructure.local.room.EventDao
import com.example.myapplication.src.movies.infrastructure.local.room.AppDatabase
import com.example.myapplication.src.movies.infrastructure.local.room.MovieDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "jetpack-compose-app-database"
        )
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideMovieDao(db: AppDatabase): MovieDao = db.movieDao()
    @Provides
    fun provideEventDao(db: AppDatabase): EventDao = db.eventDao()
}