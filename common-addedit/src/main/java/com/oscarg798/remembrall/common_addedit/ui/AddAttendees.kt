package com.oscarg798.remembrall.common_addedit.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.oscarg798.remembrall.common_addedit.R
import com.oscarg798.remembrall.ui_common.ui.theming.RemembrallTheme
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.input
import com.vanpra.composematerialdialogs.rememberMaterialDialogState

@Composable
fun AddAttendees(
    attendees: Set<String>?,
    enabled: Boolean,
    onAttendeeAdded: (String) -> Unit,
    onAttendeeRemoved: (String) -> Unit
) {

    val dialogState = rememberMaterialDialogState()

    ShowAttendeesDialog(onAttendeeAdded = onAttendeeAdded, state = dialogState)

    Column(
        modifier = Modifier
            .padding(RemembrallTheme.dimens.Medium)
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.add_attendees_label),
                style = TextStyle(color = MaterialTheme.colorScheme.onBackground),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .weight(.8f)
            )

            Image(
                painter = painterResource(R.drawable.ic_add_circular),
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondary),
                modifier = Modifier
                    .size(AddAttendeesSize)
                    .weight(.2f)
                    .clickable {
                        if (enabled) {
                            dialogState.show()
                        }
                    }
            )
        }

        Column {
            attendees?.map { attendee ->
                Row(
                    modifier = Modifier
                        .padding(vertical = RemembrallTheme.dimens.Small),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(.8f)
                    ) {
                        Text(
                            text = attendee,
                            style = TextStyle(color = MaterialTheme.colorScheme.onSurface),
                            modifier = Modifier
                                .padding(RemembrallTheme.dimens.Medium)
                        )
                    }
                    Image(
                        painter = painterResource(R.drawable.ic_delete),
                        contentDescription = null,
                        modifier = Modifier
                            .weight(.2f)
                            .clickable {
                                if (enabled) {
                                    onAttendeeRemoved(attendee)
                                }
                            },
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primaryContainer)
                    )
                }
            }
        }
    }
}

@Composable
private fun ShowAttendeesDialog(onAttendeeAdded: (String) -> Unit, state: MaterialDialogState) {

    MaterialDialog(
        dialogState = state,
        buttons = {
            negativeButton(stringResource(R.string.negative_button_text))
            positiveButton(stringResource(R.string.positive_button_text))
        }
    ) {
        input(
            label = stringResource(R.string.add_attendees_input_label),
            hint = stringResource(R.string.add_attendees_input_hint)
        ) { attendee ->
            onAttendeeAdded(attendee)
            state.hide()
        }
    }
}

private val AddAttendeesSize = 30.dp
