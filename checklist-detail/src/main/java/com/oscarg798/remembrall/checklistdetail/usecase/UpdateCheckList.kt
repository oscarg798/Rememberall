package com.oscarg798.remembrall.checklistdetail.usecase

import com.oscarg798.remembrall.common_checklist.model.Checklist
import com.oscarg798.remembrall.common_checklist.repository.ChecklistRepository
import javax.inject.Inject

class UpdateCheckList @Inject constructor(private val checklistRepository: ChecklistRepository) {

    suspend operator fun invoke(checklist: Checklist): Checklist {
        checklistRepository.updateChecklist(checklist = checklist)
        return checklist.copy(items = checklist.items.sortedBy { it.completed })
    }
}