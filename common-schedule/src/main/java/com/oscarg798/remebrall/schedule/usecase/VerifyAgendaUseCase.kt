package com.oscarg798.remebrall.schedule.usecase

import android.util.Log
import com.oscarg798.remebrall.schedule.R
import com.oscarg798.remebrall.schedule.notification.NotificationScheduler
import com.oscarg798.remembrall.common.model.Task
import com.oscarg798.remembrall.common.model.TaskPriority
import com.oscarg798.remembrall.common.provider.StringProvider
import javax.inject.Inject

class VerifyAgendaUseCase @Inject constructor(
    private val getTodayScheduleUseCase: GetTodayScheduleUseCase,
    private val notificationScheduler: NotificationScheduler,
    private val stringProvider: StringProvider
) {

    suspend fun execute() {
        val tasks = getTodayScheduleUseCase.execute()

        Log.i("Scheduler", "Verifying")
        notificationScheduler.schedule(
            if (tasks.isEmpty()) {
                getSmallNotificationType()
            } else {
                getBigNotificationType(tasks)
            }
        )
    }

    private fun getBigNotificationType(
        tasks:
            Collection<Task>
    ): NotificationScheduler.Type.BigText {
        val taskCountByPriority = getTaskCountByPriorityLabel(tasks)

        return NotificationScheduler.Type.BigText(
            title = stringProvider.get(R.string.big_notification_title),
            content = stringProvider.get(R.string.busy_agenda_notification_content),
            subtext = String.format(
                stringProvider.get(R.string.busy_agenda_notification_subtext),
                tasks.size.toString()
            ),
            subTitle = stringProvider.get(R.string.busy_agenda_notification_subtitle),
            bigText = String.format(
                stringProvider.get(R.string.busy_agenda_notification_big_text),
                taskCountByPriority
            )
        )
    }

    private fun getSmallNotificationType() = NotificationScheduler.Type.Small(
        title = stringProvider.get(R.string.small_notification_title),
        content = stringProvider.get(R.string.free_agenda_notification_content),
        subtext = stringProvider.get(R.string.free_agenda_notification_subtext),
        subTitle = stringProvider.get(R.string.free_agenda_notification_subtitle)
    )

    private fun getTaskCountByPriorityLabel(
        tasks: Collection<Task>
    ): String {
        val urgentTasksCount = tasks.count { it.priority == TaskPriority.Urgent }
        val highTaskCount = tasks.count { it.priority == TaskPriority.High }
        val mediumTaskCount = tasks.count { it.priority == TaskPriority.Medium }
        val lowTaskCount = tasks.count { it.priority == TaskPriority.Low }

        return listOf(
            getTaskCountLabel(TaskPriority.Urgent, urgentTasksCount),
            getTaskCountLabel(TaskPriority.High, highTaskCount),
            getTaskCountLabel(TaskPriority.Medium, mediumTaskCount),
            getTaskCountLabel(TaskPriority.Low, lowTaskCount)
        ).joinToString(TaskCountSeparator)
    }

    private fun getTaskCountLabel(
        priority: TaskPriority,
        taskCount: Int
    ) = if (taskCount != NoTaskByPriorityCount) {
        String.format(
            stringProvider.get(R.string.tasks_by_priority_bullet_point),
            taskCount.toString(),
            stringProvider.get(getPriorityLabel(priority))
        )
    } else {
        String.format(
            stringProvider.get(R.string.no_tasks_by_priority_bullet_point),
            stringProvider.get(getPriorityLabel(priority))
        )
    }

    private fun getPriorityLabel(priority: TaskPriority) = when (priority) {
        TaskPriority.High -> R.string.high_task_priority_label
        TaskPriority.Low -> R.string.low_task_priority_label
        TaskPriority.Medium -> R.string.medium_task_priority_label
        TaskPriority.Urgent -> R.string.urgent_task_priority_label
    }
}

private const val TaskCountSeparator = "\n"
private const val NoTaskByPriorityCount = 0
