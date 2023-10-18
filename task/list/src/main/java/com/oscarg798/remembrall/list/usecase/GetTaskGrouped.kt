package com.oscarg798.remembrall.list.usecase

import com.oscarg798.remembrall.dateformatter.DateFormatter
import com.oscarg798.remembrall.list.domain.model.DisplayableTasksGroup
import com.oscarg798.remembrall.list.domain.model.Effect
import com.oscarg798.remembrall.list.domain.model.Event
import com.oscarg798.remembrall.task.Task
import com.oscarg798.remembrall.list.domain.model.TaskGroup
import java.util.SortedMap
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map

internal interface GetTaskGrouped : suspend (Effect, (Event) -> Unit) -> Unit

internal class GetTaskGroupedImpl @Inject constructor(
    private val getTasksUseCase: GetTasksUseCase,
    private val dateFormatter: DateFormatter
) : GetTaskGrouped {

    override suspend fun invoke(effect: Effect, output: (Event) -> Unit) {
        require(effect is Effect.GetTasks)
        getTasksUseCase().map { tasks ->
            val groups = HashMap<TaskGroup.MonthGroup, TaskGroup>()
            tasks.filter { it.dueDate != null }.forEach { task ->
                val taskDate = TaskGroup.TaskDate(
                    day = dateFormatter.getDayNameFromDate(task.dueDate!!),
                    month = dateFormatter.getMonthFromDate(task.dueDate!!),
                    year = dateFormatter.getYearFromDate(task.dueDate!!),
                    dayNumber = dateFormatter.getDayFromDate(task.dueDate!!)
                )

                val groupLabel = TaskGroup.MonthGroup(
                    name = taskDate.month,
                    value = dateFormatter.getMonthNumber(taskDate.month),
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

            groups
        }.collectLatest { groups->
            output(
                Event.OnTasksFound(
                    groups.entries.associate {
                        it.key to DisplayableTasksGroup(it.value, dateFormatter)
                    }.toSortedMap { first, second ->
                        convertMonthStringToIntPosition(first)
                            .compareTo(convertMonthStringToIntPosition(second))
                    }
                )
            )
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
