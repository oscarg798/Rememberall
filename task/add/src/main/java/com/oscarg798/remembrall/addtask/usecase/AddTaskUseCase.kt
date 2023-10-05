package com.oscarg798.remembrall.addtask.usecase

import com.oscarg798.remembrall.addtask.domain.Effect
import com.oscarg798.remembrall.addtask.domain.Event
import com.oscarg798.remembrall.addtask.domain.Error
import com.oscarg798.remembrall.auth.Session
import com.oscarg798.remembrall.dateformatter.DateFormatter
import com.oscarg798.remembrall.task.TaskRepository
import com.oscarg798.remembrall.user.User
import java.util.regex.Pattern
import javax.inject.Inject

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
            return Event.OnError(Error.InvalidName)
        }

        if (!effect.attendees.areAttendeesValid()) {
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

            taskRepository.onTaskUpdated(task)
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

    private fun Set<String>.areAttendeesValid(): Boolean {
        return count {
            !emailPattern.matcher(it).matches()
        } == 0
    }
}

private const val RequiredNameLength = 3
