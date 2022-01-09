package com.oscarg798.remembrall.checklistdetail.ui

import android.app.Activity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.oscarg798.remembrall.checklistdetail.ChecklistDetailViewModel
import com.oscarg798.remembrall.checklistdetail.R
import com.oscarg798.remembrall.common.extensions.requireArguments
import com.oscarg798.remembrall.common_checklist.ui.ChecklistItems
import com.oscarg798.remembrall.common_checklist.ui.RemembrallAddCheckboxTextField
import com.oscarg798.remembrall.ui_common.navigation.LocalNavControllerProvider
import com.oscarg798.remembrall.ui_common.navigation.Router
import com.oscarg798.remembrall.ui_common.ui.theming.RemembrallPage
import com.oscarg798.remembrall.ui_common.ui.theming.RemembrallScaffold
import com.oscarg798.remembrall.ui_common.ui.theming.RemembrallTheme
import com.oscarg798.remembrall.ui_common.ui.theming.RemembrallTopBar
import com.oscarg798.remembrall.ui_common.ui.theming.RemembrallTopBarTitle
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.components.ActivityComponent
import org.burnoutcrew.reorderable.ItemPosition

fun NavGraphBuilder.checklistDetailScreen() = composable(
    route = Router.ChecklistDetail.route,
    deepLinks = Router.ChecklistDetail.getDeepLinks()
) { backStackEntry ->

    val checklistId = backStackEntry.requireArguments()
        .getString(Router.ChecklistDetail.ChecklistIdArgument)!!

    val viewModel: ChecklistDetailViewModel =
        getViewModel(LocalContext.current as Activity, checklistId)

    val snackbarHostState = remember { SnackbarHostState() }
    val state by viewModel.state.collectAsState(initial = ChecklistDetailViewModel.ViewState())
    val events by viewModel.events.collectAsState(initial = null)
    val navController = LocalNavControllerProvider.current

    LaunchedEffect(key1 = events) {
        val event = events ?: return@LaunchedEffect

        when (event) {
            is ChecklistDetailViewModel.Event.ChecklistUpdated -> navController.popBackStack()
            is ChecklistDetailViewModel.Event.ChecklistItemAdded -> {
            }
            is ChecklistDetailViewModel.Event.ErrorMessage -> snackbarHostState.showSnackbar(
                message = event.message,
                duration = SnackbarDuration.Long
            )
        }
    }

    RemembrallScaffold(topBar = {
        RemembrallTopBar(
            title = {
                val checklist = state.checklist ?: return@RemembrallTopBar
                RemembrallTopBarTitle(checklist.name.capitalize(Locale.current))
            }, actions = {
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .clickable {
                            if (!state.loading) {
                                viewModel.save()
                            }
                        }
                        .padding(RemembrallTheme.dimens.ExtraSmall)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_save),
                        contentDescription = stringResource(id = R.string.save_content_description),
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(30.dp)
                    )
                }
            },
            backButtonAction = {
                navController.popBackStack()
            }
        )
    }) {
        RemembrallPage {
            Column(
                Modifier
                    .padding(RemembrallTheme.dimens.Medium)
            ) {
                if (state.loading) {
                    LoadingScreen()
                } else {


                    RemembrallAddCheckboxTextField(
                        state.newItemName, {
                            viewModel.onNewItemNameChange(it)
                        }, Modifier
                            .padding(vertical = RemembrallTheme.dimens.Small)
                            .weight(.1f)
                            .fillMaxWidth(), enabled = !state.loading
                    ) {
                        viewModel.addNewItem()
                    }


                    if (state.checklist != null) {
                        val checklistItems = state.checklist!!.items.toMutableList()
                        ChecklistItems(
                            checklistItems = checklistItems,
                            loading = state.loading,
                            modifier = Modifier.weight(.9f),
                            onItemStatusChange = {item, _ ->
                                viewModel.updateItemStatus(item)
                            },
                            onMove = { fromPos: ItemPosition, toPos: ItemPosition ->
                                viewModel.reorderCheckList(fromPos.index, toPos.index)
                            },
                            onRemoveClicked = {
                                viewModel.removeItem(it)
                            }
                        )
                    }
                }
            }
        }
    }
}




@Composable
private fun getViewModel(
    context: Activity,
    checklistId: String
): ChecklistDetailViewModel = EntryPointAccessors.fromActivity(
    context,
    ChecklistDetailScreenEntryPoint::class.java
).checklistFactory().create(checklistId)


@EntryPoint
@InstallIn(ActivityComponent::class)
internal interface ChecklistDetailScreenEntryPoint {

    fun checklistFactory(): ChecklistDetailViewModel.Factory
}
