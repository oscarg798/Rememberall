package com.oscarg798.remembrall.edittask

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
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.oscarg798.remembrall.R
import com.oscarg798.remembrall.common.extensions.requireArguments
import com.oscarg798.remembrall.common.navigation.Router
import com.oscarg798.remembrall.common.ui.AddEditTaskForm
import com.oscarg798.remembrall.common.ui.RemembrallPage
import com.oscarg798.remembrall.common.ui.RemembrallScaffold
import com.oscarg798.remembrall.common.ui.RemembrallTopBar
import com.oscarg798.remembrall.common.ui.RemembrallTopBarTitle
import com.oscarg798.remembrall.common.ui.showSnackBar
import com.oscarg798.remembrall.common.viewmodel.ViewModelStore
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.components.ActivityComponent
import kotlinx.coroutines.flow.collect

fun NavGraphBuilder.editTaskScreen(navController: NavController, viewModelStore: ViewModelStore) =
    composable(route = Router.Edit.route, deepLinks = getDeepLinks()) { backStackEntry ->

        val taskId = backStackEntry.requireArguments()
            .getString(Router.Edit.TaskIdArgument)!!
        EditTask(
            navController = navController,
            viewModelStore = viewModelStore,
            taskId = taskId
        )
    }

@Composable
private fun EditTask(
    navController: NavController,
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

    RemembrallScaffold(
        topBar = {
            RemembrallTopBar(
                title = {
                    RemembrallTopBarTitle("Edit")
                }
            )
        },
        snackbarHostState = snackbarHostState
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
                        editableTask.task.calendarSyncInformation?.attendees
                            ?: editableTask.attendees
                        ).toSet(),
                    loading = state.loading,
                    isUserLoggedIn = state.isUserLoggedIn,
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
