package com.oscarg798.remembrall.addtask.usecase

import com.oscarg798.remembrall.addtask.domain.Effect
import com.oscarg798.remembrall.addtask.domain.Error
import com.oscarg798.remembrall.addtask.domain.Event
import com.oscarg798.remembrall.dateformatter.DateFormatter
import com.oscarg798.remembrall.task.TaskRepository
import javax.inject.Inject

internal interface UpdateTask : suspend (Effect.UpdateTask) -> Event

internal class UpdateTaskImpl @Inject constructor(
    private val dateFormatter: DateFormatter,
    private val taskRepository: TaskRepository,
    private val fieldValidator: FieldValidator,
    private val addTaskToCalendarUseCase: AddTaskToCalendarUseCase,
    private val updateCalendarInformation: UpdateCalendarInformation,
) : UpdateTask {

    override suspend fun invoke(effect: Effect.UpdateTask): Event {
        if (!fieldValidator.isTitleValid(effect.title)) {
            return Event.OnError(Error.InvalidName)
        }

        if (!fieldValidator.areAttendeesValid(effect.attendees)) {
            return Event.OnError(Error.InvalidAttendeesFormat)
        }

        val task = effect.originalTask

        val calendarSyncInformation =
            if (effect.dueDate != null && task.calendarSyncInformation == null) {
                addTaskToCalendarUseCase.execute(
                    AddTaskToCalendarUseCase.AddTaskToCalendarParams(
                        taskId = task.id,
                        name = effect.title,
                        dueDate = effect.dueDate.date,
                        attendees = effect.attendees,
                        description = effect.description
                    )
                )
            } else if (effect.dueDate != null) {
                val calendarSyncInformation = task.calendarSyncInformation!!
                updateCalendarInformation(
                    task.title,
                    task.description,
                    calendarSyncInformation,
                    effect.dueDate,
                    calendarSyncInformation.attendees?.map { it.email }?.toSet() ?: emptySet()
                )
            } else if (task.calendarSyncInformation == null) {
                null
            } else {
                //TODO: support due date removal
                return Event.OnError(Error.CanNotRemoveDueDateWhileUpdating)
            }

        taskRepository.update(
            task.copy(
                title = effect.title,
                description = effect.description,
                priority = effect.priority,
                calendarSyncInformation = calendarSyncInformation,
                dueDate = effect.dueDate?.date?.let { dateFormatter.toMillis(it) }
            )
        )

        return Event.OnTaskSaved
    }
}