package com.oscarg798.remembrall.common_checklist.di

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.oscarg798.remembrall.common_checklist.repository.ChecklistRepository
import com.oscarg798.remembrall.common_checklist.repository.FirestoreChecklistRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object CommonChecklistModule {

    @CheckListCollection
    @Singleton
    @Provides
    fun provideCheckListFirestoreCollection(firebaseFirestore: FirebaseFirestore): CollectionReference =
        firebaseFirestore.collection(CollectionName)

    @Singleton
    @Provides
    fun provideCheckListRepository(firestoreChecklistRepository: FirestoreChecklistRepository): ChecklistRepository = firestoreChecklistRepository
}

@Qualifier
annotation class CheckListCollection

private const val CollectionName = "checklists"
