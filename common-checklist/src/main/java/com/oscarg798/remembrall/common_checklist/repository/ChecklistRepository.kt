package com.oscarg798.remembrall.common_checklist.repository

import com.oscarg798.remembrall.common_checklist.model.Checklist
import com.oscarg798.remembrall.common_checklist.model.ChecklistItem

interface ChecklistRepository {

    suspend fun add(checklist: Checklist)

    suspend fun get(owner: String): Collection<Checklist>

    suspend fun getById(id: String): Checklist

    suspend fun updateChecklist(checklist: Checklist)

    suspend fun removeChecklistItem(checklistId: String, checklistItem: ChecklistItem)

}