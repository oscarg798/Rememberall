package com.oscarg798.remembrall.common.usecase

import com.oscarg798.remembrall.common.formatter.DueDateFormatter
import javax.inject.Inject

class GetDueDateAsMillisFormDisplayableDueDateUseCase @Inject constructor(
    private val dueDateFormatter: DueDateFormatter
) {

    fun execute(dueDate: String) = dueDateFormatter.toDueDateInMillis(dueDate)
}
