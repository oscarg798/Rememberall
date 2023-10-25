package com.oscarg798.remembrall.task.persistence.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SupportSQLiteQuery
import com.oscarg798.remembrall.task.persistence.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface RoomTaskDAO {

    @Update
    fun update(task: TaskEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(tasks: List<TaskEntity>)

    @Query("select * from ${TaskEntity.TableName} where id=:id")
    fun get(id: String): Flow<TaskEntity>

    @RawQuery(observedEntities = [TaskEntity::class])
    fun streamViaQuery(query: SupportSQLiteQuery): Flow<List<TaskEntity>>

    @Query("select * from ${TaskEntity.TableName} where owner=:user")
    fun stream(user: String): Flow<List<TaskEntity>>

    @Delete
    fun delete(task: TaskEntity)

    @Query("select count(id) from ${TaskEntity.TableName} where id=:id")
    fun count(id: String): Int
}