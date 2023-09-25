package com.oscarg798.remembrall.addtask.domain

import androidx.compose.runtime.Stable
import com.oscarg798.remembrall.task.TaskPriority
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

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

    object OnTaskSaved : Event
    object OnBackClicked : Event
    object OnCloseClicked : Event
    object OnTagActionClicked : Event
    object OnSaveActionClicked : Event
    object OnCalendarActionClicked : Event
    object OnAttendeeActionClicked : Event
    object OnTaskPrioritySelectorDismissed : Event
    data class OnTitleChanged(val title: String) : Event
    data class OnAttendeesChanged(val attendee: String) : Event
    data class OnValidationError(val error: ValidationError): Event
    data class OnDescriptionChanged(val description: String) : Event
    data class OnPriorityChanged(val priority: TaskPriority) : Event
    data class OnTaskPrioritiesFound(val priorities: List<TaskPriority>) : Event
    data class OnDueDatePickerInitialDateFound(val initialDate: LocalDateTime) : Event
    data class OnDueDateDateAndTimeSelected(val date: LocalDate, val time: LocalTime) : Event
    data class OnDueDateFormatted(val date: LocalDateTime, val formattedDate: String) : Event
}

internal sealed interface Effect {

    data class SaveTask(
        val title: String,
        val description: String,
        val dueDate: DueDate? = null,
        val priority: TaskPriority? = null,
        val attendees: Set<String> = setOf(),
    ) : Effect

    data class FormatDueDate(val date: LocalDate, val time: LocalTime) : Effect
    data class GetDueDatePickerInitialDate(val dueDate: DueDate?) : Effect
    data class GetAvailableTaskPriorities(val selectedPriority: TaskPriority? = null) : Effect

    sealed interface UIEffect : Effect {
        object Close : UIEffect
        object ShowAttendeesPicker : UIEffect
        object DismissDueDatePicker : UIEffect
        object DismissAttendeesPicker : UIEffect
        object DismissTaskPriorityPicker : UIEffect
        data class ShowDueDateDatePicker(val initialDateTime: LocalDateTime) : UIEffect
        data class ShowPriorityPicker(val priorities: List<TaskPriority>) : UIEffect
        data class ShowError(val error: ValidationError) : UIEffect
    }
}

internal sealed interface ValidationError {

    object NameWrongLength : ValidationError
    object AttendeesNotValid : ValidationError

}