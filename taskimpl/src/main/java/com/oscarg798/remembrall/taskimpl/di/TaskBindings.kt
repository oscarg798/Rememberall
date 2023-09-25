package com.oscarg798.remembrall.taskimpl.di

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.oscarg798.remembrall.task.TaskRepository
import com.oscarg798.remembrall.taskimpl.IdProvider
import com.oscarg798.remembrall.taskimpl.TaskRepositoryImpl
import com.oscarg798.remembrall.taskimpl.UUIDIdProvider
import com.oscarg798.remembrall.taskimpl.datasource.FirebaseTaskStoreDataSource
import com.oscarg798.remembrall.taskimpl.datasource.TaskDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface TaskBindings {

    @Binds
    fun bindTaskDataSource(impl: FirebaseTaskStoreDataSource): TaskDataSource

    @Binds
    fun bindIdProvider(impl: UUIDIdProvider): IdProvider

    @Binds
    @Singleton
    fun bindTaskRepository(impl: TaskRepositoryImpl): TaskRepository

    companion object {
        @Provides
        @Reusable
        fun provideFireStoreCollection(firebaseFirestore: FirebaseFirestore): CollectionReference =
            firebaseFirestore.collection(CollectionName)
    }


}

private const val CollectionName = "tasks"