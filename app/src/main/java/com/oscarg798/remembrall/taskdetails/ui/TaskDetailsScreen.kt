package com.oscarg798.remembrall.taskdetails.ui

import android.app.Activity
import android.os.Bundle
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.oscarg798.remembrall.R
import com.oscarg798.remembrall.addtask.ui.RemembrallButton
import com.oscarg798.remembrall.common.extensions.requireArguments
import com.oscarg798.remembrall.common.navigation.Router
import com.oscarg798.remembrall.common.ui.TaskBody
import com.oscarg798.remembrall.common.ui.TaskCard
import com.oscarg798.remembrall.common.ui.TaskCardOptions
import com.oscarg798.remembrall.common.viewmodel.ViewModelStore
import com.oscarg798.remembrall.taskdetails.TaskDetailsViewModel
import com.oscarg798.remembrall.ui_common.theming.Dimensions
import com.oscarg798.remembrall.ui_common.theming.RemembrallPage
import com.oscarg798.remembrall.ui_common.theming.RemembrallScaffold
import com.oscarg798.remembrall.ui_common.theming.RemembrallTopBar
import com.oscarg798.remembrall.ui_common.theming.RemembrallTopBarTitle
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.components.ActivityComponent
import kotlinx.coroutines.flow.collect

fun NavGraphBuilder.taskDetailsScreen(
    navController: NavController,
    viewModelStore: ViewModelStore
) =
    composable(route = Router.TaskDetail.route, deepLinks = getDeepLinks()) { backStackEntry ->

        val taskId = backStackEntry.requireArguments()
            .getString(Router.TaskDetail.TaskIdArgument)!!
        TaskDetail(navController = navController, viewModelStore = viewModelStore, taskId = taskId)
    }

@Composable
private fun TaskDetail(
    navController: NavController,
    viewModelStore: ViewModelStore,
    taskId: String
) {
    val context = LocalContext.current as Activity
    val viewModel: TaskDetailsViewModel = viewModelStore.get("${TaskDetailViewModelKey}_$taskId") {
        createViewModel(taskId, context)
    } as TaskDetailsViewModel

    val edited = remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    val state by viewModel.state.collectAsState(TaskDetailsViewModel.ViewState())

    RemembrallScaffold(
        topBar = {
            RemembrallTopBar(
                title = {
                    RemembrallTopBarTitle(stringResource(R.string.task_detail_title))
                }
            )
        },
        snackbarHostState = snackbarHostState
    ) {
        RemembrallPage {

            if (state.loading) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(Dimensions.Spacing.Medium),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (state.task != null) {
                val task = state.task!!

                TaskCard(
                    task = task,
                    modifier = Modifier
                        .padding(Dimensions.Spacing.Medium)
                ) {
                    TaskBody(
                        task = task,
                        taskCardOptions = TaskCardOptions.None,
                        showAttendees = true,
                        descriptionMaxLines = DescriptionMaxLines
                    )

                    RemembrallButton(stringResource(R.string.EditButtonText)) {
                        viewModel.onEditClicked()
                    }
                }
            }
        }
    }

    LaunchedEffect(key1 = viewModel) {
        viewModel.events.collect {
            if (it is TaskDetailsViewModel.Event.NavigateToEdit) {
                Router.Edit.navigate(
                    navController = navController,
                    Bundle().apply {
                        putString(Router.Edit.TaskIdArgument, taskId)
                    }
                )
                edited.value = true
            }
        }
    }
}

private fun getDeepLinks() = listOf(
    navDeepLink {
        uriPattern = Router.TaskDetail.uriPattern
    }
)

@EntryPoint
@InstallIn(ActivityComponent::class)
internal interface TaskDetailsViewModelEntryPoint {

    fun factory(): TaskDetailsViewModel.TaskDetailsViewModelFactory
}

fun createViewModel(
    taskId: String,
    activity: Activity
): TaskDetailsViewModel {
    return EntryPointAccessors.fromActivity(
        activity,
        TaskDetailsViewModelEntryPoint::class.java
    ).factory().create(
        taskId = taskId
    )
}

const val TaskDetailViewModelKey = "TaskDetailViewModelKey"
private const val DescriptionMaxLines = 10
