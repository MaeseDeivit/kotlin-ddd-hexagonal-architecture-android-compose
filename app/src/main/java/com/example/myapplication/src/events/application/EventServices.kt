package com.example.myapplication.src.events.application

import com.example.myapplication.src.events.domain.Event
import com.example.myapplication.src.events.domain.EventLocalRepositoryInterface
import kotlinx.coroutines.flow.Flow

class EventServices(
    private val repository: EventLocalRepositoryInterface,
) {
    fun getEventsByEntityType(entityType: String?): Flow<List<Event>> {
        return repository.getEventsByEntityType(entityType)
    }

    suspend fun getPendingEventsByEntityType(entityType: String?): Flow<List<Event>> {
        return repository.getPendingEventsByEntityType(entityType)
    }

    suspend fun create(event: Event) {
        repository.create(event)
    }

    suspend fun updateEvent(event: Event) {
        repository.update(event)
    }

    suspend fun markEventAsSynced(event: Event) {
        val updatedEvent = event.copy(synced = true)
        repository.update(updatedEvent)
    }
}