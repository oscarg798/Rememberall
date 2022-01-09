package com.oscarg798.remembrall.edittask.ui

import android.app.Activity
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.oscarg798.remembrall.common.extensions.requireArguments
import com.oscarg798.remembrall.common.viewmodel.ViewModelStore
import com.oscarg798.remembrall.common_addedit.ui.AddEditTaskForm
import com.oscarg798.remembrall.edittask.EditTaskViewModel
import com.oscarg798.remembrall.edittask.R
import com.oscarg798.remembrall.ui_common.navigation.LocalNavControllerProvider
import com.oscarg798.remembrall.ui_common.navigation.Router
import com.oscarg798.remembrall.ui_common.ui.theming.RemembrallPage
import com.oscarg798.remembrall.ui_common.ui.theming.RemembrallScaffold
import com.oscarg798.remembrall.ui_common.ui.theming.RemembrallTopBar
import com.oscarg798.remembrall.ui_common.ui.theming.RemembrallTopBarTitle
import com.oscarg798.remembrall.ui_common.ui.theming.showSnackBar
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.components.ActivityComponent
import kotlinx.coroutines.flow.collect

fun NavGraphBuilder.editTaskScreen(viewModelStore: ViewModelStore) =
    composable(route = Router.Edit.route, deepLinks = getDeepLinks()) { backStackEntry ->
        val taskId = backStackEntry.requireArguments()
            .getString(Router.Edit.TaskIdArgument)!!
        EditTask(
            viewModelStore = viewModelStore,
            taskId = taskId
        )
    }

@Composable
private fun EditTask(
    viewModelStore: ViewModelStore,
    taskId: String
) {

    val context = LocalContext.current as Activity
    val viewModel: EditTaskViewModel = viewModelStore.get("${EditTaskViewModelKey}_$taskId") {
        createViewModel(taskId, context)
    } as EditTaskViewModel

    val state by viewModel.state.collectAsState(initial = EditTaskViewModel.ViewState())
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val navController = LocalNavControllerProvider.current

    RemembrallScaffold(
        topBar = {
            RemembrallTopBar(
                title = {
                    RemembrallTopBarTitle("Edit")
                },
                backButtonAction = {
                    navController.popBackStack()
                }
            )
        }
    ) {
        RemembrallPage {

            if (state.error != null) {
                showSnackBar(
                    message = state.error!!.message ?: stringResource(id = R.string.generic_error),
                    snackbarHostState = snackbarHostState,
                    coroutineScope = coroutineScope
                )
            }
            if (state.editableTask != null) {
                val editableTask = state.editableTask!!
                AddEditTaskForm(
                    taskName = editableTask.task.name,
                    taskDescription = editableTask.task.description,
                    availablePriorities = editableTask.availablePriorities,
                    selectedPriority = editableTask.task.priority,
                    dueDate = editableTask.displayableTask.dueDate,
                    attendees = (
                            editableTask.task.calendarSyncInformation?.attendees?.map { it.email }
                                ?: editableTask.attendees.map { it.email }
                            ).toSet(),
                    loading = state.loading,
                    onNameUpdated = {
                        viewModel.onNameUpdated(it)
                    },
                    onDescriptionUpdated = {
                        viewModel.onDescriptionUpdated(it)
                    },
                    onDueDateSelected = {
                        viewModel.onDueDateSelected(it)
                    },
                    onAttendeeAdded = {
                        viewModel.onAttendeeAdded(it)
                    },
                    onAttendeeRemoved = {
                        viewModel.onAttendeeRemoved(it)
                    },
                    onPrioritySelected = {
                        viewModel.onPrioritySelected(it)
                    },
                    onDonePressed = {
                        viewModel.onDonePressed()
                    }
                )
            }
        }
    }

    LaunchedEffect(key1 = viewModel) {
        viewModel.events.collect {
            if (it is EditTaskViewModel.Event.TaskEdited) {
                navController.popBackStack()
            }
        }
    }
}

@EntryPoint
@InstallIn(ActivityComponent::class)
internal interface EditTaskViewModelEntryPoint {

    fun editFactory(): EditTaskViewModel.EditTaskViewModelFactory
}

fun createViewModel(
    taskId: String,
    activity: Activity
): EditTaskViewModel {
    return EntryPointAccessors.fromActivity(
        activity,
        EditTaskViewModelEntryPoint::class.java
    ).editFactory().create(
        taskId = taskId
    )
}

private fun getDeepLinks() = listOf(
    navDeepLink {
        uriPattern = Router.Edit.uriPattern
    }
)

private const val DueDatePlaceholder = "Thu, May 14 12:00 mm"
private const val EditTaskViewModelKey = "EditTaskViewModelKey"
