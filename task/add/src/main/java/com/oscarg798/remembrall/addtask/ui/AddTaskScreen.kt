package com.oscarg798.remembrall.addtask.ui

import android.util.Patterns
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.oscarg798.remembrall.addtask.R
import com.oscarg798.remembrall.addtask.domain.Effect
import com.oscarg798.remembrall.addtask.domain.Event
import com.oscarg798.remembrall.ui.RemembrallButton
import com.oscarg798.remembrall.ui.icons.R as IconsR
import androidx.compose.ui.tooling.preview.Devices
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.oscarg798.remembrall.ui.components.toolbar.RemembrallToolbar
import com.oscarg798.remembrall.ui.dimensions.dimensions
import com.oscarg798.remembrall.ui.dimensions.typo
import com.oscarg798.remembrall.ui.navigation.LocalNavControllerProvider
import com.oscarg798.remembrall.ui.navigation.Router
import com.oscarg798.remembrall.ui.theming.RemembrallPage
import com.oscarg798.remembrall.ui.theming.RemembrallScaffold
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun NavGraphBuilder.addTaskScreen() =
    composable(Router.AddTask.route, deepLinks = getDeepLinks()) { backStackEntry ->

        val viewModel: AddTaskViewModel = hiltViewModel(backStackEntry)
        val initialState = remember { viewModel.model.value }
        val navController = LocalNavControllerProvider.current
        val snackbarHostState = remember { SnackbarHostState() }
        val context = LocalContext.current

        val model by viewModel.model.collectAsStateWithLifecycle(initialValue = initialState)
        val uiEffects by viewModel.uiEffect.collectAsStateWithLifecycle(initialValue = null)
        var selectingTaskPriority by remember { mutableStateOf(false) }
        val dueDateDatePickerState = rememberMaterialDialogState()
        val dueDateTimePickerState = rememberMaterialDialogState()
        var pickersInitialDateTime by remember { mutableStateOf<LocalDateTime?>(null) }
        var selectedDueDateDate by remember { mutableStateOf<LocalDate?>(null) }
        val attendeesBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        var showAttendeesBottomSheet by remember { mutableStateOf(false) }

        LaunchedEffect(key1 = uiEffects) {
            val uiEffect = uiEffects ?: return@LaunchedEffect

            when (uiEffect) {
                Effect.UIEffect.Close -> navController.popBackStack()
                Effect.UIEffect.DismissAttendeesPicker -> {
                    if (attendeesBottomSheetState.currentValue == SheetValue.Expanded) {
                        attendeesBottomSheetState.hide()
                    }
                    showAttendeesBottomSheet = false
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
                    if (attendeesBottomSheetState.currentValue != SheetValue.Expanded
                    ) {
                        attendeesBottomSheetState.expand()
                        showAttendeesBottomSheet = true
                    }

                    if (!showAttendeesBottomSheet) {
                        showAttendeesBottomSheet = true
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
                            Effect.UIEffect.ShowError.Error.InvalidAttendeesFormat ->
                                context.getString(R.string.attendees_error_message)

                            Effect.UIEffect.ShowError.Error.InvalidName ->
                                context.getString(R.string.name_error_message)

                            Effect.UIEffect.ShowError.Error.ErrorAddingTask ->
                                context.getString(R.string.error_adding_task)

                        }
                    )
                }

                is Effect.UIEffect.NavigateToLogin -> Router.Login.navigate(navController)
            }
        }

        RemembrallScaffold(
            topBar = {
                AddTaskToolbar(
                    onEvent = { viewModel.onEvent(it) },
                    modifier = Modifier
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
                    loading = model.loading,
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

            AnimatedVisibility(
                visible = showAttendeesBottomSheet,
                enter = slideInVertically(tween(durationMillis = 1_000))
            ) {
                AttendeesBottomSheet(
                    state = attendeesBottomSheetState,
                    attendees = model.attendees,
                    onAttendeeAdded = {
                        viewModel.onEvent(Event.OnAttendeeAdded(it))
                    },
                    onAttendeeDeleted = {
                        viewModel.onEvent(Event.OnAttendeeRemoved(it))
                    }
                ) {
                    viewModel.onEvent(Event.DismissAttendeePicker)
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
    RemembrallToolbar(
        modifier = modifier,
        backEnabled = true,
        actions = {
            ToolbarButton(icon = IconsR.drawable.ic_close) {
                onEvent(Event.OnCloseClicked)
            }
        }, onBackPressed = {
            onEvent(Event.OnCloseClicked)
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

@Composable
private fun AttendeesBottomSheet(
    state: SheetState,
    modifier: Modifier = Modifier,
    attendees: Set<String>,
    onAttendeeDeleted: (String) -> Unit,
    onAttendeeAdded: (String) -> Unit,
    onDismissRequest: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(key1 = Unit) { focusRequester.requestFocus() }
    ModalBottomSheet(
        modifier = modifier,
        sheetState = state,
        shape = if (state.hasExpandedState) {
            BottomSheetDefaults.ExpandedShape
        } else {
            BottomSheetDefaults.HiddenShape
        },
        onDismissRequest = onDismissRequest,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.dimensions.Medium)
        ) {
            Text(
                text = "Attendees",
                style = MaterialTheme.typo.h6.copy(
                    color = MaterialTheme.colorScheme.onSurface
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                maxLines = SingleLine,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(modifier = Modifier.width(MaterialTheme.dimensions.Small))

            AddAttendeeField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                onClick = {
                    onAttendeeAdded(it)
                }
            )

            Spacer(modifier = Modifier.width(MaterialTheme.dimensions.ExtraSmall))

            if (attendees.isNotEmpty()) {
                Text(
                    text = "Already added:",
                    style = MaterialTheme.typo.body1.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MaterialTheme.dimensions.Small)
                )
            }

            attendees.forEach {
                key(it) {
                    AttendeeItem(
                        attendee = it,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = MaterialTheme.dimensions.Medium),
                        onClick = onAttendeeDeleted
                    )
                }
            }

            Spacer(modifier = Modifier.width(MaterialTheme.dimensions.Small))

            RemembrallButton(text = "Close", Modifier.fillMaxWidth()) {
                onDismissRequest()
            }
        }
    }
}

@Composable
private fun AddAttendeeField(
    modifier: Modifier,
    onClick: (String) -> Unit
) {
    var editableAttendee by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    var showError by remember { mutableStateOf(false) }

    ConstraintLayout(modifier) {
        val (input, button) = createRefs()

        Column(
            Modifier.constrainAs(input) {
                linkTo(parent.top, parent.bottom)
                linkTo(parent.start, button.start)
                width = Dimension.fillToConstraints
            }
        ) {
            TextField(
                value = editableAttendee,
                isError = showError,
                onValueChange = {
                    editableAttendee = it
                },
                colors = TextFieldColors,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        "jhon@doe.com",
                        style = MaterialTheme.typo.body1
                    )
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email
                ),
                singleLine = true
            )

            if (showError) {
                Text(
                    text = "must be a valid email",
                    style = MaterialTheme.typo.caption.copy(
                        color = MaterialTheme.colorScheme.error,
                        fontStyle = FontStyle.Italic
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        IconButton(
            modifier = Modifier.constrainAs(button) {
                linkTo(input.top, input.bottom)
                end.linkTo(parent.end)
            },
            onClick = {
                coroutineScope.launch(Dispatchers.Main.immediate) {
                    if (Patterns.EMAIL_ADDRESS.matcher(editableAttendee).matches()) {
                        showError = false
                        onClick(editableAttendee)
                        editableAttendee = ""
                    } else {
                        showError = true
                    }
                }
            }
        ) {
            Icon(
                painter = painterResource(id = IconsR.drawable.ic_save),
                contentDescription = "Add attendee",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun AttendeeItem(
    attendee: String,
    modifier: Modifier,
    onClick: (String) -> Unit
) {
    ConstraintLayout(modifier) {
        val (text, icon) = createRefs()

        Text(
            text = attendee,
            style = MaterialTheme.typo.body1.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = Modifier.constrainAs(text) {
                linkTo(parent.top, parent.bottom)
                linkTo(parent.start, icon.start)
                width = Dimension.fillToConstraints
            },
            maxLines = SingleLine,
            overflow = TextOverflow.Ellipsis,
        )

        IconButton(
            onClick = { onClick(attendee) },
            modifier = Modifier.constrainAs(icon) {
                linkTo(text.top, text.bottom)
                end.linkTo(parent.end)
            },
        ) {
            Icon(
                painter = painterResource(id = IconsR.drawable.ic_delete),
                contentDescription = "Delete attendee",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(device = Devices.NEXUS_5)
@Composable
private fun AttendeesBottomSheetPreview() {
    com.oscarg798.remembrall.ui.theming.RemembrallTheme {
        AttendeesBottomSheet(state = rememberModalBottomSheetState(), attendees = setOf(
            "oscarg79@gmail.com"
        ), onAttendeeDeleted = {}, onAttendeeAdded = {}) {

        }
    }
}

@Preview
@Composable
private fun AddTaskToolbarPreview() {
    com.oscarg798.remembrall.ui.theming.RemembrallTheme {
        AddTaskToolbar(Modifier.width(200.dp)) {
        }
    }
}

private const val SingleLine = 1
