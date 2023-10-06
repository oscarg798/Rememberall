package com.oscarg798.remembrall.detail.usecase

import com.oscarg798.remembrall.dateformatter.DateFormatter
import com.oscarg798.remembrall.detail.domain.DisplayableTask
import com.oscarg798.remembrall.detail.domain.Effect
import com.oscarg798.remembrall.detail.domain.Event
import com.oscarg798.remembrall.task.TaskRepository
import javax.inject.Inject

internal interface GetTask : suspend (Effect.GetTask) -> Event.OnDisplayableTaskFound

internal class GetTaskImpl @Inject constructor(
    private val dateFormatter: DateFormatter,
    private val taskRepository: TaskRepository,
) : GetTask {

    override suspend fun invoke(effect: Effect.GetTask): Event.OnDisplayableTaskFound {
        val task = taskRepository.getTask(effect.id)
        return Event.OnDisplayableTaskFound(
            DisplayableTask(
                id = task.id,
                owned = task.owned,
                title = task.title,
                attendees = task.calendarSyncInformation?.attendees?.map { it.email }?.toSet()
                    ?: emptySet(),
                description = task.description,
                priority = task.priority,
                completed = task.completed,
                dueDate = task.dueDate?.let {
                    dateFormatter.toDisplayableDate(it)
                }
            )
        )
    }
}