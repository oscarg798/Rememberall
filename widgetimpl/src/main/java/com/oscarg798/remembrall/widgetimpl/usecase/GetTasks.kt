package com.oscarg798.remembrall.widgetimpl.usecase

import com.oscarg798.remembrall.dateformatter.DateFormatter
import com.oscarg798.remembrall.task.TaskRepository
import com.oscarg798.remembrall.widgetimpl.domain.DisplayableTask
import com.oscarg798.remembrall.widgetimpl.domain.Effect
import com.oscarg798.remembrall.widgetimpl.domain.Event
import javax.inject.Inject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map

internal interface GetTasks : suspend (Effect, (Event) -> Unit) -> Unit

internal class GetTaskImpl @Inject constructor(
    private val dateFormatter: DateFormatter,
    private val taskRepository: TaskRepository,
) : GetTasks {

    override suspend fun invoke(
        effect: Effect,
        output: (Event) -> Unit
    ) {
        require(effect is Effect.GetTasks)
        taskRepository.streamTasks(effect.user)
            .map {
                it.take(3).map { task ->
                    DisplayableTask(
                        id = task.id,
                        title = task.title,
                        owned = task.owned,
                        description = task.description,
                        dueDate = task.dueDate?.let { dateFormatter.toDisplayableDate(it) },
                    )
                }
            }
            .collectLatest {
            output(
                Event.OnTaskFound(it )
            )
        }
    }
}