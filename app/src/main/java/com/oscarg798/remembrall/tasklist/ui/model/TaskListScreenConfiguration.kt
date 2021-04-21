package com.oscarg798.remembrall.tasklist.ui.model

data class TaskListScreenConfiguration(
    val tasks: List<DisplayableTaskGroup>
) {

    fun isEmpty() = tasks.isEmpty()
}
