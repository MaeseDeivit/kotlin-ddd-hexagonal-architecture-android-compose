package com.example.myapplication.src.events.domain

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class Event(
    val id: String,
    val entityId: String,
    val entityType: String,
    val operationType: String,
    val payload: String?,
    val timestamp: Long,
    val synced: Boolean = false
) : Parcelable
