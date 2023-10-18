package com.oscarg798.remembrall.detail.usecase

import com.oscarg798.rememberall.task.descriptionformatter.DescriptionFormatter
import com.oscarg798.remembrall.dateformatter.DateFormatter
import com.oscarg798.remembrall.detail.domain.DisplayableTask
import com.oscarg798.remembrall.detail.domain.Effect
import com.oscarg798.remembrall.detail.domain.Event
import com.oscarg798.remembrall.task.TaskRepository
import java.io.DataOutput
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map

internal interface GetTask : suspend (Effect, (Event) -> Unit) -> Unit

internal class GetTaskImpl @Inject constructor(
    private val dateFormatter: DateFormatter,
    private val taskRepository: TaskRepository,
    private val descriptionFormatter: DescriptionFormatter,
) : GetTask {

    override suspend fun invoke(effect: Effect, output: (Event) -> Unit) {
        require(effect is Effect.GetTask)
        taskRepository.streamTask(effect.id).map { task ->
            Event.OnDisplayableTaskFound(
                DisplayableTask(
                    id = task.id,
                    owned = task.owned,
                    title = task.title,
                    attendees = task.calendarSyncInformation?.attendees?.map { it.email }?.toSet()
                        ?: emptySet(),
                    description = task.description?.let { descriptionFormatter(it) },
                    priority = task.priority,
                    completed = task.completed,
                    dueDate = task.dueDate?.let {
                        dateFormatter.toDisplayableDate(it)
                    }
                )
            )
        }.collectLatest { output(it) }
    }
}