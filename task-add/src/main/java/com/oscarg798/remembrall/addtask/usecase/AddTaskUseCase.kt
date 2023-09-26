package com.oscarg798.remembrall.addtask.usecase

import com.oscarg798.remembrall.addtask.domain.Effect
import com.oscarg798.remembrall.addtask.domain.Event
import com.oscarg798.remembrall.addtask.domain.ValidationError
import com.oscarg798.remembrall.auth.Session
import com.oscarg798.remembrall.task.TaskRepository
import com.oscarg798.remembrall.common_addedit.usecase.AddTaskToCalendarUseCase
import com.oscarg798.remembrall.dateformatter.DateFormatter
import com.oscarg798.remembrall.user.User
import javax.inject.Inject
import java.util.regex.Pattern

internal interface AddTaskUseCase : suspend (Effect.SaveTask) -> Event

internal class AddTaskUseCaseImpl @Inject constructor(
    private val session: Session,
    private val emailPattern: Pattern,
    private val taskRepository: TaskRepository,
    private val dateProvider: CurrentDateProvider,
    private val dueDateFormatter: DateFormatter,
    private val addTaskToCalendarUseCase: AddTaskToCalendarUseCase,
) : AddTaskUseCase {

    override suspend fun invoke(effect: Effect.SaveTask): Event {
        if (effect.title.length < RequiredNameLength) {
            return Event.OnValidationError(ValidationError.NameWrongLength)
        }

        if (!effect.attendees.areAttendeesValid()) {
            return Event.OnValidationError(ValidationError.AttendeesNotValid)
        }

        val taskId = taskRepository.createTaskId()

        val calendarSyncInformation = if (effect.dueDate != null) {
            addTaskToCalendarUseCase.execute(
                AddTaskToCalendarUseCase.AddTaskToCalendarParams(
                    taskId = taskId,
                    name = effect.title,
                    dueDate = effect.dueDate.date,
                    attendees = effect.attendees,
                    description = effect.description
                )
            )
        } else null

        val task = taskRepository.addTask(
            user = getUser().email,
            addTaskParam = TaskRepository.AddTaskParam(
                id = taskId,
                name = effect.title,
                priority = effect.priority,
                description = effect.description,
                dueDate = effect.dueDate?.date?.let { dueDateFormatter.toMillis(it) },
                calendarSyncInformation = calendarSyncInformation,
                createdAt = dueDateFormatter.toMillis(dateProvider())
            )
        )

        taskRepository.onTaskUpdated(task)

        return Event.OnTaskSaved
    }

    private suspend fun getUser(): User {
        val session = session.getLoggedInState()

        if (session !is Session.State.LoggedIn) {
            throw IllegalStateException("Must be Logged In at this Point")
        }

        return session.user
    }

    private fun Set<String>.areAttendeesValid(): Boolean {
        return count {
            !emailPattern.matcher(it).matches()
        } == 0
    }
}

private const val RequiredNameLength = 3
