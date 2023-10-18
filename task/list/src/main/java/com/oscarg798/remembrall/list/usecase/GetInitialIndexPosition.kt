package com.oscarg798.remembrall.list.usecase

import com.oscarg798.remembrall.dateformatter.DateFormatter
import com.oscarg798.remembrall.list.domain.model.DisplayableTasksGroup
import com.oscarg798.remembrall.list.domain.model.Effect
import com.oscarg798.remembrall.list.domain.model.Event
import com.oscarg798.remembrall.list.domain.model.TaskGroup
import java.util.Calendar
import javax.inject.Inject

internal interface GetInitialIndexPosition :
        (Effect.GetScrollIndexPosition) -> Event.OnScrollIndexFound

internal class GetInitialIndexPositionImpl @Inject constructor(
    private val dueDateFormatter: DateFormatter
) : GetInitialIndexPosition {

    override fun invoke(effect: Effect.GetScrollIndexPosition): Event.OnScrollIndexFound {
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

        val groupedTaskAsList = effect.tasks.entries.toList().sortedBy { it.key.value.toInt() }

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

        return Event.OnScrollIndexFound(index)
    }

    private fun Collection<Any>.getIndexFromSize() = if (this.isEmpty()) 0 else size - 1
}
