package com.oscarg798.remembrall.list.ui

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import com.oscarg798.remembrall.homeutils.HomeContent
import com.oscarg798.remembrall.list.TaskListViewModel
import com.oscarg798.remembrall.navigation.LocalNavigatorProvider
import com.oscarg798.remembrall.navigation.Route

internal object TaskListHomeContent : HomeContent {

    @Composable
    override fun Content(backStack: NavBackStackEntry) {
        TaskListScreen(backStackEntry = backStack)
    }
}

@Composable
private fun TaskListScreen(
    backStackEntry: NavBackStackEntry
) {

    val viewModel: TaskListViewModel = hiltViewModel(viewModelStoreOwner = backStackEntry)
    val state by viewModel.state.collectAsState(initial = TaskListViewModel.ViewState())
    val events by viewModel.events.collectAsState(initial = null)
    val navigator = LocalNavigatorProvider.current

    when {
        state.tasks.isEmpty() && !state.loading -> EmptyTaskList { viewModel.onAddClicked() }
        else -> TaskList(
            viewModel,
            tasks = state.tasks,
            loading = state.loading,
            initialIndex = state.initialIndex,
            options = state.options,
            onClick = {
                navigator.navigate(Route.DETAIL, Bundle().apply {
                    putString(Route.TaskIdArgument, it)
                })
            }, onAddButtonClicked = { viewModel.onAddClicked() }
        ) { task, option ->
            viewModel.onOptionClicked(task, option)
        }
    }

    LaunchedEffect(key1 = events) {
        val event = events ?: return@LaunchedEffect
        when (event) {
            is TaskListViewModel.Event.ShowAddTaskForm -> navigator.navigate(Route.ADD)
            is TaskListViewModel.Event.OpenProfile -> navigator.navigate(Route.PROFILE)
            is TaskListViewModel.Event.NavigateToEdit -> navigator.navigate(Route.ADD,
                Bundle().apply {
                    putString(Route.TaskIdArgument, event.taskId)
                }
            )
        }
    }

    LaunchedEffect(key1 = viewModel) {
        viewModel.getTasks()
    }
}
