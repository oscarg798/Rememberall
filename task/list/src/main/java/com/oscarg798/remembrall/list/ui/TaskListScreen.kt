package com.oscarg798.remembrall.list.ui

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import com.oscarg798.remembrall.list.TaskListViewModel
import com.oscarg798.remembrall.ui.navigation.LocalNavControllerProvider
import com.oscarg798.remembrall.ui.navigation.Router

@Composable
fun TaskListScreen(
    backStackEntry: NavBackStackEntry
) {

    val viewModel: TaskListViewModel = hiltViewModel(viewModelStoreOwner = backStackEntry)
    val state by viewModel.state.collectAsState(initial = TaskListViewModel.ViewState())
    val events by viewModel.events.collectAsState(initial = null)
    val navController = LocalNavControllerProvider.current

    when {
        state.tasks.isEmpty() && !state.loading -> EmptyTaskList { viewModel.onAddClicked() }
        else -> TaskList(
            viewModel,
            tasks = state.tasks,
            loading = state.loading,
            initialIndex = state.initialIndex,
            options = state.options,
            onClick = {
                Router.TaskDetail.navigate(
                    navController,
                    Bundle().apply {
                        putString(Router.TaskDetail.TaskIdArgument, it)
                    }
                )
            }, onAddButtonClicked = { viewModel.onAddClicked() }
        ) { task, option ->
            viewModel.onOptionClicked(task, option)
        }
    }

    LaunchedEffect(key1 = events) {
        val event = events ?: return@LaunchedEffect
        when (event) {
            is TaskListViewModel.Event.ShowAddTaskForm -> Router.AddTask.navigate(navController)
            is TaskListViewModel.Event.OpenProfile -> Router.Profile.navigate(navController)
            is TaskListViewModel.Event.NavigateToEdit -> Router.Edit.navigate(
                navController,
                Bundle().apply {
                    putString(Router.Edit.TaskIdArgument, event.taskId)
                }
            )
        }
    }

    LaunchedEffect(key1 = viewModel) {
        viewModel.getTasks()
    }
}
