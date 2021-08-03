package com.oscarg798.remembrall.addtask.usecase

import com.oscarg798.remembrall.addtask.exception.AddTaskException
import com.oscarg798.remembrall.common.formatter.DueDateFormatter
import com.oscarg798.remembrall.common.model.TaskPriority
import com.oscarg798.remembrall.common.repository.domain.TaskRepository
import com.oscarg798.remembrall.common_addedit.usecase.AddTaskToCalendarUseCase
import com.oscarg798.remembrall.common_auth.repository.domain.AuthRepository
import dagger.Reusable
import java.time.LocalDateTime
import java.util.regex.Pattern
import javax.inject.Inject

@Reusable
class AddTaskUseCase @Inject constructor(
    private val addTaskToCalendarUseCase: AddTaskToCalendarUseCase,
    private val dueDateFormatter: DueDateFormatter,
    private val taskRepository: TaskRepository,
    private val authRepository: AuthRepository,
    private val emailPattern: Pattern
) {

    suspend fun execute(addTaskParam: AddTaskParam) {
        if (addTaskParam.name == null || addTaskParam.name.length < RequiredNameLength) {
            throw AddTaskException.MissingName
        }

        if (addTaskParam.priority == null) {
            throw AddTaskException.MissingPriority
        }

        if (addTaskParam.attendees != null && !addTaskParam.attendees.areAttendeesValid()) {
            throw AddTaskException.AttendeesWrongFormat
        }

        if (addTaskParam.attendees != null && addTaskParam.dueDate == null) {
            throw AddTaskException.AttendeesRequiredDueDate
        }

        val task = taskRepository.addTask(
            TaskRepository.AddTaskParam(
                name = addTaskParam.name,
                priority = addTaskParam.priority,
                description = addTaskParam.description,
                dueDate = addTaskParam.dueDate?.let {
                    dueDateFormatter.toDueDateInMillis(addTaskParam.dueDate)
                }
            )
        )

        if (!shouldTaskBeSynced(addTaskParam)) {
            return
        }

        require(addTaskParam.dueDate != null)

        addTaskToCalendarUseCase.execute(task, addTaskParam.dueDate, addTaskParam.attendees)
    }

    private fun shouldTaskBeSynced(addTaskParam: AddTaskParam): Boolean {
        val userSignedIn = runCatching { authRepository.getSignedInUser() }
            .map { true }
            .getOrElse { false }
        return addTaskParam.dueDate != null && userSignedIn
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
