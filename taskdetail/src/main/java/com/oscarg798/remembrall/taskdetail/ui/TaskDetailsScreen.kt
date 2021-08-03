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
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.oscarg798.remembrall.common.extensions.requireArguments
import com.oscarg798.remembrall.common.viewmodel.ViewModelStore
import com.oscarg798.remembrall.taskdetail.R
import com.oscarg798.remembrall.taskdetail.TaskDetailsViewModel
import com.oscarg798.remembrall.ui_common.navigation.Router
import com.oscarg798.remembrall.ui_common.theming.Dimensions
import com.oscarg798.remembrall.ui_common.theming.RemembrallPage
import com.oscarg798.remembrall.ui_common.theming.RemembrallScaffold
import com.oscarg798.remembrall.ui_common.theming.RemembrallTopBar
import com.oscarg798.remembrall.ui_common.theming.RemembrallTopBarTitle
import com.oscarg798.remembrall.ui_common.ui.RemembrallButton
import com.oscarg798.remembrall.ui_common.ui.TaskBody
import com.oscarg798.remembrall.ui_common.ui.TaskCard
import com.oscarg798.remembrall.ui_common.ui.TaskCardOptions
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
    val viewModel: com.oscarg798.remembrall.taskdetail.TaskDetailsViewModel = viewModelStore.get("${TaskDetailViewModelKey}_$taskId") {
        createViewModel(taskId, context)
    } as com.oscarg798.remembrall.taskdetail.TaskDetailsViewModel

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
            if (it is com.oscarg798.remembrall.taskdetail.TaskDetailsViewModel.Event.NavigateToEdit) {
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

    fun factory(): com.oscarg798.remembrall.taskdetail.TaskDetailsViewModel.TaskDetailsViewModelFactory
}

fun createViewModel(
    taskId: String,
    activity: Activity
): com.oscarg798.remembrall.taskdetail.TaskDetailsViewModel {
    return EntryPointAccessors.fromActivity(
        activity,
        TaskDetailsViewModelEntryPoint::class.java
    ).factory().create(
        taskId = taskId
    )
}

const val TaskDetailViewModelKey = "TaskDetailViewModelKey"
private const val DescriptionMaxLines = 10
