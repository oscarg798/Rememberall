package com.oscarg798.remembrall.addtask.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.oscarg798.remembrall.R
import com.oscarg798.remembrall.common.ui.theming.Dimensions
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.buttons
import com.vanpra.composematerialdialogs.input
import com.vanpra.composematerialdialogs.title

@Composable
fun AddAttendees(
    attendees: Set<String>,
    enabled: Boolean,
    onAttendeeAdded: (String) -> Unit,
    onAttendeeRemoved: (String) -> Unit
) {

    val dialog = getAddAttendeesDialog(onAttendeeAdded)

    Column(
        modifier = Modifier
            .padding(Dimensions.Spacing.Medium)
            .layoutId(AttendeesId)
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.add_attendees_label),
                style = TextStyle(color = MaterialTheme.colors.onBackground),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .weight(.8f)
            )

            Image(
                painter = painterResource(R.drawable.ic_add_circular),
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colors.secondary),
                modifier = Modifier.size(AddAttendeesSize)
                    .weight(.2f)
                    .clickable {
                        if (enabled) {
                            dialog.show()
                        }
                    }
            )
        }

        Column {
            attendees.map { attendee ->
                Row(
                    modifier = Modifier
                        .padding(vertical = Dimensions.Spacing.Small),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(.8f)
                    ) {
                        Text(
                            text = attendee,
                            style = TextStyle(color = MaterialTheme.colors.onSurface),
                            modifier = Modifier
                                .padding(Dimensions.Spacing.Medium)
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
                        colorFilter = ColorFilter.tint(MaterialTheme.colors.primaryVariant)
                    )
                }
            }
        }
    }
}

@Composable
private fun getAddAttendeesDialog(onAttendeeAdded: (String) -> Unit): MaterialDialog {
    val dialog = remember { MaterialDialog() }

    dialog.build {

        title(stringResource(R.string.add_attendees_dialog_title))

        input(
            label = stringResource(R.string.add_attendees_input_label),
            hint = stringResource(R.string.add_attendees_input_hint)
        ) { inputString ->
            onAttendeeAdded(inputString)
        }

        buttons {
            negativeButton(stringResource(R.string.negative_button_text))
            positiveButton(stringResource(R.string.positive_button_text))
        }
    }

    return dialog
}

private val AddAttendeesSize = 30.dp
