package com.example.myapplication.src.events.domain

import kotlinx.coroutines.flow.Flow

interface EventLocalRepositoryInterface {
    fun getEventsByEntityType(entityType: String?): Flow<List<Event>>
    suspend fun getPendingEventsByEntityType(entityType: String?): Flow<List<Event>>
    suspend fun getPendingEventsByEntityId(entityId: String): List<Event>
    suspend fun create(event: Event): Unit
    suspend fun update(event: Event): Unit
    suspend fun delete(event: Event): Unit
}