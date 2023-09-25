package com.oscarg798.remembrall.addtask.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.oscarg798.remembrall.addtask.R
import com.oscarg798.remembrall.addtask.domain.Effect
import com.oscarg798.remembrall.addtask.domain.Event
import com.oscarg798.remembrall.addtask.domain.ValidationError
import com.oscarg798.remembrall.ui_common.navigation.LocalNavControllerProvider
import com.oscarg798.remembrall.ui_common.navigation.Router
import com.oscarg798.remembrall.ui_common.ui.RemembrallButton
import com.oscarg798.remembrall.ui_common.ui.theming.RemembrallPage
import com.oscarg798.remembrall.ui_common.ui.theming.RemembrallScaffold
import com.oscarg798.remembrall.ui_common.ui.theming.RemembrallTheme
import com.oscarg798.remembrall.ui_common.ui.theming.SecondaryTextColor
import com.oscarg798.remembrall.ui_common.ui.theming.colorScheme
import com.oscarg798.remembrall.ui_common.ui.theming.dimensions
import com.oscarg798.remembrall.ui_common.ui.theming.typo
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.input
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

fun NavGraphBuilder.addTaskScreen() =
    composable(Router.AddTask.route, deepLinks = getDeepLinks()) { backStackEntry ->

        val viewModel: AddTaskViewModel = hiltViewModel(backStackEntry)
        val initialState = remember { viewModel.model.value }
        val navController = LocalNavControllerProvider.current
        val snackbarHostState = remember { SnackbarHostState() }
        val context = LocalContext.current

        val model by viewModel.model.collectAsState(initialState)
        val uiEffects by viewModel.uiEffect.collectAsState(initial = null)
        var selectingTaskPriority by remember { mutableStateOf(false) }
        val dueDateDatePickerState = rememberMaterialDialogState()
        val dueDateTimePickerState = rememberMaterialDialogState()
        var pickersInitialDateTime by remember { mutableStateOf<LocalDateTime?>(null) }
        val attendeeDialogState = rememberMaterialDialogState()
        var selectedDueDateDate by remember { mutableStateOf<LocalDate?>(null) }

        LaunchedEffect(key1 = uiEffects) {
            val uiEffect = uiEffects ?: return@LaunchedEffect

            when (uiEffect) {
                Effect.UIEffect.Close -> navController.popBackStack()
                Effect.UIEffect.DismissAttendeesPicker -> {
                    if (attendeeDialogState.showing) {
                        attendeeDialogState.hide()
                    }
                }

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
                Effect.UIEffect.ShowAttendeesPicker -> {
                    if (!attendeeDialogState.showing) {
                        attendeeDialogState.show()
                    }
                }

                is Effect.UIEffect.ShowDueDateDatePicker -> {
                    pickersInitialDateTime = uiEffect.initialDateTime
                    if (!dueDateDatePickerState.showing) {
                        dueDateDatePickerState.show()
                    }
                }

                is Effect.UIEffect.ShowPriorityPicker -> {
                    selectingTaskPriority = true
                }

                is Effect.UIEffect.ShowError -> {
                    snackbarHostState.showSnackbar(
                        when (uiEffect.error) {
                            ValidationError.AttendeesNotValid ->
                                context.getString(R.string.attendees_error_message)
                            ValidationError.NameWrongLength ->
                                context.getString(R.string.name_error_message)
                        }
                    )
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
            },
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
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
                    hasAttendees = model.attendees.isNotEmpty(),
                    enabled = !model.loading,
                    onEvent = { viewModel.onEvent(it) }
                )

                AttendeeDialog(
                    dialogState = attendeeDialogState,
                    attendees = model.attendees,
                    onAttendeeAdded = {
                        viewModel.onEvent(Event.OnAttendeesChanged(it))
                    }
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
private fun AttendeeDialog(
    dialogState: MaterialDialogState,
    attendees: Set<String>,
    onAttendeeAdded: (String) -> Unit
) {
    var attendee by remember { mutableStateOf("") }
    MaterialDialog(
        dialogState = dialogState,
        backgroundColor = MaterialTheme.colorScheme.surface,
        buttons = {
            positiveButton(
                stringResource(R.string.save_attendee_button_label),
                onClick = {
                    onAttendeeAdded(attendee)
                    attendee = ""
                }, textStyle = MaterialTheme.typo.body1.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
            )
        }) {
        Column(
            Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.ExtraSmall)
        ) {
            Text(
                text = stringResource(R.string.attendee_dialog_title),
                style = MaterialTheme.typo.body1.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(MaterialTheme.dimensions.Medium)
            )

            TextField(
                value = attendee,
                placeholder = {
                    Text(text = stringResource(R.string.attendee_hint))
                },
                onValueChange = { newValue ->
                    attendee = newValue
                },
                colors = TextFieldColors
            )

            if (attendees.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .padding(MaterialTheme.dimensions.Medium)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.attendee_dialog_attendees_subtitle),
                        style = MaterialTheme.typo.body2.copy(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.SemiBold
                        ),
                    )
                    Text(
                        text = attendees.joinToString(stringResource(R.string.attendess_separator)),
                        style = MaterialTheme.typo.caption.copy(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontStyle = FontStyle.Italic
                        ),
                    )
                }

            }

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