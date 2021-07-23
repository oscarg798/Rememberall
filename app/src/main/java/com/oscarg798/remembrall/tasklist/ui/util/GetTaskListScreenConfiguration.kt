package com.oscarg798.remembrall.tasklist.ui.util

import com.oscarg798.remembrall.R
import com.oscarg798.remembrall.common.formatters.DueDateFormatter
import com.oscarg798.remembrall.common.model.Task
import com.oscarg798.remembrall.common.provider.StringProvider
import com.oscarg798.remembrall.tasklist.ui.model.DisplayableTask
import com.oscarg798.remembrall.tasklist.ui.model.DisplayableTaskGroup
import com.oscarg798.remembrall.tasklist.ui.model.DisplayableTasksGroups
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

class GetDisplayableTasks @Inject constructor(
    private val dueDateFormatter: DueDateFormatter,
    private val stringProvider: StringProvider
) {

    private val todayCalendar = Calendar.getInstance()
    private val utilCalendar = Calendar.getInstance()

    fun execute(tasks: Collection<Task>): List<DisplayableTaskGroup> {
        var displayableTaskGroups = DisplayableTasksGroups()

        tasks.forEach { task ->
            displayableTaskGroups = when {
                task.dueDate == null -> addToNonExpirableGroup(displayableTaskGroups, task)
                isDueToday(task) -> addToTodayGroup(displayableTaskGroups, task)
                isAlreadyExpired(task) -> addToAlreadyExpiredGroup(displayableTaskGroups, task)
                else -> addToComingGroup(displayableTaskGroups, task)
            }
        }

        displayableTaskGroups = sortTasks(displayableTaskGroups)

        return arrayListOf<DisplayableTaskGroup>().apply {
            if (displayableTaskGroups.nonExpirable != null) {
                add(
                    DisplayableTaskGroup(
                        stringProvider.get(R.string.non_expirable_label),
                        displayableTaskGroups.nonExpirable!!.toList()
                    )
                )
            }
            if (displayableTaskGroups.todayTasks != null) {
                add(
                    DisplayableTaskGroup(
                        stringProvider.get(R.string.today_due_date_label),
                        displayableTaskGroups.todayTasks!!.toList()
                    )
                )
            }
            if (displayableTaskGroups.upComingTasks != null) {
                add(
                    DisplayableTaskGroup(
                        stringProvider.get(R.string.comming_label),
                        displayableTaskGroups.upComingTasks!!.toList()
                    )
                )
            }
            if (displayableTaskGroups.expiredTasks != null) {
                add(
                    DisplayableTaskGroup(
                        stringProvider.get(R.string.expired_label),
                        displayableTaskGroups.expiredTasks!!.toList()
                    )
                )
            }
        }
    }

    private fun sortTasks(displayableTasksGroups: DisplayableTasksGroups) =
        displayableTasksGroups.copy(
            todayTasks = displayableTasksGroups.todayTasks?.let { displayableTasks ->
                sortBasedOnPriority(displayableTasks)
            },
            expiredTasks = displayableTasksGroups.expiredTasks?.let { displayableTasks ->
                sortDueDateAscending(displayableTasks)
            },
            upComingTasks = displayableTasksGroups.upComingTasks?.let { displayableTasks ->
                sortDueDateAscending(displayableTasks)
            },
            nonExpirable = displayableTasksGroups.nonExpirable?.let { displayableTasks ->
                sortBasedOnPriority(displayableTasks)
            }
        )

    private fun sortDueDateAscending(displayableTasks: Collection<DisplayableTask>) =
        displayableTasks.sortedBy { it.dueDate }

    private fun sortBasedOnPriority(displayableTasks: Collection<DisplayableTask>) =
        displayableTasks.sortedWith { first, second ->
            first.priority.compareTo(second.priority)
        }

    private fun addToNonExpirableGroup(
        displayableTasksGroups: DisplayableTasksGroups,
        task: Task
    ) = displayableTasksGroups.copy(
        nonExpirable = arrayListOf<DisplayableTask>().apply {
            add(getDisplayableTask(task))
            displayableTasksGroups.nonExpirable?.let {
                addAll(it)
            }
        }
    )

    private fun addToTodayGroup(
        displayableTasksGroups: DisplayableTasksGroups,
        task: Task
    ) = displayableTasksGroups.copy(
        todayTasks = arrayListOf<DisplayableTask>().apply {
            add(getDisplayableTask(task))
            displayableTasksGroups.todayTasks?.let {
                addAll(it)
            }
        }
    )

    private fun addToAlreadyExpiredGroup(
        displayableTasksGroups: DisplayableTasksGroups,
        task: Task
    ) = displayableTasksGroups.copy(
        expiredTasks = arrayListOf<DisplayableTask>().apply {
            add(getDisplayableTask(task))
            displayableTasksGroups.expiredTasks?.let {
                addAll(it)
            }
        }
    )

    private fun addToComingGroup(
        displayableTasksGroups: DisplayableTasksGroups,
        task: Task
    ) = displayableTasksGroups.copy(
        upComingTasks = arrayListOf<DisplayableTask>().apply {
            add(getDisplayableTask(task))
            displayableTasksGroups.upComingTasks?.let {
                addAll(it)
            }
        }
    )

    private fun getDisplayableTask(task: Task) = DisplayableTask(
        id = task.id,
        name = task.name,
        description = task.description,
        priority = task.priority,
        completed = task.completed,
        calendarSynced = task.calendarSyncInformation?.synced ?: false,
        dueDate = task.dueDate?.let {
            getDisplayableDueDate(task.dueDate)
        }
    )

    private fun getDisplayableDueDate(dueDate: Long): String {
        return dueDateFormatter.toDisplayableDate(dueDate)
    }

    private fun isDueToday(task: Task): Boolean {
        utilCalendar.time = Date(task.dueDate!!)

        return todayCalendar.get(Calendar.DAY_OF_MONTH) ==
            utilCalendar.get(Calendar.DAY_OF_MONTH) &&
            todayCalendar.get(Calendar.MONTH) == utilCalendar.get(Calendar.MONTH) &&
            todayCalendar.get(Calendar.YEAR) == utilCalendar.get(Calendar.YEAR)
    }

    private fun isAlreadyExpired(task: Task): Boolean {
        utilCalendar.time = Date(task.dueDate!!)

        return todayCalendar.get(Calendar.DAY_OF_MONTH) >=
            utilCalendar.get(Calendar.DAY_OF_MONTH) &&
            todayCalendar.get(Calendar.MONTH) >= utilCalendar.get(Calendar.MONTH) &&
            todayCalendar.get(Calendar.YEAR) >= utilCalendar.get(Calendar.YEAR)
    }
}
