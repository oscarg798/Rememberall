package com.oscarg798.remembrall.edittask

import com.oscarg798.remembrall.common.formatters.DueDateFormatter
import com.oscarg798.remembrall.common.model.EditableTask
import com.oscarg798.remembrall.common.repository.domain.TaskRepository
import com.oscarg798.remembrall.common.usecase.AddTaskToCalendarUseCase
import com.oscarg798.remembrall.common.usecase.GetTaskById
import javax.inject.Inject

class EditTaskUseCase @Inject constructor(
    private val getTaskById: GetTaskById,
    private val dueDateFormatter: DueDateFormatter,
    private val addTaskToCalendarUseCase: AddTaskToCalendarUseCase,
    private val taskRepository: TaskRepository
) {

    suspend fun execute(
        editableTask: EditableTask

    ) {
        var task = editableTask.task

        if (task.calendarSyncInformation == null && task.dueDate != null) {
            addTaskToCalendarUseCase.execute(
                task,
                dueDateFormatter.toDueLocalDatetime(task.dueDate!!),
                editableTask.attendees.map { it.email }.toSet()
            )

            task = getTaskById.execute(task.id)
        } else if (task.calendarSyncInformation != null && task.dueDate != null) {
            // TODO update task calendar info
        }

        taskRepository.update(task)
        taskRepository.onTaskUpdated(task)
    }
}
