package com.oscarg798.remembrall.tasklist.model

import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import com.oscarg798.remembrall.common.model.Task

data class TaskGroup(
    val date: TaskDate,
    val itemsByDay: Map<DayGroup, Collection<Task>>
) {

    data class TaskDate(
        val day: String,
        val dayNumber: String,
        val month: String,
        val year: String
    )

    data class DayGroup(val dayName: String, val dayNumber: String){
        override fun toString(): String = "$dayName $dayNumber"
    }

    data class MonthGroup(val name: String, val value: String, val year: String){
        override fun toString() = "${name.capitalize(Locale.current)} $year"
    }
}