package com.oscarg798.remembrall.profile.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import com.oscarg798.remembrall.profile.R
import com.oscarg798.remembrall.common.R as CommonR
import com.oscarg798.remembrall.ui.theming.RemembrallTheme
import com.remembrall.oscarg798.calendar.Calendar


@Composable
internal fun CalendarSelector(
    profileInformation: com.oscarg798.remembrall.profile.model.ProfileInformation,
    onCalendarSelection: (Calendar) -> Unit
) {
    Column(
        Modifier
            .padding(horizontal = RemembrallTheme.dimens.Medium)
    ) {
        Text(
            text = stringResource(CommonR.string.profile_calendar_label),
            modifier = Modifier.padding(
                vertical = RemembrallTheme.dimens.Small
            ),
            style = MaterialTheme.typography.bodyMedium
                .merge(TextStyle(color = MaterialTheme.colorScheme.onBackground))
        )
        Card(
            backgroundColor = MaterialTheme.colorScheme.surface,
            modifier = Modifier.padding(
                vertical = RemembrallTheme.dimens.Small
            ),
            shape = RoundedCornerShape(RemembrallTheme.dimens.Small)
        ) {
            LazyColumn {
                itemsIndexed(profileInformation.calendars.toList()) { index, item ->
                    CalendarItem(
                        calendar = item,
                        isSelected = profileInformation.selectedCalendar == item.id,
                        onCalendarSelection = onCalendarSelection,
                        divider = index != profileInformation.calendars.size - 1
                    )
                }
            }
        }
    }
}
