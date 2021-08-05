package com.oscarg798.remembrall.checklistdetail.usecase

import com.oscarg798.remembrall.common_checklist.model.Checklist
import com.oscarg798.remembrall.common_checklist.repository.ChecklistRepository
import javax.inject.Inject

class RemoveChecklistItem @Inject constructor(private val checklistRepository: ChecklistRepository) {

    suspend operator fun invoke(checklist: Checklist, checklistItemId: String): Checklist {
        checklistRepository.removeChecklistItem(
            checklist.id,
            checklist.items.first { it.id == checklistItemId })
        val currentItems = checklist.items.toMutableList()
        currentItems.removeIf { it.id == checklistItemId }
        return checklist.copy(items = currentItems.sortedBy { it.completed })

    }
}