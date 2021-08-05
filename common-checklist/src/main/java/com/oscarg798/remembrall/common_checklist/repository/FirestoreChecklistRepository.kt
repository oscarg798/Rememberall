package com.oscarg798.remembrall.common_checklist.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.QuerySnapshot
import com.oscarg798.remembrall.common.toSuspend
import com.oscarg798.remembrall.common_checklist.AddChecklistException
import com.oscarg798.remembrall.common_checklist.UpdateChecklistException
import com.oscarg798.remembrall.common_checklist.di.CheckListCollection
import com.oscarg798.remembrall.common_checklist.model.Checklist
import com.oscarg798.remembrall.common_checklist.model.ChecklistDto
import com.oscarg798.remembrall.common_checklist.model.ChecklistItem
import com.oscarg798.remembrall.common_checklist.model.ChecklistItemDto
import javax.inject.Inject
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class FirestoreChecklistRepository @Inject constructor(
    @CheckListCollection private val collectionReference: CollectionReference
) : ChecklistRepository {

    override suspend fun add(checklist: Checklist): Unit = coroutineScope {
        val checklistDto = ChecklistDto(checklist)
        val document = collectionReference.document(checklist.id)
        document
            .set(
                checklistDto.toMap()
            )
            .toSuspend {
                AddChecklistException.AddError()
            }

        runCatching {
            checklistDto.items.map {
                async(start = CoroutineStart.LAZY) {
                    document.collection(ChecklistDto.ColumnNames.Items).document(it.id)
                        .addChecklistItem(it)
                }
            }.awaitAll()
        }.onFailure {
            if (it is AddChecklistException) {
                rollbackCheckListCreation(checklistDto.id)
            }

            throw it
        }

        get(checklist.owner)
    }

    private suspend fun DocumentReference.addChecklistItem(
        checklistItemDto: ChecklistItemDto
    ) {
        set(
            checklistItemDto.toMap()
        ).toSuspend {
            AddChecklistException.AddError()
        }
    }

    override suspend fun get(owner: String): Collection<Checklist> {
        val checklistResults =
            collectionReference.whereEqualTo(ChecklistDto.ColumnNames.Owner, owner)
                .get().toSuspend { error ->
                    AddChecklistException.UnableToFindChecklists(error)
                }

        return checklistResults.result.documents.map {
            ChecklistDto(
                it.id,
                it.data!!,
                getChecklistItems(collectionReference.document(it.id)).result.documents.map { checkListItemDocumentSnapshot ->
                    ChecklistItemDto(
                        checkListItemDocumentSnapshot.id,
                        checkListItemDocumentSnapshot.data!!
                    )
                }
            )

        }.map {
            it.toChecklist()
        }
    }

    override suspend fun getById(id: String): Checklist {
        val checklistDocument = collectionReference.document(id)
        val checklistResult = checklistDocument.get().toSuspend {
            AddChecklistException.ChecklistNotFoundById(id)
        }

        return ChecklistDto(
            id,
            checklistResult.result.data!!,
            getChecklistItems(checklistDocument).result.documents.map { checkListItemDocumentSnapshot ->
                ChecklistItemDto(
                    checkListItemDocumentSnapshot.id,
                    checkListItemDocumentSnapshot.data!!
                )
            }).toChecklist()
    }

    override suspend fun updateChecklist(checklist: Checklist) = coroutineScope {
        val checklistDto = ChecklistDto(checklist = checklist)
        val checklistDocument = collectionReference.document(checklist.id)

        checklistDocument.update(checklistDto.toMap()).toSuspend {
            UpdateChecklistException.UnableToUpdateChecklist(checklist.id)
        }

        val collection = checklistDocument.collection(ChecklistDto.ColumnNames.Items)
        checklistDto.items.map { checklistItemDto ->
            async(start = CoroutineStart.LAZY) {
                val checklistItemDocument = collection.document(checklistItemDto.id)

                runCatching {
                    doesCheckListItemExists(checklistItemDocument)
                }.fold({ exists ->
                    if (exists) {
                        checklistItemDocument.updateChecklistItem(checklistItemDto, checklist)
                    } else {
                        checklistItemDocument.addChecklistItem(checklistItemDto)
                    }
                }, {
                    checklistItemDocument.addChecklistItem(checklistItemDto)
                })
            }
        }.awaitAll()

        Unit
    }

    override suspend fun removeChecklistItem(checklistId: String, checklistItem: ChecklistItem) {
        val checklistDocument = collectionReference.document(checklistId)
        val collection = checklistDocument.collection(ChecklistDto.ColumnNames.Items)
        collection.document(checklistItem.id).delete().toSuspend {
            UpdateChecklistException.ErrorUpdatingChecklistItem(
                checklistId, checklistItem.id, it
            )
        }
    }

    private suspend fun DocumentReference.updateChecklistItem(
        checklistItem: ChecklistItemDto,
        checklist: Checklist
    ) = update(checklistItem.toMap())
        .toSuspend {
            UpdateChecklistException.ErrorUpdatingChecklistItem(
                checklist.id,
                checklistItem.id
            )
        }

    private suspend fun doesCheckListItemExists(checklistDocument: DocumentReference): Boolean {
        val result = checklistDocument.get().toSuspend {
            NullPointerException()
        }

        return result.result.data != null && result.result.data!!.isNotEmpty()
    }

    private suspend fun getChecklistItems(checklistDocument: DocumentReference): Task<QuerySnapshot> {
        return checklistDocument.collection(ChecklistDto.ColumnNames.Items)
            .get().toSuspend { error ->
                AddChecklistException.UnableToFindChecklists(error)
            }
    }

    private suspend fun rollbackCheckListCreation(checklistId: String) {
        collectionReference.document(checklistId).delete().toSuspend {
            AddChecklistException.RollbackError(checklistId, it)
        }
    }
}

