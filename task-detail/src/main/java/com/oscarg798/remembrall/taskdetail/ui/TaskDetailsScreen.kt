package com.oscarg798.remembrall.taskdetail.ui

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
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.oscarg798.remembrall.common.extensions.requireArguments
import com.oscarg798.remembrall.common.viewmodel.ViewModelStore
import com.oscarg798.remembrall.taskdetail.R
import com.oscarg798.remembrall.taskdetail.TaskDetailsViewModel
import com.oscarg798.remembrall.ui_common.navigation.LocalNavControllerProvider
import com.oscarg798.remembrall.ui_common.navigation.Router
import com.oscarg798.remembrall.ui_common.ui.RemembrallButton
import com.oscarg798.remembrall.ui_common.ui.TaskBody
import com.oscarg798.remembrall.ui_common.ui.TaskCard
import com.oscarg798.remembrall.ui_common.ui.TaskCardOptions
import com.oscarg798.remembrall.ui_common.ui.theming.RemembrallPage
import com.oscarg798.remembrall.ui_common.ui.theming.RemembrallScaffold
import com.oscarg798.remembrall.ui_common.ui.theming.RemembrallTheme
import com.oscarg798.remembrall.ui_common.ui.theming.RemembrallTopBar
import com.oscarg798.remembrall.ui_common.ui.theming.RemembrallTopBarTitle
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.components.ActivityComponent
import kotlinx.coroutines.flow.collect

fun NavGraphBuilder.taskDetailsScreen(
    viewModelStore: ViewModelStore
) =
    composable(
        route = Router.TaskDetail.route,
        deepLinks = Router.TaskDetail.getDeepLinks()
    ) { backStackEntry ->

        val taskId = backStackEntry.requireArguments()
            .getString(Router.TaskDetail.TaskIdArgument)!!
        TaskDetail(viewModelStore = viewModelStore, taskId = taskId)
    }

@Composable
private fun TaskDetail(
    viewModelStore: ViewModelStore,
    taskId: String
) {
    val context = LocalContext.current as Activity
    val viewModel: TaskDetailsViewModel = viewModelStore.get("${TaskDetailViewModelKey}_$taskId") {
        createViewModel(taskId, context)
    } as TaskDetailsViewModel

    val edited = remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val navController = LocalNavControllerProvider.current

    val state by viewModel.state.collectAsState(TaskDetailsViewModel.ViewState())

    RemembrallScaffold(
        topBar = {
            RemembrallTopBar(
                title = {
                    RemembrallTopBarTitle(stringResource(R.string.task_detail_title))
                }
            )
        }
    ) {
        RemembrallPage {

            if (state.loading) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(RemembrallTheme.dimens.Medium),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (state.task != null) {
                val task = state.task!!

                TaskCard(
                    task = task,
                    modifier = Modifier
                        .padding(RemembrallTheme.dimens.Medium)
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

@EntryPoint
@InstallIn(ActivityComponent::class)
internal interface TaskDetailsViewModelEntryPoint {

    fun factory(): TaskDetailsViewModel.TaskDetailsViewModelFactory
}

private fun createViewModel(
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
