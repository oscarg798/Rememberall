package com.oscarg798.remembrall.addtask.usecase

import com.oscarg798.remembrall.addtask.domain.DueDate
import com.oscarg798.remembrall.addtask.domain.Effect
import com.oscarg798.remembrall.addtask.domain.Event
import com.oscarg798.remembrall.dateformatter.DateFormatter
import com.oscarg798.remembrall.task.TaskRepository
import javax.inject.Inject

internal interface GetTask : suspend (Effect.LoadTask) -> Event

internal class GetTaskImpl @Inject constructor(
    private val dateFormatter: DateFormatter,
    private val taskRepository: TaskRepository,
) : GetTask {

    override suspend fun invoke(effect: Effect.LoadTask): Event {
        val task = taskRepository.getTask(effect.taskId)
        return Event.OnTaskLoaded(task, task.dueDate?.let {
            DueDate(
                dateFormatter.toLocalDatetime(it),
                dateFormatter.toDisplayableDate(it, true)
            )
        })
    }
}