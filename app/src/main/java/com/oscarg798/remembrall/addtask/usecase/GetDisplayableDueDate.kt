package com.oscarg798.remembrall.addtask.usecase

import com.oscarg798.remembrall.common.formatters.DueDateFormatter
import java.time.LocalDateTime
import javax.inject.Inject

class GetDisplayableDueDate @Inject constructor(private val dueDateFormatter: DueDateFormatter) {

    fun execute(dueDate: LocalDateTime): String = dueDateFormatter.toDisplayableDate(dueDate)
}
