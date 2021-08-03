package com.oscarg798.remembrall.edittask

import com.oscarg798.remebrall.common_gettask.usecase.GetTaskById
import com.oscarg798.remembrall.addtask.usecase.GetAvailablePrioritiesUseCase
import com.oscarg798.remembrall.common.formatter.DueDateFormatter
import com.oscarg798.remembrall.common.model.DisplayableTask
import com.oscarg798.remembrall.common.model.EditableTask
import javax.inject.Inject

class GetEditableTaskUseCase @Inject constructor(
    private val getTaskById: GetTaskById,
    private val getAvailablePrioritiesUseCase: GetAvailablePrioritiesUseCase,
    private val dueDateFormatter: DueDateFormatter
) {

    suspend fun execute(taskId: String): EditableTask {
        val task = getTaskById.execute(taskId)
        return EditableTask(
            task,
            displayableTask = DisplayableTask(task, dueDateFormatter),
            getAvailablePrioritiesUseCase.execute()
        )
    }
}
