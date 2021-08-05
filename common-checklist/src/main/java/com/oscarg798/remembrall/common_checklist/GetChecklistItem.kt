package com.oscarg798.remembrall.common_checklist

import com.oscarg798.remembrall.common.IdentifierGenerator
import com.oscarg798.remembrall.common_checklist.model.ChecklistItem
import javax.inject.Inject

class GetChecklistItem @Inject constructor(private val identifierGenerator: IdentifierGenerator) {

    operator fun invoke(name: String, position: Int): ChecklistItem {
        if (name.length < 3) {
            throw AddChecklistException.CheckListItemError()
        }

        return ChecklistItem(
            id = identifierGenerator.createStringIdentifier(),
            name = name,
            completed = false,
            position = position
        )
    }
}