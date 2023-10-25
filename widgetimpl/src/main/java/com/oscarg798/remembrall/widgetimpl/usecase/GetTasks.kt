package com.oscarg798.remembrall.widgetimpl.usecase

import com.oscarg798.remembrall.dateformatter.DateFormatter
import com.oscarg798.remembrall.task.TaskRepository
import com.oscarg798.remembrall.widget.RemembrallWidgetState
import com.oscarg798.remembrall.widget.RemembrallWidgetUpdater
import com.oscarg798.remembrall.widgetimpl.domain.DisplayableTask
import com.oscarg798.remembrall.widgetimpl.domain.Effect
import com.oscarg798.remembrall.widgetimpl.domain.Event
import java.time.LocalDateTime
import java.util.Calendar
import javax.inject.Inject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

internal interface GetTasks : suspend (Effect, (Event) -> Unit) -> Unit

internal class GetTaskImpl @Inject constructor(
    private val dateFormatter: DateFormatter,
    private val getTaskWindow: GetTaskWindow,
    private val taskRepository: TaskRepository,
) : GetTasks {

    override suspend fun invoke(
        effect: Effect,
        output: (Event) -> Unit
    ) {
        require(effect is Effect.GetTasks)
        val taskWindow = getTaskWindow()
        return taskRepository.streamTasks(
            listOf(
                TaskRepository.TaskQuery.UserEquals(effect.user),
                TaskRepository.TaskQuery.DueDateAfter(taskWindow.start),
                TaskRepository.TaskQuery.DueDateBefore(taskWindow.end),
                TaskRepository.TaskQuery.Completed(false)
            ),
            TaskRepository.QueryOperation.And
        ).map {
            it.map { task ->
                DisplayableTask(
                    id = task.id,
                    title = task.title,
                    description = task.description,
                    dueDate = task.dueDate?.let { dueDate ->
                        dateFormatter.toDisplayableDate(dueDate, true)
                    },
                )
            }
        }.collectLatest {
            output(
                Event.OnTaskFound(task = it, taskWindow = taskWindow)
            )
        }
    }
}
