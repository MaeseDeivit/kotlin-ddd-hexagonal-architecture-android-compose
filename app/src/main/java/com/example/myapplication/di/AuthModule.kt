package com.example.myapplication.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import android.content.Context
import com.example.myapplication.src.shared.domain.TokenProvider
import com.example.myapplication.src.shared.infrastructure.DataStoreTokenProvider
import dagger.Provides
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideTokenProvider(@ApplicationContext context: Context): TokenProvider =
        DataStoreTokenProvider(context)
}