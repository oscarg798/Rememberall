package com.oscarg798.remembrall.common_task.di

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.oscarg798.remembrall.common_task.TaskRepositoryImpl
import com.oscarg798.remembrall.common_task.datasource.FirebaseTaskStoreDataSource
import com.oscarg798.remembrall.common_task.datasource.TaskDataSource
import com.oscarg798.remembrall.common.repository.domain.TaskRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object TaskModule {

    @Provides
    @Singleton
    fun provideTaskDataSource(
        localDataSourceTask: FirebaseTaskStoreDataSource
    ): TaskDataSource = localDataSourceTask

    @Provides
    @Singleton
    fun provideTaskRepository(
        taskDataSource:
        TaskDataSource
    ): TaskRepository =
        TaskRepositoryImpl(taskDataSource)


    @Provides
    @Singleton
    fun provideFireStoreCollection(firebaseFirestore: FirebaseFirestore): CollectionReference =
        firebaseFirestore.collection(
            CollectionName
        )
}

private const val CollectionName = "tasks"
