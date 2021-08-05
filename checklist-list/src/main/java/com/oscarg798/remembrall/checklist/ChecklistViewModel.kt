package com.oscarg798.remembrall.checklist

import com.oscarg798.remembrall.checklist.usecase.GetChecklists
import com.oscarg798.remembrall.common.coroutines.CoroutineContextProvider
import com.oscarg798.remembrall.common.viewmodel.AbstractViewModel
import com.oscarg798.remembrall.common.viewmodel.launch
import com.oscarg798.remembrall.common_checklist.model.Checklist
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

@HiltViewModel
class ChecklistViewModel @Inject constructor(
    private val getChecklists: GetChecklists,
    coroutineContextProvider: CoroutineContextProvider
) : AbstractViewModel<ChecklistViewModel.ViewState, ChecklistViewModel.Event>(ViewState()),
    CoroutineContextProvider by coroutineContextProvider {


    fun fetchChecklists() {
        launch {
            update { it.copy(loading = true) }
            val checkLists = withContext(io) {
                getChecklists()
            }

            update { it.copy(loading = false, checklists = checkLists) }
        }
    }

    fun onAddRequested() {
        _event.tryEmit(Event.NavigateToAdd)
    }

    fun onChecklistClicked(checklist: Checklist){
        _event.tryEmit(Event.ShowDetail(checklistId = checklist.id))
    }

    data class ViewState(
        val loading: Boolean = false,
        val checklists: Collection<Checklist>? = null
    )

    sealed interface Event {
        class ShowDetail(val checklistId: String): Event
        object NavigateToAdd : Event
    }
}