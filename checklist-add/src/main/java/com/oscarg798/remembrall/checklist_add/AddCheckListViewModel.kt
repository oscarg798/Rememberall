package com.oscarg798.remembrall.checklist_add

import com.oscarg798.remembrall.checklist_add.usecase.AddCheckList
import com.oscarg798.remembrall.checklist_add.usecase.GetSelectedIcon
import com.oscarg798.remembrall.common.coroutines.CoroutineContextProvider
import com.oscarg798.remembrall.common.provider.StringProvider
import com.oscarg798.remembrall.common.viewmodel.AbstractViewModel
import com.oscarg798.remembrall.common.viewmodel.launch
import com.oscarg798.remembrall.common_checklist.AddChecklistException
import com.oscarg798.remembrall.common_checklist.GetChecklistItem
import com.oscarg798.remembrall.common_checklist.model.ChecklistItem
import com.oscarg798.remembrall.ui_common.ui.AwesomeIcon
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.withContext

@HiltViewModel
class AddCheckListViewModel @Inject constructor(
    private val addCheckList: AddCheckList,
    private val getChecklistItem: GetChecklistItem,
    private val stringProvider: StringProvider,
    private val coroutineContextProvider: CoroutineContextProvider,
    private val getSelectedIcon: GetSelectedIcon,
    awesomeIcons: Set<AwesomeIcon>,
) : AbstractViewModel<AddCheckListViewModel.ViewState, AddCheckListViewModel.Event>(
    ViewState(
        awesomeIcons = awesomeIcons
    )
), CoroutineContextProvider by coroutineContextProvider {

    init {
        launch {
            val icon = withContext(io) {
                getSelectedIcon()
            }
            update { it.copy(selectedIcon = icon) }
        }
    }

    fun onNameUpdated(name: String) {
        updateSync { it.copy(name = name) }
    }

    fun add() {
        launch {
            update { it.copy(loading = true) }
            val currentState = currentState()
            withContext(io) {
                addCheckList(currentState.name, currentState.checklistItems, currentState.selectedIcon)
            }

            update { it.copy(loading = false) }
        }
    }

    fun addChecklistItem() {
        launch {
            update { it.copy(loading = true) }
            runCatching {
                withContext(io) {
                    getChecklistItem(currentState().newItemText, currentState().checklistItems.size)
                }
            }.fold({ item ->
                update {
                    val updatedItems = (it.checklistItems + listOf(item)).toMutableList()
                    updatedItems.sortBy { item-> item.position }
                    it.copy(
                        loading = false,
                        checklistItems = updatedItems,
                        newItemText = ""
                    )
                }
                _event.tryEmit(Event.ChecklistItemAdded)
            }, { error ->
                if (error !is Exception || error !is AddChecklistException) {
                    throw error
                }

                update { it.copy(loading = false) }
                _event.tryEmit(
                    Event.ErrorMessage(
                        when (error) {
                            is AddChecklistException.CheckListItemError -> stringProvider.get(R.string.add_checklist_item_error)
                            is AddChecklistException.CheckListCanNotBeEmptyError -> stringProvider.get(
                                R.string.checklist_items_empty_error
                            )
                            else -> stringProvider.get(R.string.add_checklist_name_error)
                        }
                    )
                )
            })
        }
    }

    fun reorderCheckList(fromPos: Int, toPos: Int) {
        launch {
            withContext(io) {
                val currentCheckList = currentState().checklistItems
                val itemToMove = currentCheckList[fromPos]
                currentCheckList.removeAt(fromPos)
                currentCheckList.add(toPos, itemToMove)
                val updatedItems = currentCheckList.mapIndexed { index, checklistItem ->
                    checklistItem.copy(position = index)
                }
                updatedItems.sortedBy { it.position }
                update { it.copy(checklistItems = updatedItems.toMutableList()) }
            }


        }
    }

    fun onNewItemTextChange(text: String) {
        updateSync { it.copy(newItemText = text) }
    }

    fun onCheckListItemStateChanged(id: String, state: Boolean) {
        launch {
            update { it.copy(loading = true) }
            val updatedItems = withContext(io) {
                val currentState = currentState()
                val items = currentState.checklistItems
                val itemToMutate = items.first { it.id == id }
                val itemToMutateIndex = items.indexOf(itemToMutate)
                items.remove(itemToMutate)
                items.add(itemToMutateIndex, itemToMutate.copy(completed = state))
                items.sortBy { it.position }
                items
            }
            update { it.copy(checklistItems = updatedItems, loading = false) }
        }
    }

    fun onCheckListRemoved(id: String) {
        launch {
            update { it.copy(loading = true) }
            val currentState = currentState()
            val updatedItems = withContext(io) {
                val items = currentState.checklistItems.toMutableList()
                items.removeIf { it.id == id }
                items
            }
            update { it.copy(checklistItems = updatedItems, loading = false) }
        }
    }

    fun onIconChosen(awesomeIcon: AwesomeIcon){
        updateSync { it.copy(selectedIcon = awesomeIcon) }
    }

    data class ViewState(
        val loading: Boolean = false,
        val name: String = "",
        val newItemText: String = "",
        val awesomeIcons: Set<AwesomeIcon> = setOf(),
        val selectedIcon: AwesomeIcon? = null,
        val checklistItems: MutableList<ChecklistItem> = mutableListOf()
    )

    sealed interface Event {

        object ChecklistAdded : Event
        object ChecklistItemAdded : Event {
            override fun equals(other: Any?): Boolean = false
        }

        data class ErrorMessage(val message: String) : Event
    }
}