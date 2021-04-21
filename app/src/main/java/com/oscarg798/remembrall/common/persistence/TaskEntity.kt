package com.oscarg798.remembrall.common.persistence

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = EntityName)
data class TaskEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "description")
    val description: String?,
    @ColumnInfo(name = "priority")
    val priority: String,
    @ColumnInfo(name = "completed")
    val completed: Boolean = false,
    @ColumnInfo(name = "dueDate")
    val dueDate: Long? = null,
    @ColumnInfo(name = "calendarSynced")
    val calendarSynced: Boolean?,
    @ColumnInfo(name = "calendarEventId")
    val calendarEventId: String?,
    @ColumnInfo(name = "calendarId")
    val calendarId: String?,
    @ColumnInfo(name = "attendees")
    val attendees: String?
)

const val EntityName = "task"
