package com.oscarg798.remembrall.checklistdetail.usecase

import com.oscarg798.remembrall.common_checklist.model.Checklist
import com.oscarg798.remembrall.common_checklist.repository.ChecklistRepository
import javax.inject.Inject

class GetChecklistById @Inject constructor(private val checklistRepository: ChecklistRepository) {

    suspend operator fun invoke(id: String): Checklist {
        val checklist =  checklistRepository.getById(id)
        return checklist.copy(items = checklist.items.sortedBy { it.position })
    }
}