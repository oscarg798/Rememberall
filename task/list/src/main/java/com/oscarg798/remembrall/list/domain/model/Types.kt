package com.oscarg798.remembrall.list.domain.model

import com.oscarg798.remembrall.list.ui.TaskCardOptions

internal data class Model(
    val tasks: Map<TaskGroup.MonthGroup, DisplayableTasksGroup>? = null,
    val initialIndex: Int = -1,
    val viewMode: ViewMode = ViewMode.List,
) {

    enum class ViewMode {
        List,
        Grid
    }
}

internal sealed interface Event {
    data object OnAddClicked: Event
    data class OnScrollIndexFound(val index: Int) : Event
    data class OnTasksClicked(val taskId: String) : Event
    data class OnTaskOptionsClicked(val task: DisplayableTask) : Event
    data class OnTaskOptionsFound(val options: List<TaskCardOptions.Option>): Event
    data class OnTasksFound(val tasks: Map<TaskGroup.MonthGroup, DisplayableTasksGroup>) : Event
}

internal sealed interface Effect {
    // TODO: We might want to consider a date range to get the tasks, or include completed
    data object GetTasks : Effect
    data class GetTaskOptions(val task: DisplayableTask) : Effect
    data class GetScrollIndexPosition(val tasks: Map<TaskGroup.MonthGroup, DisplayableTasksGroup>) :
        Effect

    sealed interface UIEffect : Effect {
        data object NavigateToAdd: UIEffect
        data class ScrollToItem(val index: Int) : UIEffect
        data class NavigateToDetail(val taskId: String) : UIEffect
        data class ShowOptions(val options: List<TaskCardOptions.Option>) : UIEffect
    }
}