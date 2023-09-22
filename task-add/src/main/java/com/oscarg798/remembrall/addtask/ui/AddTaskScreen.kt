package com.oscarg798.remembrall.addtask.ui

import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.oscarg798.remembrall.addtask.AddTaskViewModel
import com.oscarg798.remembrall.addtask.R
import com.oscarg798.remembrall.ui_common.navigation.LocalNavControllerProvider
import com.oscarg798.remembrall.ui_common.navigation.Router
import com.oscarg798.remembrall.ui_common.ui.theming.RemembrallPage
import com.oscarg798.remembrall.ui_common.ui.theming.RemembrallScaffold
import com.oscarg798.remembrall.ui_common.ui.theming.RemembrallTopBar
import com.oscarg798.remembrall.ui_common.ui.theming.RemembrallTopBarTitle
import com.oscarg798.remembrall.ui_common.ui.theming.showSnackBar

fun NavGraphBuilder.addTaskScreen() =
    composable(Router.AddTask.route, deepLinks = getDeepLinks()) { backStackEntry ->
        val viewModel: AddTaskViewModel = hiltViewModel(backStackEntry)
        val state by viewModel.state.collectAsState(AddTaskViewModel.ViewState())
        val snackbarHostState = remember { SnackbarHostState() }
        val coroutineScope = rememberCoroutineScope()
        val navController = LocalNavControllerProvider.current

        RemembrallScaffold(
            topBar = {
                RemembrallTopBar(
                    title = {
                        RemembrallTopBarTitle(stringResource(R.string.add_task_screen_title))
                    }, backButtonAction = {
                        navController.popBackStack()
                    }
                )
            }
        ) {
            RemembrallPage {
                AddTaskForm(state, viewModel)

                if (state.error != null) {
                    showSnackBar(state.error!!, snackbarHostState, coroutineScope)
                }
            }
        }

        LaunchedEffect(viewModel) {
            viewModel.events.collect {
                if (it is AddTaskViewModel.Event.TaskAdded) {
                    navController.popBackStack()
                }
            }
        }
    }

private fun getDeepLinks() = listOf(
    navDeepLink {
        uriPattern = Router.AddTask.uriPattern
    }
)
