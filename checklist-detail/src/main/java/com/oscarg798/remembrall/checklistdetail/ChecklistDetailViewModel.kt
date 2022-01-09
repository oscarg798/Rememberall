package com.oscarg798.remembrall.checklistdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.oscarg798.remembrall.checklistdetail.usecase.GetChecklistById
import com.oscarg798.remembrall.checklistdetail.usecase.RemoveChecklistItem
import com.oscarg798.remembrall.checklistdetail.usecase.UpdateCheckList
import com.oscarg798.remembrall.common.coroutines.CoroutineContextProvider
import com.oscarg798.remembrall.common.provider.StringProvider
import com.oscarg798.remembrall.common.viewmodel.AbstractViewModel
import com.oscarg798.remembrall.common.viewmodel.launch
import com.oscarg798.remembrall.common_checklist.AddChecklistException
import com.oscarg798.remembrall.common_checklist.GetChecklistItem
import com.oscarg798.remembrall.common_checklist.UpdateChecklistException
import com.oscarg798.remembrall.common_checklist.model.Checklist
import com.oscarg798.remembrall.common_checklist.model.ChecklistItem
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.withContext

class ChecklistDetailViewModel @AssistedInject constructor(
    @Assisted private val checklistId: String,
    private val getChecklistById: GetChecklistById,
    private val updateCheckListUsecase: UpdateCheckList,
    private val stringProvider: StringProvider,
    private val getChecklistItem: GetChecklistItem,
    private val removeChecklistItem: RemoveChecklistItem,
    coroutineContextProvider: CoroutineContextProvider
) : AbstractViewModel<ChecklistDetailViewModel.ViewState, ChecklistDetailViewModel.Event>(ViewState()),
    CoroutineContextProvider by coroutineContextProvider {

    init {
        getChecklist()
    }

    private fun getChecklist() {
        launch {
            update { it.copy(loading = true) }
            val checklist = withContext(io) {
                getChecklistById(checklistId)
            }

            update { it.copy(loading = false, checklist = checklist) }
        }
    }

    fun addNewItem() {
        launch {
            val currentCheckList = currentState().checklist
                ?: throw  IllegalStateException("At this point we require a checklist")
            update { it.copy(loading = true) }
            runCatching {
                withContext(io) {
                    getChecklistItem(currentState().newItemName, currentCheckList.items.size)
                }
            }.fold({ item ->
                val newItems = withContext(io) {
                    (currentCheckList.items + listOf(item)).sortedBy { it.completed }
                }

                val updatedChecklist = currentCheckList.copy(items = newItems)
                update {
                    it.copy(
                        loading = false,
                        checklist = updatedChecklist,
                        newItemName = ""
                    )
                }
                _event.tryEmit(Event.ChecklistItemAdded)
            }, { error ->
                if (error !is AddChecklistException ||
                    error !is UpdateChecklistException
                ) {
                    throw error
                }

                update { it.copy(loading = false) }
                _event.tryEmit(
                    Event.ErrorMessage(
                        when (error) {
                            is AddChecklistException.CheckListItemError -> stringProvider.get(R.string.add_checklist_item_error)
                            else -> stringProvider.get(R.string.error_updating_checklist_message)
                        }
                    )
                )
            })
        }
    }

    fun reorderCheckList(fromPos: Int, toPos: Int) {
        launch {
            withContext(io) {
                val currentCheckList = currentState().checklist
                    ?: throw  IllegalStateException("At this point we require a checklist")

                val items = currentCheckList.items.toMutableList()
                val itemToMove = items[fromPos]
                items.removeAt(fromPos)
                items.add(toPos, itemToMove)
                val updatedItems = items.mapIndexed { index, checklistItem ->
                    checklistItem.copy(position = index)
                }
                updatedItems.sortedBy { it.position }
                update { it.copy(checklist = currentCheckList.copy(items = updatedItems)) }
            }


        }
    }

    fun updateItemStatus(checklistItem: ChecklistItem) {
        launch {
            val currentCheckList = currentState().checklist
                ?: throw  IllegalStateException("At this point we require a checklist")

            val updatedChecklist = withContext(io) {
                val currentItems = currentCheckList.items.toMutableList()
                val itemToUpdateIndex = currentItems.indexOfFirst { it.id == checklistItem.id }
                currentItems.removeAt(itemToUpdateIndex)
                currentItems.add(
                    itemToUpdateIndex,
                    checklistItem.copy(completed = !checklistItem.completed)
                )
                currentItems.sortBy { it.completed }
                currentCheckList.copy(items = currentItems)
            }
            update {
                it.copy(
                    checklist = updatedChecklist,
                )
            }
            _event.tryEmit(Event.ChecklistItemAdded)
        }
    }

    fun removeItem(checklistItem: ChecklistItem) {
        launch {
            val currentCheckList = currentState().checklist
                ?: throw  IllegalStateException("At this point we require a checklist")

            update { it.copy(loading = true) }

            runCatching {
                withContext(io) {
                    removeChecklistItem(
                        checklist = currentCheckList,
                        checklistItemId = checklistItem.id
                    )
                }
            }.fold({ updatedChecklist ->
                update {
                    it.copy(
                        loading = false,
                        checklist = updatedChecklist,
                    )
                }
            }, { error ->
                onUpdateError(error)
            })
        }
    }

    private fun onUpdateError(
        error: Throwable
    ) {
        if (error !is UpdateChecklistException) {
            throw error
        }

        _event.tryEmit(
            Event.ErrorMessage(stringProvider.get(R.string.error_updating_checklist_message))
        )
    }

    fun save() {
        launch {
            update { it.copy(loading = true) }
            runCatching {
                withContext(io) {
                    updateCheckListUsecase(currentState().checklist!!)
                }
            }.fold({
                _event.tryEmit(Event.ChecklistUpdated)
            }, { error ->
                onUpdateError(error)
                update { it.copy(loading = false) }
            })

        }
    }

    fun onNewItemNameChange(name: String) {
        updateSync { it.copy(newItemName = name) }
    }


    @AssistedFactory
    interface Factory {

        fun create(checklistId: String): ChecklistDetailViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideFactory(
            assistedFactory: Factory,
            checklistId: String
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(checklistId) as T
            }

        }
    }

    data class ViewState(
        val loading: Boolean = false,
        val checklist: Checklist? = null,
        val newItemName: String = ""
    )

    sealed interface Event {
        object ChecklistUpdated : Event
        object ChecklistItemAdded : Event
        data class ErrorMessage(val message: String) : Event
    }
}