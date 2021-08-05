package com.oscarg798.remembrall.checklist.usecase

import com.oscarg798.remembrall.common.auth.GetSignedInUserUseCase
import com.oscarg798.remembrall.common_checklist.model.Checklist
import com.oscarg798.remembrall.common_checklist.repository.ChecklistRepository
import javax.inject.Inject

class GetChecklists @Inject constructor(
    private val getSignedInUserUseCase: GetSignedInUserUseCase,
    private val checklistRepository: ChecklistRepository
) {

    suspend operator fun invoke(): Collection<Checklist> {
        return checklistRepository.get(getSignedInUserUseCase.execute().email)
    }
}