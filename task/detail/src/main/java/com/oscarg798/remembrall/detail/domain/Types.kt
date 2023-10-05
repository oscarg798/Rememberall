package com.oscarg798.remembrall.detail.domain

internal data class Model(
    val taskId: String,
    val loading: Boolean = false,
    val task: DisplayableTask? = null
)

internal sealed interface Event {
    data object OnEditActionClicked : Event
    data object OnBackButtonClicked : Event
    data object OnDeleteButtonClicked : Event
    data object OnTaskMarkedAsCompleted : Event
    data object OnErrorDeletingTask : Event
    data class OnDisplayableTaskFound(val task: DisplayableTask) : Event
}

sealed interface Effect {

    data class GetTask(val id: String) : Effect
    data class MarkTaskAsCompleted(val taskId: String): Effect

    sealed interface UIEffect : Effect {
        data object CloseScreen : UIEffect
        data class ShowError(val error: Error) : UIEffect
        data class NavigateToEdit(val taskId: String) : UIEffect
    }
}

sealed interface Error {

    object ErrorDeletingTask : Error
}