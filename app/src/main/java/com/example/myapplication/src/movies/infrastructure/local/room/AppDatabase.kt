package com.example.myapplication.src.movies.infrastructure.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myapplication.src.events.infrastructure.local.room.EventDao
import com.example.myapplication.src.events.infrastructure.local.room.EventEntity

@Database(entities = [MovieEntity::class, EventEntity::class], version = 4)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun eventDao(): EventDao
}