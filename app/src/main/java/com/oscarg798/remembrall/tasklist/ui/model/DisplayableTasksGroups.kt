package com.oscarg798.remembrall.tasklist.ui.model

data class DisplayableTasksGroups(
    val nonExpirable: Collection<DisplayableTask>? = null,
    val todayTasks: Collection<DisplayableTask>? = null,
    val upComingTasks: Collection<DisplayableTask>? = null,
    val expiredTasks: Collection<DisplayableTask>? = null
)
