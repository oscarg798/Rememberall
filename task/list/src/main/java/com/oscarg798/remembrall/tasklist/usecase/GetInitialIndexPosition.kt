package com.oscarg798.remembrall.tasklist.usecase

import com.oscarg798.remembrall.dateformatter.DateFormatter

import com.oscarg798.remembrall.tasklist.model.TaskGroup
import javax.inject.Inject
import java.util.Calendar

class GetInitialIndexPosition @Inject constructor(private val dueDateFormatter: DateFormatter) {

    operator fun invoke(groupedTask: Map<TaskGroup.MonthGroup, TaskGroup>): Int {
        val currentDate = Calendar.getInstance()
        val currentDateInMillis = currentDate.timeInMillis
        val currentMonthValue = currentDate.get(Calendar.MONTH)
        val currentDay = currentDate.get(Calendar.DAY_OF_MONTH)

        val currentMonthName = dueDateFormatter.getMonthFromDate(currentDateInMillis)

        val currentMonthGroup = TaskGroup.MonthGroup(
            name = currentMonthName,
            value = dueDateFormatter.getMonthNumber(currentMonthName),
            year = currentDate.get(Calendar.YEAR).toString()
        )
        var index = -1

        val groupedTaskAsList = groupedTask.entries.toList().sortedBy { it.key.value.toInt() }

        val monthsNotAfterCurrentMonth = groupedTaskAsList.filter {
            it.key.year <= currentMonthGroup.year && it.key.value.toInt() <= currentMonthValue
        }.sortedBy { it.key.value.toInt() }

        index = monthsNotAfterCurrentMonth.getIndexFromSize()

        monthsNotAfterCurrentMonth.forEach {
            index += if (it.key.value != currentMonthGroup.value) {
                it.value.itemsByDay.values.getIndexFromSize()
            } else {
                it.value.itemsByDay.filter { dayGroupEntry ->
                    dayGroupEntry.key.dayNumber.toInt() < currentDay
                }.values.getIndexFromSize()
            }
        }

        return index
    }

    private fun Collection<Any>.getIndexFromSize() = if (this.isEmpty()) 0 else size - 1
}