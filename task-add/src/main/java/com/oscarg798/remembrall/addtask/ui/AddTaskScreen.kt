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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.oscarg798.remembrall.addtask.domain.Effect
import com.oscarg798.remembrall.addtask.domain.Event
import com.oscarg798.remembrall.ui_common.navigation.LocalNavControllerProvider
import com.oscarg798.remembrall.ui_common.navigation.Router
import com.oscarg798.remembrall.ui_common.ui.theming.RemembrallPage
import com.oscarg798.remembrall.ui_common.ui.theming.RemembrallScaffold
import com.oscarg798.remembrall.ui_common.ui.theming.RemembrallTheme
import com.oscarg798.remembrall.ui_common.ui.theming.dimensions
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

fun NavGraphBuilder.addTaskScreen() =
    composable(Router.AddTask.route, deepLinks = getDeepLinks()) { backStackEntry ->


        val viewModel: AddTaskViewModel = hiltViewModel(backStackEntry)
        val initialState = remember { viewModel.model.value }
        val navController = LocalNavControllerProvider.current

        val model by viewModel.model.collectAsState(initialState)
        val uiEffects by viewModel.uiEffect.collectAsState(initial = null)
        var selectingTaskPriority by remember { mutableStateOf(false) }
        val dueDateDatePickerState = rememberMaterialDialogState()
        val dueDateTimePickerState = rememberMaterialDialogState()
        var pickersInitialDateTime by remember { mutableStateOf<LocalDateTime?>(null) }
        var selectedDueDateDate by remember { mutableStateOf<LocalDate?>(null) }

        LaunchedEffect(key1 = uiEffects) {
            val uiEffect = uiEffects ?: return@LaunchedEffect

            when (uiEffect) {
                Effect.UIEffect.Close -> navController.popBackStack()
                Effect.UIEffect.DismissAttendeesPicker -> {}
                Effect.UIEffect.DismissDueDatePicker -> {
                    selectedDueDateDate = null
                    pickersInitialDateTime = null
                    if (dueDateDatePickerState.showing) {
                        dueDateDatePickerState.hide()
                    }

                    if (dueDateTimePickerState.showing) {
                        dueDateTimePickerState.hide()
                    }
                }

                Effect.UIEffect.DismissTaskPriorityPicker -> selectingTaskPriority = false
                Effect.UIEffect.ShowAttendeesPicker -> {}
                is Effect.UIEffect.ShowDueDateDatePicker -> {
                    pickersInitialDateTime = uiEffect.initialDateTime
                    if (!dueDateDatePickerState.showing) {
                        dueDateDatePickerState.show()
                    }
                }

                is Effect.UIEffect.ShowPriorityPicker -> {
                    selectingTaskPriority = true
                }
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
                    title = model.title,
                    description = model.description,
                    availableTaskPriorities = model.availablePriorities,
                    selectingTaskPriority = selectingTaskPriority,
                    selectedPriority = model.priority,
                    dueDate = model.dueDate,
                    onEvent = { viewModel.onEvent(it) }
                )

                pickersInitialDateTime?.let { initialDateTime ->
                    DueDatePickerDialog(
                        dialogState = dueDateDatePickerState,
                        initialDateTime = initialDateTime,
                        onDatePicked = { selectedDate ->
                            if (dueDateDatePickerState.showing) {
                                dueDateDatePickerState.hide()
                            }
                            selectedDueDateDate = selectedDate
                            dueDateTimePickerState.show()
                        }
                    )

                    selectedDueDateDate?.let { selectedDate ->
                        DueDateTimePicker(
                            dialogState = dueDateTimePickerState,
                            initialDateTime = initialDateTime,
                            onTimePicked = {
                                viewModel.onEvent(
                                    Event.OnDueDateDateAndTimeSelected(
                                        date = selectedDate,
                                        time = it
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }
    }

private fun getDeepLinks() = listOf(
    navDeepLink {
        uriPattern = Router.AddTask.uriPattern
    }
)

@Composable
private fun DueDatePickerDialog(
    dialogState: MaterialDialogState,
    initialDateTime: LocalDateTime,
    onDatePicked: (LocalDate) -> Unit,
) {
    MaterialDialog(
        dialogState = dialogState,
        buttons = {
            positiveButton("Ok")
            negativeButton("Cancel")
        }
    ) {
        datepicker(initialDate = initialDateTime.toLocalDate()) {
            onDatePicked(it)
        }
    }
}

@Composable
private fun DueDateTimePicker(
    dialogState: MaterialDialogState,
    initialDateTime: LocalDateTime,
    onTimePicked: (LocalTime) -> Unit,
) {
    MaterialDialog(
        dialogState = dialogState,
        buttons = {
            positiveButton("Ok")
            negativeButton("Cancel")
        }
    ) {
        timepicker(initialTime = initialDateTime.toLocalTime()) {
            onTimePicked(it)
        }
    }
}

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
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = CircleShape
        )
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = "Select a date for the note ",
            tint = MaterialTheme.colorScheme.onSurface
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