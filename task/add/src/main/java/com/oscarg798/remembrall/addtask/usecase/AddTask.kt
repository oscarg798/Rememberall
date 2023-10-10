package com.oscarg798.remembrall.addtask.usecase

import com.oscarg798.remembrall.addtask.domain.Effect
import com.oscarg798.remembrall.addtask.domain.Event
import com.oscarg798.remembrall.addtask.domain.Error
import com.oscarg798.remembrall.auth.Session
import com.oscarg798.remembrall.dateformatter.DateFormatter
import com.oscarg798.remembrall.task.TaskRepository
import com.oscarg798.remembrall.user.User
import javax.inject.Inject

internal interface AddTask : suspend (Effect.SaveTask) -> Event

internal class AddTaskImpl @Inject constructor(
    private val session: Session,
    private val fieldValidator: FieldValidator,
    private val taskRepository: TaskRepository,
    private val dueDateFormatter: DateFormatter,
    private val dateProvider: CurrentDateProvider,
    private val addTaskToCalendarUseCase: AddTaskToCalendarUseCase,
) : AddTask {

    override suspend fun invoke(effect: Effect.SaveTask): Event {
        if (!fieldValidator.isTitleValid(effect.title)) {
            return Event.OnError(Error.InvalidName)
        }

        if (!fieldValidator.areAttendeesValid(effect.attendees)) {
            return Event.OnError(Error.InvalidAttendeesFormat)
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

        val user = runCatching { getUser().email }.getOrNull() ?: return Event.OnError(Error.Auth)

        return runCatching {
            val task = taskRepository.addTask(
                user = user,
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
        }.fold({
            Event.OnTaskSaved
        }, {
            if (it !is Exception) throw it
            Event.OnError(Error.AddingTask)
        })


    }

    private suspend fun getUser(): User {
        val session = session.getLoggedInState()

        require(session is Session.State.LoggedIn) {
            "User must be logged in to add a task"
        }

        return session.user
    }
}


