package com.oscarg798.remembrall.detail.usecase

import com.oscarg798.remembrall.detail.domain.Effect
import com.oscarg798.remembrall.detail.domain.Event
import com.oscarg798.remembrall.task.TaskRepository
import javax.inject.Inject

internal interface MarkTaskAsCompleted : suspend (Effect.MarkTaskAsCompleted) -> Event

internal class MarkTaskAsCompletedImpl @Inject constructor(
    private val taskRepository: TaskRepository
) : MarkTaskAsCompleted {

    override suspend fun invoke(effect: Effect.MarkTaskAsCompleted): Event {
        return runCatching {
            taskRepository.removeTask(effect.taskId)
        }.map {
            Event.OnTaskMarkedAsCompleted
        }.getOrElse {
            if (it !is Exception) throw it
            Event.OnErrorDeletingTask
        }
    }
}