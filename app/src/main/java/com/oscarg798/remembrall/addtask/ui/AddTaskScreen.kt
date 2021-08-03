package com.oscarg798.remembrall.addtask.ui

import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.oscarg798.remembrall.R
import com.oscarg798.remembrall.addtask.AddTaskViewModel
import com.oscarg798.remembrall.ui_common.navigation.Router
import com.oscarg798.remembrall.ui_common.theming.RemembrallPage
import com.oscarg798.remembrall.ui_common.theming.RemembrallScaffold
import com.oscarg798.remembrall.ui_common.theming.RemembrallTopBar
import com.oscarg798.remembrall.ui_common.theming.RemembrallTopBarTitle
import com.oscarg798.remembrall.ui_common.theming.showSnackBar
import kotlinx.coroutines.flow.collect

fun NavGraphBuilder.addTaskScreen(
    navController: NavHostController
) = composable(Router.AddTask.route, deepLinks = getDeepLinks()) { backStackEntry ->
    val viewModel: AddTaskViewModel = hiltNavGraphViewModel(backStackEntry)
    val state by viewModel.state.collectAsState(AddTaskViewModel.ViewState())
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    RemembrallScaffold(
        topBar = {
            RemembrallTopBar(
                title = {
                    RemembrallTopBarTitle(stringResource(R.string.add_task_screen_title))
                }
            )
        },
        snackbarHostState = snackbarHostState
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
