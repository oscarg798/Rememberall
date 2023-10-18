package com.oscarg798.remembrall.task.persistence.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = TaskEntity.TableName)
internal data class TaskEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "owner") val owner: String,
    @ColumnInfo(name = "owned") val owned: Boolean,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "priority") val priority: String?,
    @ColumnInfo(name = "calendar_sync_information") val calendarSyncInformation: String?,
    @ColumnInfo(name = "due_date") val dueDate: Long?,
    @ColumnInfo(name = "completed") val completed: Boolean,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "created_at") val createdAt: Long?
){

    companion object {
        const val TableName = "task"
    }
}