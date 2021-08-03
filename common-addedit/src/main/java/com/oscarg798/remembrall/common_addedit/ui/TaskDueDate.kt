package com.oscarg798.remembrall.common_addedit.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.oscarg798.remembrall.common_addedit.R
import com.oscarg798.remembrall.ui_common.theming.Dimensions
import com.oscarg798.remembrall.ui_common.theming.SecondaryTextColor
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.datetimepicker
import java.time.LocalDateTime

@Composable
internal fun TaskDueDateField(
    formattedDueDate: String,
    isUserLoggedIn: Boolean,
    enabled: Boolean,
    onDatePicked: (LocalDateTime) -> Unit
) {
    val dialog = remember { MaterialDialog(autoDismiss = false) }

    DatePickerDialog(dialog, onDatePicked)

    Column() {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clickable {
                    if (enabled) {
                        dialog.show()
                    }
                }
        ) {

            Text(
                text = stringResource(R.string.due_date_label),
                style = TextStyle(color = MaterialTheme.colors.onBackground),
                modifier = Modifier
                    .padding(Dimensions.Spacing.Medium)
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .weight(DueTimeLabelTextWeight)
            )

            Text(
                text = formattedDueDate,
                style = TextStyle(color = SecondaryTextColor),
                modifier = Modifier
                    .padding(
                        end = Dimensions.Spacing.Medium,
                        top = Dimensions.Spacing.Medium,
                        bottom = Dimensions.Spacing.Medium
                    )
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .weight(DueTimeValueTextWeight)
            )

            Image(
                painter = painterResource(id = R.drawable.ic_time),
                contentDescription = "",
                colorFilter = ColorFilter.tint(MaterialTheme.colors.secondary),
                modifier = Modifier
                    .size(30.dp)
                    .weight(DueTimeIconWeight)
            )
        }

        if (isUserLoggedIn) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.padding(Dimensions.Spacing.Small)
                    .background(
                        color = MaterialTheme.colors.secondary,
                        shape = RoundedCornerShape(Dimensions.CornerRadius.Small)
                    )
            ) {
                Text(
                    text = String.format(
                        stringResource(R.string.due_date_hint),
                        formattedDueDate
                    ),
                    style = MaterialTheme.typography.body2
                        .merge(TextStyle(color = MaterialTheme.colors.onSecondary)),
                    modifier = Modifier
                        .padding(Dimensions.Spacing.Medium)
                        .fillMaxWidth()
                        .wrapContentHeight()
                )
            }
        }
    }
}

@Composable
private fun DatePickerDialog(
    dialog: MaterialDialog,
    onDatePicked: (LocalDateTime) -> Unit
) {
    dialog.build {
        datetimepicker(
            is24HourClock = true,
            onCancel = {
                dialog.hide()
            }
        ) { date ->
            onDatePicked.invoke(date)
            dialog.hide()
        }
    }
}

private const val DueTimeLabelTextWeight = 0.3f
private const val DueTimeValueTextWeight = 0.5f
private const val DueTimeIconWeight = 0.2f
