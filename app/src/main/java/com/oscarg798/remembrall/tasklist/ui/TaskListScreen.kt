package com.oscarg798.remembrall.tasklist.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.oscarg798.remembrall.R
import com.oscarg798.remembrall.common.navigation.Router
import com.oscarg798.remembrall.common.ui.PageConfigurator
import com.oscarg798.remembrall.common.ui.RemembrallPage
import com.oscarg798.remembrall.tasklist.LoadingProfileButton
import com.oscarg798.remembrall.tasklist.TaskListViewModel
import kotlinx.coroutines.flow.collect

fun NavGraphBuilder.listScreen(
    navController: NavController,
    pageConfigurator: MutableState<PageConfigurator>,
) = composable(route = Router.TaskList.route, deepLinks = getDeepLinks()) { backStackEntry ->

    val viewModel: TaskListViewModel = hiltNavGraphViewModel(backStackEntry = backStackEntry)
    val state by viewModel.state.collectAsState(initial = TaskListViewModel.ViewState())
    val pageConfiguration = getPageConfiguration(
        sessionStatus = state.userSessionStatus,
        onAddClicked = {
            viewModel.onAddClicked()
        },
        onProfileButtonClicked = {
            viewModel.onProfileButtonClicked()
        }
    )

    RemembrallPage(pageConfigurator = pageConfiguration) {
        when {
            state.loading -> TaskListLoading()
            state.screenConfiguration != null &&
                state.screenConfiguration!!.isEmpty() -> EmptyTaskList()
            state.screenConfiguration != null -> TaskList(state.screenConfiguration!!) {
                viewModel.removeTask(it)
            }
        }

    }

    LaunchedEffect(key1 = viewModel) {
        viewModel.getTasks()
        viewModel.getSignedInUser()

        pageConfigurator.value = pageConfiguration

        viewModel.events.collect {
            when (it) {
                is TaskListViewModel.Event.ShowAddTaskForm -> Router.AddTask.navigate(
                    navController
                )
                is TaskListViewModel.Event.OpenProfile -> Router.Profile.navigate(navController)
            }
        }
    }
}

@Composable
private fun getPageConfiguration(
    sessionStatus: TaskListViewModel.ViewState.UserSessionStatus,
    onAddClicked: () -> Unit,
    onProfileButtonClicked: () -> Unit,
) = PageConfigurator.buildPageConfigurator().copy(
    title = stringResource(R.string.task_list_screen_title),
    addButtonEnabled = true,
    toolbarRightButton = {
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
    },
    onAddButtonClicked = {
        onAddClicked()
    }
)

private fun getDeepLinks() = listOf(
    navDeepLink {
        uriPattern = Router.TaskList.uriPattern
    }
)
