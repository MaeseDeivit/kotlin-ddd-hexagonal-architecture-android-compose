package com.example.myapplication.src.movies.infrastructure.local.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val title: String
)