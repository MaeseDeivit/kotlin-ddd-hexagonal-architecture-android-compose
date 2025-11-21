package com.example.myapplication.src.events.infrastructure.local.room

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import com.example.myapplication.src.events.domain.Event
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {
    @Insert
    suspend fun insert(event: EventEntity)
    @Query("SELECT * FROM events WHERE entityType = :entityType ORDER BY timestamp DESC")
    fun getEventsByEntityType(entityType: String?): Flow<List<EventEntity>>
    @Query("SELECT * FROM events WHERE entityType = :entityType AND synced = 0 ORDER BY timestamp DESC")
    fun getPendingEventsByEntityType(entityType: String?): Flow<List<EventEntity>>

    @Query("SELECT * FROM events WHERE entityId = :entityId AND synced = 0 ORDER BY timestamp DESC")
    fun getPendingEventsByEntityId(entityId: String): List<EventEntity>
    @Query("SELECT * FROM events WHERE id = :id")
    fun findById(id: Int): EventEntity?
    @Update
    suspend fun update(event: EventEntity)
    @Delete
    suspend fun delete(event: EventEntity)

    companion object {
        fun fromDomain(event: Event): EventEntity {
            return EventEntity(
                id = event.id,
                entityId = event.entityId,
                entityType = event.entityType,
                operationType = event.operationType,
                payload = event.payload,
                timestamp = event.timestamp,
                synced = event.synced
            )
        }
        fun toDomain(eventEntity: EventEntity): Event {
            return Event(
                id = eventEntity.id,
                entityId = eventEntity.entityId,
                entityType = eventEntity.entityType,
                operationType = eventEntity.operationType,
                payload = eventEntity.payload,
                timestamp = eventEntity.timestamp,
                synced = eventEntity.synced
            )
        }
    }
}