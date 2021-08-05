package com.oscarg798.remembrall.edittask.usecase

import com.oscarg798.remembrall.common_gettask.usecase.GetTaskById
import com.oscarg798.remembrall.common.formatter.DueDateFormatter
import com.oscarg798.remembrall.edittask.model.EditableTask
import com.oscarg798.remembrall.common.repository.domain.TaskRepository
import com.oscarg798.remembrall.common_addedit.usecase.AddTaskToCalendarUseCase
import javax.inject.Inject

class EditTaskUseCase @Inject constructor(
    private val getTaskById: GetTaskById,
    private val dueDateFormatter: DueDateFormatter,
    private val addTaskToCalendarUseCase: AddTaskToCalendarUseCase,
    private val taskRepository: TaskRepository,
    private val updateCalendarTaskUseCase: UpdateCalendarTaskUseCase
) {

    suspend fun execute(
        editableTask: EditableTask

    ) {
        val task = editableTask.task
        updateCalendarTaskUseCase.execute(task)
        taskRepository.update(task)
        taskRepository.onTaskUpdated(task)
    }
}
