package com.oscarg798.remembrall.tasklist.model

import com.oscarg798.remembrall.common.formatter.DueDateFormatter
import com.oscarg798.remembrall.common.model.DisplayableTask

data class DisplayableTasksGroup(
    val date: TaskGroup.TaskDate,
    val itemsByDay: Map<TaskGroup.DayGroup, Collection<DisplayableTask>>
) {

    constructor(
        taskGroup: TaskGroup,
        dueDateFormatter: DueDateFormatter
    ) : this(date = taskGroup.date, taskGroup.itemsByDay.map { entry ->
        entry.key to entry.value.map { DisplayableTask(it, dueDateFormatter) }
    }.toMap().toSortedMap { first, second ->
        first.dayNumber.compareTo(second.dayNumber)
    })
}