package com.oscarg798.remembrall.common.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

/**
 * For some reason suspend is not supported any more :(
 */
@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)

    fun addTask(taskEntity: TaskEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)

    fun update(taskEntity: TaskEntity)

    @Query("select * from $EntityName")
    fun getAll(): List<TaskEntity>

    @Query("select * from $EntityName where id=:id")
    fun find(id: String): TaskEntity?
}
