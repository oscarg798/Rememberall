package com.oscarg798.remembrall.task.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.oscarg798.remembrall.task.persistence.dao.CalendarSyncInformationDAO
import com.oscarg798.remembrall.task.persistence.dao.RoomTaskDAO
import com.oscarg798.remembrall.task.persistence.entity.CalendarSyncInformationEntity
import com.oscarg798.remembrall.task.persistence.entity.TaskEntity

@Database(entities = [TaskEntity::class, CalendarSyncInformationEntity::class], version = 1)
internal abstract class TaskDatabase : RoomDatabase() {
    abstract fun roomTaskDao(): RoomTaskDAO
    abstract fun calendarSyncInformationDao(): CalendarSyncInformationDAO
}