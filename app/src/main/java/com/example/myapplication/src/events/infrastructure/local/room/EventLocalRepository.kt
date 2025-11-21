package com.example.myapplication.src.events.infrastructure.local.room

import com.example.myapplication.src.events.domain.Event
import com.example.myapplication.src.events.domain.EventLocalRepositoryInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class EventLocalRepository @Inject constructor(
    private val dao: EventDao
) : EventLocalRepositoryInterface {
    override fun getEventsByEntityType(entityType: String?): Flow<List<Event>> =
        dao.getEventsByEntityType(entityType).map { entities ->
            entities.map { Event(it.id, it.entityId, it.entityType, it.operationType,it.payload,it.timestamp, it.synced) }
        }

    override suspend fun getPendingEventsByEntityId(entityId: String): List<Event> =
        dao.getPendingEventsByEntityId(entityId).map {
            Event(it.id, it.entityId, it.entityType, it.operationType,it.payload,it.timestamp, it.synced)
        }

    override suspend fun getPendingEventsByEntityType(entityType: String?): Flow<List<Event>> =
        dao.getPendingEventsByEntityType(entityType).map { entities ->
            entities.map { Event(it.id, it.entityId, it.entityType, it.operationType,it.payload,it.timestamp, it.synced) }
        }

    override suspend fun create(event: Event) {
        dao.insert(EventDao.fromDomain(event))
    }

    override suspend fun update(event: Event) {
        dao.update(EventDao.fromDomain(event))
    }

    override suspend fun delete(event: Event) {
        dao.delete(EventDao.fromDomain(event))
    }
}