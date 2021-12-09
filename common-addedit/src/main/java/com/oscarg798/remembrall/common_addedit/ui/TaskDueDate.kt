package com.oscarg798.remembrall.common_addedit.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.oscarg798.remembrall.common_addedit.R
import com.oscarg798.remembrall.ui_common.ui.theming.RemembrallTheme
import com.oscarg798.remembrall.ui_common.ui.theming.SecondaryTextColor
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Composable
internal fun TaskDueDateField(
    formattedDueDate: String,
    enabled: Boolean,
    onDatePicked: (LocalDateTime) -> Unit
) {
    var datePicked by remember { mutableStateOf<LocalDate?>(null) }
    var timePicked by remember { mutableStateOf<LocalTime?>(null) }
    val dateDialogState = rememberMaterialDialogState()
    val timeDialogState = rememberMaterialDialogState()

    DatePickerDialog(dateDialogState) {
        datePicked = it
    }

    TimePickerDialog(dialogState = timeDialogState) {
        timePicked = it
    }

    LaunchedEffect(key1 = datePicked) {
        if (datePicked == null) {
            return@LaunchedEffect
        }

        timeDialogState.show()
    }

    LaunchedEffect(key1 = timePicked) {
        if (timePicked == null) {
            return@LaunchedEffect
        }
        val date =
            datePicked ?: throw IllegalStateException("Time should not be picked before date")
        val time =
            timePicked ?: throw  IllegalStateException("Time should not be null at this point")
        onDatePicked(LocalDateTime.of(date, time))

    }

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clickable {
                    if (enabled) {
                        dateDialogState.show()
                    }
                }
        ) {

            Text(
                text = stringResource(R.string.due_date_label),
                style = TextStyle(color = MaterialTheme.colorScheme.onBackground),
                modifier = Modifier
                    .padding(RemembrallTheme.dimens.Medium)
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .weight(DueTimeLabelTextWeight)
            )

            Text(
                text = formattedDueDate,
                style = TextStyle(color = SecondaryTextColor).merge(MaterialTheme.typography.bodyMedium),
                modifier = Modifier
                    .padding(
                        end = RemembrallTheme.dimens.Medium,
                        top = RemembrallTheme.dimens.Medium,
                        bottom = RemembrallTheme.dimens.Medium
                    )
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .weight(DueTimeValueTextWeight)
            )

            Image(
                painter = painterResource(id = R.drawable.ic_time),
                contentDescription = "",
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondary),
                modifier = Modifier
                    .size(30.dp)
                    .weight(DueTimeIconWeight)
            )
        }
    }
}

@Composable
private fun DatePickerDialog(
    dialogState: MaterialDialogState,
    onDatePicked: (LocalDate) -> Unit
) {
    MaterialDialog(dialogState = dialogState,
        buttons = {
            negativeButton(stringResource(R.string.negative_button_text))
            positiveButton(stringResource(R.string.positive_button_text))
        },) {
        datepicker() { date ->
            onDatePicked.invoke(date)
            dialogState.hide()
        }
    }
}

@Composable
private fun TimePickerDialog(
    dialogState: MaterialDialogState,
    onTimePicked: (LocalTime) -> Unit
) {
    MaterialDialog(dialogState = dialogState,
        buttons = {
            negativeButton(stringResource(R.string.negative_button_text))
            positiveButton(stringResource(R.string.positive_button_text))
        }) {

        timepicker { time ->
            onTimePicked.invoke(time)
        }
    }

}

private const val DueTimeLabelTextWeight = 0.3f
private const val DueTimeValueTextWeight = 0.5f
private const val DueTimeIconWeight = 0.2f
