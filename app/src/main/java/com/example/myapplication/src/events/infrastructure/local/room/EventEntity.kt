package com.example.myapplication.src.events.infrastructure.local.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val entityId: String,
    val entityType: String,
    val operationType: String,
    val payload: String?,
    val timestamp: Long = System.currentTimeMillis(),
    val synced: Boolean = false
)