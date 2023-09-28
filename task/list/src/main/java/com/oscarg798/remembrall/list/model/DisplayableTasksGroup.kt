package com.oscarg798.remembrall.list.model

import com.oscarg798.remembrall.dateformatter.DateFormatter

data class DisplayableTasksGroup(
    val date: TaskGroup.TaskDate,
    val itemsByDay: Map<TaskGroup.DayGroup, Collection<DisplayableTask>>
) {

    constructor(
        taskGroup: TaskGroup,
        dueDateFormatter: DateFormatter
    ) : this(
        date = taskGroup.date,
        taskGroup.itemsByDay.map { entry ->
            entry.key to entry.value.map { DisplayableTask(it, dueDateFormatter) }
        }.toMap().toSortedMap { first, second ->
            first.dayNumber.compareTo(second.dayNumber)
        }
    )
}
