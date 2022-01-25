package com.oscarg798.remembrall.tasklist.usecase

import com.oscarg798.remembrall.common.formatter.DueDateFormatter
import com.oscarg798.remembrall.common.model.Task
import com.oscarg798.remembrall.tasklist.model.TaskGroup
import javax.inject.Inject
import java.util.SortedMap
import java.util.TreeMap

class GetTaskGrouped @Inject constructor(
    private val getTaskUseCase: GetTaskUseCase,
    private val dueDateFormatter: DueDateFormatter,
    private val monthPositionMapper: MonthPositionMapper
) {

    suspend operator fun invoke(): SortedMap<TaskGroup.MonthGroup, TaskGroup> {
        val tasks = getTaskUseCase.execute()
        val groups = HashMap<TaskGroup.MonthGroup, TaskGroup>()
        tasks.forEach { task ->
            val taskDate = TaskGroup.TaskDate(
                day = dueDateFormatter.getDayNameFromDueDate(task.dueDate),
                month = dueDateFormatter.getMonthFromDueDate(task.dueDate),
                year = dueDateFormatter.getYearFromDueDate(task.dueDate),
                dayNumber = dueDateFormatter.getDayFromDueDate(task.dueDate)
            )

            val groupLabel = TaskGroup.MonthGroup(
                name = taskDate.month,
                value = dueDateFormatter.getMonthNumber(taskDate.month),
                year = taskDate.year
            )
            val group = groups[groupLabel]
                ?: TaskGroup(taskDate, mapOf())
            val itemsByDay = group.itemsByDay
            val dayGroup = TaskGroup.DayGroup(
                dayName = taskDate.day,
                dayNumber = taskDate.dayNumber
            )
            groups[groupLabel] = if (itemsByDay.containsKey(dayGroup)) {
                val currentTasks = itemsByDay[dayGroup]!!.toMutableList()
                currentTasks.add(task)
                val currentItems = group.itemsByDay.toMutableMap()
                currentItems[dayGroup] = currentTasks
                sortBasedOnDay(currentItems)
                group.copy(itemsByDay = currentItems)
            } else {
                val currentItems = group.itemsByDay.toMutableMap()
                currentItems[dayGroup] = listOf(task)
                sortBasedOnDay(currentItems = currentItems)
                group.copy(itemsByDay = currentItems)
            }
        }

        return  groups.toSortedMap() { first, second ->
            convertMonthStringToIntPosition(first).compareTo(convertMonthStringToIntPosition(second))
        }
    }

    private fun sortBasedOnDay(currentItems: MutableMap<TaskGroup.DayGroup, Collection<Task>>) {
        currentItems.toSortedMap { first, second ->
            first.dayNumber.compareTo(second.dayNumber)
        }
    }

    private fun convertMonthStringToIntPosition(
        monthGroup: TaskGroup.MonthGroup,
    ): Int {
        return "${
            if (monthGroup.value.length == 1) {
                "0${monthGroup.value}"
            } else {
                monthGroup.value
            }
        }${monthGroup.year}".toInt()
    }
}

private const val January = "January"
private const val February = "February"
private const val March = "March"
private const val April = "April"
private const val May = "May"
private const val June = "June"
private const val July = "July"
private const val August = "August"
private const val September = "September"
private const val October = "October"
private const val November = "November"
private const val December = "December"
