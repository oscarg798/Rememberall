package com.oscarg798.remembrall.tasklist.ui

import android.os.Bundle
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import com.oscarg798.remembrall.tasklist.TaskListViewModel
import com.oscarg798.remembrall.ui_common.navigation.LocalNavControllerProvider
import com.oscarg798.remembrall.ui_common.navigation.Router
import kotlinx.coroutines.flow.collect

@Composable
fun TaskListScreen(
    backStackEntry: NavBackStackEntry,
    snackbarHostState: SnackbarHostState
) {

    val viewModel: TaskListViewModel = hiltViewModel(viewModelStoreOwner = backStackEntry)
    val state by viewModel.state.collectAsState(initial = TaskListViewModel.ViewState())
    val events by viewModel.events.collectAsState(initial = null)
    val navController = LocalNavControllerProvider.current

    when {
        state.tasks.isEmpty() && !state.loading -> EmptyTaskList()
        else -> TaskList(
            viewModel,
            tasks = state.tasks,
            loading = state.loading,
            initialIndex = state.initialIndex,
            onClick = {
                Router.TaskDetail.navigate(
                    navController,
                    Bundle().apply {
                        putString(Router.TaskDetail.TaskIdArgument, it)
                    }
                )
            }, onAddButtonClicked = { viewModel.onAddClicked() }) {
            viewModel.removeTask(it)
        }
    }

    LaunchedEffect(key1 = events){
        val event = events ?: return@LaunchedEffect
        when (event) {
            is TaskListViewModel.Event.ShowAddTaskForm -> Router.AddTask.navigate(navController)
            is TaskListViewModel.Event.OpenProfile -> Router.Profile.navigate(navController)
        }
    }

    LaunchedEffect(key1 = viewModel) {
        viewModel.getTasks()
    }
}