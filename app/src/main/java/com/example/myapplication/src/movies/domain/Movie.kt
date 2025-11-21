package com.example.myapplication.src.movies.domain

import com.example.myapplication.src.events.domain.Event
import java.util.UUID
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie(
    val id: String,
    val title: String,
    val events: List<Event> = emptyList(),
) : Parcelable {

    fun addEvent(event: Event): Movie {
        return copy(events = events + event)
    }

    fun removeEvent(event: Event): Movie {
        return copy(events = events - event)
    }

    fun pendingEvents(): List<Event> = events.filter { !it.synced }

    override fun toString(): String = "Movie(id=$id, title=$title)"

    companion object {
        fun emptyMovie(): Movie = Movie(
            id = UUID.randomUUID().toString(),
            title = "",
            events = emptyList()
        )
    }
}