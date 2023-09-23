package com.oscarg798.remembrall.addtask.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton

import androidx.compose.material.TopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.oscarg798.remembrall.addtask.R
import com.oscarg798.remembrall.addtask.domain.Event
import com.oscarg798.remembrall.common.model.TaskPriority
import com.oscarg798.remembrall.ui_common.navigation.LocalNavControllerProvider
import com.oscarg798.remembrall.ui_common.navigation.Router
import com.oscarg798.remembrall.ui_common.ui.theming.RemembrallPage
import com.oscarg798.remembrall.ui_common.ui.theming.RemembrallScaffold
import com.oscarg798.remembrall.ui_common.ui.theming.RemembrallTheme
import com.oscarg798.remembrall.ui_common.ui.theming.dimensions

fun NavGraphBuilder.addTaskScreen() =
    composable(Router.AddTask.route, deepLinks = getDeepLinks()) { backStackEntry ->
        val viewModel: AddTaskViewModel = hiltViewModel(backStackEntry)
        val navController = LocalNavControllerProvider.current
        val title = remember { mutableStateOf("") }
        val description =
            remember { mutableStateOf("") }
        val priorityBoxExpanded = remember { mutableStateOf(false) }
        val selectedPriority = remember { mutableStateOf<TaskPriority?>(null) }
        val availablePriorities = remember {
            derivedStateOf {
                setOf(
                    TaskPriority.High,
                    TaskPriority.Medium,
                    TaskPriority.Low
                ).sortedBy { it == selectedPriority.value }.reversed()
            }
        }

        RemembrallScaffold(
            topBar = {
                AddTaskToolbar(
                    onEvent = {}, modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = MaterialTheme.dimensions.Medium,
                            start = MaterialTheme.dimensions.Medium,
                            end = MaterialTheme.dimensions.Medium
                        )
                )
            }
        ) { paddingValues ->
            RemembrallPage(modifier = Modifier.padding(paddingValues)) {
                AddTaskForm(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(MaterialTheme.dimensions.Medium),
                    title = title.value,
                    description = description.value,
                    availableTaskPriorities =availablePriorities.value,
                    selectingTaskPriority = priorityBoxExpanded.value,
                    selectedPriority = selectedPriority.value,
                    onEvent = {
                        when {
                            it is Event.OnTitleChanged -> title.value = it.title
                            it is Event.OnDescriptionChanged -> description.value = it.description
                            it is Event.OnTagActionClicked && !priorityBoxExpanded.value ->
                                priorityBoxExpanded.value = true

                            it is Event.OnTaskPrioritySelectorDismissed && priorityBoxExpanded.value ->
                                priorityBoxExpanded.value = false

                            it is Event.OnPriorityChanged -> {
                                selectedPriority.value = it.priority
                                priorityBoxExpanded.value = false
                            }
                        }
                    }
                )
            }
        }
    }

private fun getDeepLinks() = listOf(
    navDeepLink {
        uriPattern = Router.AddTask.uriPattern
    }
)

@Composable
private fun AddTaskToolbar(
    modifier: Modifier = Modifier,
    onEvent: (Event) -> Unit
) {
    TopAppBar(
        modifier = modifier,
        backgroundColor = Color.Transparent,
        contentColor = Color.Transparent,
        navigationIcon = {
            ToolbarButton(icon = R.drawable.ic_back) {
                onEvent(Event.OnCloseClicked)
            }
        },
        elevation = 0.dp,
        title = {},
        actions = {
            ToolbarButton(icon = R.drawable.ic_close) {
                onEvent(Event.OnCloseClicked)
            }
        }
    )
}

@Composable
private fun ToolbarButton(
    @DrawableRes icon: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.background(
            color = ToolbarButtonBackgroundColor,
            shape = CircleShape
        )
    ) {
        Icon(
            painterResource(id = icon),
            contentDescription = "Select a date for the note "
        )
    }
}

@Preview
@Composable
private fun AddTaskToolbarPreview() {
    RemembrallTheme {
        AddTaskToolbar(Modifier.width(200.dp)) {

        }
    }
}

private val ToolbarButtonBackgroundColor = Color(0x50FFFFFF)