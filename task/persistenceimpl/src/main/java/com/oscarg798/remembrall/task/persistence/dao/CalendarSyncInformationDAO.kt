package com.oscarg798.remembrall.task.persistence.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.oscarg798.remembrall.task.persistence.entity.CalendarSyncInformationEntity

@Dao
internal interface CalendarSyncInformationDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(calendarSyncInformationEntity: CalendarSyncInformationEntity)

    @Update
    fun update(calendarSyncInformationEntity: CalendarSyncInformationEntity)

    @Delete
    fun delete(calendarSyncInformationEntity: CalendarSyncInformationEntity)

    @Query("select * from ${CalendarSyncInformationEntity.TableName} where id=:id")
    fun get(id: String): CalendarSyncInformationEntity

    @Query("select * from ${CalendarSyncInformationEntity.TableName} where task_id=:taskId")
    fun getByTaskId(taskId: String): CalendarSyncInformationEntity?

    @Query("select count(id) from ${CalendarSyncInformationEntity.TableName} where id=:id")
    fun count(id: String): Int
}