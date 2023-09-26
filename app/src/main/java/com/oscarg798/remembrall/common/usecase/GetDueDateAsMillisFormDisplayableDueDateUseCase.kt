package com.oscarg798.remembrall.common.usecase

import com.oscarg798.remembrall.dateformatter.DateFormatter

import javax.inject.Inject

class GetDueDateAsMillisFormDisplayableDueDateUseCase @Inject constructor(
    private val dueDateFormatter: DateFormatter
) {

    fun execute(dueDate: String) = dueDateFormatter.toMillis(dueDate)
}
