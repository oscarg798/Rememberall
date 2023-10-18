package com.oscarg798.remembrall.list.ui

import android.os.Bundle
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import com.oscarg798.remembrall.homeutils.HomeContent
import com.oscarg798.remembrall.list.domain.model.Effect
import com.oscarg798.remembrall.list.domain.model.Event
import com.oscarg798.remembrall.navigation.LocalNavigatorProvider
import com.oscarg798.remembrall.navigation.Route
import com.oscarg798.remembrall.ui.Shimmer
import com.oscarg798.remembrall.ui.dimensions.dimensions

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
    val model by viewModel.model.collectAsStateWithLifecycle()
    val effects by viewModel.uiEffects.collectAsStateWithLifecycle(initialValue = null)
    val navigator = LocalNavigatorProvider.current
    var listIndex by remember(viewModel) { mutableIntStateOf(-1) }

    when {
        model.tasks == null -> LoadingList(modifier = Modifier.fillMaxSize())
        model.tasks!!.isEmpty() -> EmptyTaskList(modifier = Modifier.fillMaxSize()) {
            viewModel.onEvent(Event.OnAddClicked)
        }

        else -> TaskList(
            tasks = model.tasks!!,
            initialIndex = model.initialIndex,
            options = emptyList(),
            modifier = Modifier.fillMaxSize(),
            onClick = {
                navigator.navigate(Route.DETAIL, Bundle().apply {
                    putString(Route.TaskIdArgument, it)
                })
            }, onAddButtonClicked = { viewModel.onEvent(Event.OnAddClicked)}
        ) { task, option ->
            //TODO: option clicked Event.OnTaskOptionClicked(task, option )
        }
    }

    LaunchedEffect(key1 = effects) {
        val effect = effects ?: return@LaunchedEffect

        when(effect){
            Effect.UIEffect.NavigateToAdd -> navigator.navigate(Route.ADD)
            is Effect.UIEffect.NavigateToDetail -> navigator.navigate(Route.DETAIL,
                Bundle().apply {
                    putString(Route.TaskIdArgument, effect.taskId)
                }
            )
            is Effect.UIEffect.ScrollToItem -> listIndex = effect.index
            is Effect.UIEffect.ShowOptions -> {
                //show options here, bottom sheet?
            }
        }
    }
}


@Composable
private fun LoadingList(modifier: Modifier) {
    LazyColumn(modifier = modifier) {
        items(Examples.toList()) {
            Card(
                backgroundColor = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(MaterialTheme.dimensions.Medium),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = MaterialTheme.dimensions.Small,
                        horizontal = MaterialTheme.dimensions.Medium
                    )
            ) {
                Shimmer(
                    Modifier
                        .fillParentMaxWidth()
                        .height(100.dp)
                )
            }
        }
    }

}

private val Examples = 1..3