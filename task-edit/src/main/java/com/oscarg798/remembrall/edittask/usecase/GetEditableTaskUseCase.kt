package com.oscarg798.remembrall.edittask.usecase

import com.oscarg798.remembrall.common_gettask.usecase.GetTaskById
import com.oscarg798.remembrall.common_addedit.usecase.GetAvailablePrioritiesUseCase
import com.oscarg798.remembrall.common.formatter.DueDateFormatter
import com.oscarg798.remembrall.common.model.DisplayableTask
import javax.inject.Inject

class GetEditableTaskUseCase @Inject constructor(
    private val getTaskById: GetTaskById,
    private val getAvailablePrioritiesUseCase: GetAvailablePrioritiesUseCase,
    private val dueDateFormatter: DueDateFormatter
) {

    suspend fun execute(taskId: String): com.oscarg798.remembrall.edittask.model.EditableTask {
        val task = getTaskById.execute(taskId)
        return com.oscarg798.remembrall.edittask.model.EditableTask(
            task,
            displayableTask = DisplayableTask(task, dueDateFormatter),
            getAvailablePrioritiesUseCase.execute()
        )
    }
}
