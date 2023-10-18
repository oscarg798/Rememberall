package com.oscarg798.remembrall.task.persistence.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = CalendarSyncInformationEntity.TableName)
data class CalendarSyncInformationEntity(
    //this will be formed from calendarId_eventId
    @PrimaryKey @ColumnInfo("id") val id: String,
    @ColumnInfo("calendar_id") val calendarId: String,
    @ColumnInfo("eventId") val eventId: String,
    @ColumnInfo("synced") val synced: Boolean,
    @ColumnInfo("attendees") val attendees: String?,
    @ColumnInfo("task_id") val taskId: String
) {

    companion object {
        const val TableName = "calendar_sync_information"
    }
}