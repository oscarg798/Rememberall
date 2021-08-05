package com.oscarg798.remembrall.checklist_add.usecase

import com.oscarg798.remembrall.common.IdentifierGenerator
import com.oscarg798.remembrall.common.auth.GetSignedInUserUseCase
import com.oscarg798.remembrall.common_checklist.AddChecklistException
import com.oscarg798.remembrall.common_checklist.model.Checklist
import com.oscarg798.remembrall.common_checklist.model.ChecklistItem
import com.oscarg798.remembrall.common_checklist.repository.ChecklistRepository
import com.oscarg798.remembrall.ui_common.ui.AwesomeIcon
import javax.inject.Inject

class AddCheckList @Inject constructor(
    private val checklistRepository: ChecklistRepository,
    private val identifierGenerator: IdentifierGenerator,
    private val getSignedInUserUseCase: GetSignedInUserUseCase,
) {

    suspend operator fun invoke(name: String, items: List<ChecklistItem>, icon: AwesomeIcon?) {
        if (name.length < 3) {
            throw AddChecklistException.NameValidationError()
        }

        if (items.isEmpty()) {
            throw AddChecklistException.CheckListCanNotBeEmptyError()
        }

        if(icon ==null){
            throw AddChecklistException.IconMissedError()
        }

        return checklistRepository.add(
            Checklist(
                id = identifierGenerator.createStringIdentifier(),
                owner = getSignedInUserUseCase.execute().email,
                name = name,
                icon = icon.code,
                items = items
            )
        )
    }
}
