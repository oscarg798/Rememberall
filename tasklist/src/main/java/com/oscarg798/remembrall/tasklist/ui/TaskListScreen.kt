package com.oscarg798.remembrall.tasklist.ui

import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.oscarg798.remembrall.tasklist.R
import com.oscarg798.remembrall.tasklist.ui.profilebutton.LoadingProfileButton
import com.oscarg798.remembrall.tasklist.TaskListViewModel
import com.oscarg798.remembrall.tasklist.ui.profilebutton.ProfileButton
import com.oscarg798.remembrall.ui_common.navigation.Router
import com.oscarg798.remembrall.ui_common.theming.RemembrallPage
import com.oscarg798.remembrall.ui_common.theming.RemembrallScaffold
import com.oscarg798.remembrall.ui_common.theming.RemembrallTopBar
import com.oscarg798.remembrall.ui_common.theming.RemembrallTopBarTitle
import kotlinx.coroutines.flow.collect

fun NavGraphBuilder.listScreen(
    navController: NavController,
    onFinishRequest: () -> Unit
) = composable(route = Router.TaskList.route, deepLinks = getDeepLinks()) { backStackEntry ->

    val viewModel: TaskListViewModel = hiltNavGraphViewModel(backStackEntry = backStackEntry)
    val state by viewModel.state.collectAsState(initial = TaskListViewModel.ViewState())
    val snackbarHostState = remember { SnackbarHostState() }

    RemembrallScaffold(
        topBar = {
            RemembrallTopBar(
                title = {
                    RemembrallTopBarTitle(stringResource(R.string.task_list_screen_title))
                },
                actions = {
                    ToolbarRightAction(state.userSessionStatus) {
                        viewModel.onProfileButtonClicked()
                    }
                }
            )
        },
        snackbarHostState = snackbarHostState,
        floatingActionButton = {
            AddButton {
                viewModel.onAddClicked()
            }
        }
    ) {
        RemembrallPage {
            when {
                state.tasks.isEmpty() && !state.loading -> EmptyTaskList()
                else -> TaskList(
                    tasks = state.tasks,
                    loading = state.loading,
                    onClick = {
                        Router.TaskDetail.navigate(
                            navController,
                            Bundle().apply {
                                putString(Router.TaskDetail.TaskIdArgument, it)
                            }
                        )
                    }
                ) {
                    viewModel.removeTask(it)
                }
            }
        }
    }

    BackHandler {
        onFinishRequest()
    }

    LaunchedEffect(key1 = viewModel) {
        viewModel.getTasks()
        viewModel.getSignedInUser()

        viewModel.events.collect {
            when (it) {
                is TaskListViewModel.Event.ShowAddTaskForm -> Router.AddTask.navigate(navController)
                is TaskListViewModel.Event.OpenProfile -> Router.Profile.navigate(navController)
            }
        }
    }
}

@Composable
fun ToolbarRightAction(
    sessionStatus: TaskListViewModel.ViewState.UserSessionStatus,
    onProfileButtonClicked: () -> Unit
) {
    when (sessionStatus) {
        TaskListViewModel.ViewState.UserSessionStatus.Loading -> LoadingProfileButton()
        TaskListViewModel.ViewState.UserSessionStatus.LoggedOut -> ProfileButton(
            isUserLoggedIn = false,
            onProfileClicked = onProfileButtonClicked
        )
        TaskListViewModel.ViewState.UserSessionStatus.SignedIn -> ProfileButton(
            isUserLoggedIn = true,
            onProfileClicked = onProfileButtonClicked
        )
    }
}

private fun getDeepLinks() = listOf(
    navDeepLink {
        uriPattern = Router.TaskList.uriPattern
    }
)
