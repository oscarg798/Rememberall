package com.oscarg798.remembrall.addtask.ui

import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.oscarg798.remembrall.R
import com.oscarg798.remembrall.addtask.AddTaskViewModel
import com.oscarg798.remembrall.common.navigation.Router
import com.oscarg798.remembrall.common.ui.PageConfigurator
import com.oscarg798.remembrall.common.ui.RemembrallPage
import com.oscarg798.remembrall.common.ui.showSnackBar
import kotlinx.coroutines.flow.collect

fun NavGraphBuilder.addTaskScreen(
    navController: NavHostController,
    pageConfigurator: MutableState<PageConfigurator>,
    snackbarHostState: SnackbarHostState,
) =
    composable(Router.AddTask.route, deepLinks = getDeepLinks()) { backStackEntry ->
        val viewModel: AddTaskViewModel = hiltNavGraphViewModel(backStackEntry)
        val state by viewModel.state.collectAsState(AddTaskViewModel.ViewState())
        val pageConfiguration = getPageConfiguration()

        val coroutineScope = rememberCoroutineScope()

        RemembrallPage(pageConfigurator = pageConfiguration) {
            AddTaskForm(state, viewModel)

            if (state.error != null) {
                showSnackBar(state.error!!, snackbarHostState, coroutineScope)
            }
        }

        LaunchedEffect(viewModel) {
            pageConfigurator.value = pageConfiguration
            viewModel.events.collect {
                if (it is AddTaskViewModel.Event.TaskAdded) {
                    navController.popBackStack()
                }
            }
        }
    }

@Composable
private fun getPageConfiguration() = PageConfigurator.buildPageConfigurator().copy(
    title = stringResource(R.string.add_task_screen_title),
    addButtonEnabled = false,
)

private fun getDeepLinks() = listOf(
    navDeepLink {
        uriPattern = Router.AddTask.uriPattern
    }
)
