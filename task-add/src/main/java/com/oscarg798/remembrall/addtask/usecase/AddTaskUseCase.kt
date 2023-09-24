package com.oscarg798.remembrall.addtask.usecase

import com.oscarg798.remembrall.addtask.exception.AddTaskException
import com.oscarg798.remembrall.common.auth.GetSignedInUserUseCase
import com.oscarg798.remembrall.common.model.TaskPriority
import com.oscarg798.remembrall.common.repository.domain.TaskRepository
import com.oscarg798.remembrall.common_addedit.usecase.AddTaskToCalendarUseCase
import com.oscarg798.remembrall.dateformatter.DueDateFormatter
import dagger.Reusable
import javax.inject.Inject
import java.time.LocalDateTime
import java.util.regex.Pattern

@Reusable
class AddTaskUseCase @Inject constructor(
    private val addTaskToCalendarUseCase: AddTaskToCalendarUseCase,
    private val dueDateFormatter: DueDateFormatter,
    private val taskRepository: TaskRepository,
    private val getSignedInUserUseCase: GetSignedInUserUseCase,
    private val emailPattern: Pattern
) {

    suspend fun execute(addTaskParam: AddTaskParam) {
        if (addTaskParam.name == null || addTaskParam.name.length < RequiredNameLength) {
            throw AddTaskException.MissingName
        }

        if (addTaskParam.priority == null) {
            throw AddTaskException.MissingPriority
        }

        if (addTaskParam.dueDate == null) {
            throw AddTaskException.MissingDueDate
        }

        if (addTaskParam.attendees != null && !addTaskParam.attendees.areAttendeesValid()) {
            throw AddTaskException.AttendeesWrongFormat
        }

        val taskId = taskRepository.createTaskId()

        val calendarSyncInformation = addTaskToCalendarUseCase.execute(
            AddTaskToCalendarUseCase.AddTaskToCalendarParams(
                taskId = taskId,
                name = addTaskParam.name,
                dueDate = addTaskParam.dueDate,
                attendees = addTaskParam.attendees,
                description = addTaskParam.description
            )
        )

        val task = taskRepository.addTask(
            getSignedInUserUseCase.execute().email,
            TaskRepository.AddTaskParam(
                id = taskId,
                name = addTaskParam.name,
                priority = addTaskParam.priority,
                description = addTaskParam.description,
                dueDate = addTaskParam.dueDate.let {
                    dueDateFormatter.toDueDateInMillis(addTaskParam.dueDate)
                },
                calendarSyncInformation = calendarSyncInformation
            )
        )

        taskRepository.onTaskUpdated(task)
    }

    private fun Set<String>.areAttendeesValid(): Boolean {
        return count {
            !emailPattern.matcher(it).matches()
        } == 0
    }

    data class AddTaskParam(
        val name: String? = null,
        val description: String? = null,
        val dueDate: LocalDateTime? = null,
        val priority: TaskPriority? = null,
        val attendees: Set<String>? = null

    )
}

private const val RequiredNameLength = 3
