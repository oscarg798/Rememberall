package com.oscarg798.remembrall.task.persistence.di

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import com.oscarg798.remembrall.persistence.TaskDAO
import com.oscarg798.remembrall.task.persistence.TaskDAOImpl
import com.oscarg798.remembrall.task.persistence.TaskDatabase
import com.oscarg798.remembrall.task.persistence.dao.CalendarSyncInformationDAO
import com.oscarg798.remembrall.task.persistence.dao.RoomTaskDAO
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object TaskPersistenceModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TaskDatabase {
        return Room.databaseBuilder(
            context,
            TaskDatabase::class.java, TaskDatabase::class.simpleName
        ).build()
    }

    @Provides
    fun provideRoomTaskDao(db: TaskDatabase): RoomTaskDAO = db.roomTaskDao()

    @Provides
    fun provideCalendarInfoDao(db: TaskDatabase): CalendarSyncInformationDAO =
        db.calendarSyncInformationDao()
}

@Module
@InstallIn(SingletonComponent::class)
internal interface TaskPersistenceBindings {

    @Binds
    fun bindTaskDao(impl: TaskDAOImpl): TaskDAO
}