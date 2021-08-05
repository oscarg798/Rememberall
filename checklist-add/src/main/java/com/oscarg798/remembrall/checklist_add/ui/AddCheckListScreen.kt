package com.oscarg798.remembrall.checklist_add.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.oscarg798.remembrall.checklist_add.AddCheckListViewModel
import com.oscarg798.remembrall.checklist_add.R
import com.oscarg798.remembrall.common.extensions.SingleLine
import com.oscarg798.remembrall.common_checklist.ui.AwesomeIconField
import com.oscarg798.remembrall.common_checklist.ui.ChecklistItems
import com.oscarg798.remembrall.common_checklist.ui.RemembrallAddCheckboxTextField
import com.oscarg798.remembrall.ui_common.navigation.LocalNavControllerProvider
import com.oscarg798.remembrall.ui_common.navigation.Router
import com.oscarg798.remembrall.ui_common.ui.AwesomeIcon
import com.oscarg798.remembrall.ui_common.ui.RemembrallButton
import com.oscarg798.remembrall.ui_common.ui.RemembrallTextFielColorConfiguration
import com.oscarg798.remembrall.ui_common.ui.theming.RemembrallPage
import com.oscarg798.remembrall.ui_common.ui.theming.RemembrallScaffold
import com.oscarg798.remembrall.ui_common.ui.theming.RemembrallTheme
import com.oscarg798.remembrall.ui_common.ui.theming.RemembrallTopBar
import com.oscarg798.remembrall.ui_common.ui.theming.RemembrallTopBarTitle
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.listItems
import org.burnoutcrew.reorderable.ItemPosition

fun NavGraphBuilder.addCheckListScreen() = composable(
    route = Router.AddChecklist.route,
    deepLinks = Router.AddChecklist.getDeepLinks()
) { backStackEntry ->

    val viewModel: AddCheckListViewModel = hiltViewModel(backStackEntry)
    val state by viewModel.state.collectAsState(initial = AddCheckListViewModel.ViewState())
    val events by viewModel.events.collectAsState(initial = null)
    val dialogState = remember { MaterialDialogState() }

    val snackbarHostState = remember { SnackbarHostState() }
    val listState = rememberLazyListState()
    val navController = LocalNavControllerProvider.current

    IconChooserDialog(state.awesomeIcons.toList(), dialogState) {
        viewModel.onIconChosen(it)
    }

    LaunchedEffect(key1 = events) {
        val event = events ?: return@LaunchedEffect

        when (event) {
            is AddCheckListViewModel.Event.ChecklistItemAdded -> {
                listState.animateScrollToItem(state.checklistItems.size)
            }
            is AddCheckListViewModel.Event.ErrorMessage -> snackbarHostState.showSnackbar(
                event.message,
                duration = SnackbarDuration.Long
            )
            is AddCheckListViewModel.Event.ChecklistAdded -> navController.popBackStack()
        }
    }

    RemembrallScaffold(
        topBar = {
            RemembrallTopBar(
                title = {
                    RemembrallTopBarTitle(stringResource(id = R.string.add_checklist_title))
                }
            )
        },
        snackbarHostState = snackbarHostState
    ) {
        RemembrallPage {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(RemembrallTheme.dimens.Medium)
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(.1f)
                        .padding(vertical = RemembrallTheme.dimens.Small)
                ) {
                    TextField(
                        value = state.name,
                        onValueChange = { viewModel.onNameUpdated(it) },
                        enabled = !state.loading,
                        label = {
                            Text(text = stringResource(R.string.checklist_name_label))
                        },
                        modifier = Modifier.weight(if (state.selectedIcon == null) 1f else .8f),
                        maxLines = SingleLine,
                        colors = RemembrallTextFielColorConfiguration()
                    )

                    if (state.selectedIcon != null) {
                        Card(
                            Modifier
                                .weight(.2f)
                                .padding(start = RemembrallTheme.dimens.Small)
                                .background(
                                    color = MaterialTheme.colors.surface,
                                    shape = RoundedCornerShape(RemembrallTheme.dimens.Small.value)
                                )
                        ) {
                            Box(
                                contentAlignment = Alignment.Center
                            ) {
                                AwesomeIconField(
                                    iconCode = state.selectedIcon!!.code,
                                    modifier = Modifier
                                        .padding(RemembrallTheme.dimens.Medium)
                                        .clickable { dialogState.show() }
                                )
                            }
                        }
                    }
                }

                RemembrallAddCheckboxTextField(
                    state.newItemText,
                    {
                        viewModel.onNewItemTextChange(it)
                    },
                    Modifier
                        .fillMaxWidth()
                        .weight(.1f)
                        .padding(vertical = RemembrallTheme.dimens.Small),
                    enabled = !state.loading,
                ) {
                    viewModel.addChecklistItem()
                }

                ChecklistItems(
                    checklistItems = state.checklistItems,
                    loading = state.loading,
                    onItemStatusChange = { checklistItem, state ->
                        viewModel.onCheckListItemStateChanged(checklistItem.id, state)
                    },
                    modifier = Modifier.weight(.7f),
                    onRemoveClicked = {
                        viewModel.onCheckListRemoved(it.id)
                    },
                    onMove = {fromPos, toPos ->
                        viewModel.reorderCheckList(fromPos.index, toPos.index)
                    }
                )

                RemembrallButton(
                    text = "Add",
                    loading = state.loading,
                    modifier = Modifier.weight(.1f)
                ) {
                    viewModel.add()
                }
            }
        }
    }
}

@Composable
private fun IconChooserDialog(
    icons: List<AwesomeIcon>,
    dialogState: MaterialDialogState,
    onClick: (AwesomeIcon) -> Unit
) {
    MaterialDialog(dialogState = dialogState) {
        listItems(list = icons, item = { _, icon ->
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                AwesomeIconField(iconCode = icon.code, modifier = Modifier.padding(16.dp))
                Divider(Modifier.fillMaxWidth())
            }
        }, onClick = { _, icon -> onClick(icon) })
    }
}
