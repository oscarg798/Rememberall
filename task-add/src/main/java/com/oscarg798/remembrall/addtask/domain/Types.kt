package com.oscarg798.remembrall.addtask.domain

import androidx.compose.runtime.Stable
import com.oscarg798.remembrall.common.model.TaskPriority
import java.time.LocalDateTime

@Stable
internal data class Model(
    val loading: Boolean = false,
    val title: String = "",
    val description: String = "",
    val dueDate: DueDate? = null,
    val priority: TaskPriority? = null,
    val attendees: Set<String> = setOf(),
    val availablePriorities: List<TaskPriority> = listOf(),
)

internal data class DueDate(
    val date: LocalDateTime,
    val displayableDate: String
)

internal sealed interface Event {

    object OnBackClicked : Event
    object OnCloseClicked : Event
    object OnTagActionClicked : Event
    object OnCalendarActionClicked : Event
    object OnAttendeeActionClicked : Event
    object OnTaskPrioritySelectorDismissed : Event
    data class OnTitleChanged(val title: String) : Event
    data class OnDueDateChanged(val dueDate: LocalDateTime) : Event
    data class OnDescriptionChanged(val description: String) : Event
    data class OnPriorityChanged(val priority: TaskPriority) : Event
    data class OnAttendeesChanged(val attendees: Set<String>) : Event
    data class OnDueDatePickerSelectedDateFound(val selectedDate: LocalDateTime) : Event
    data class OnDueDateFormatted(val date: LocalDateTime, val formattedDate: String) : Event
}

internal sealed interface Effect {

    data class FormatDueDate(val dueDate: LocalDateTime) : Effect
    data class GetDueDatePickerSelectedDate(val dueDate: DueDate?) : Effect
    sealed interface UIEffect : Effect {
        object Close : UIEffect
        object ShowAttendeesPicker : UIEffect
        object DismissDueDatePicker : UIEffect
        object DismissAttendeesPicker : UIEffect
        object DismissTaskPriorityPicker : UIEffect
        data class ShowDueDatePicker(val selectedDate: LocalDateTime) : UIEffect
        data class ShowPriorityPicker(val priorities: List<TaskPriority>) : UIEffect
    }
}