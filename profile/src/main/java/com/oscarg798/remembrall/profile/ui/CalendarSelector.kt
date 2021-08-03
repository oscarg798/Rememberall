package com.oscarg798.remembrall.profile.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import com.oscarg798.remebrall.common_calendar.domain.model.Calendar
import com.oscarg798.remembrall.profile.R
import com.oscarg798.remembrall.ui_common.theming.Dimensions

@Composable
internal fun CalendarSelector(
    profileInformation: com.oscarg798.remembrall.profile.model.ProfileInformation,
    onCalendarSelection: (Calendar) -> Unit
) {
    Column(
        Modifier
            .padding(horizontal = Dimensions.Spacing.Medium)
    ) {
        Text(
            text = stringResource(R.string.profile_calendar_label),
            modifier = Modifier.padding(
                vertical = Dimensions.Spacing.Small
            ),
            style = MaterialTheme.typography.h3
                .merge(TextStyle(color = MaterialTheme.colors.onBackground))
        )
        Card(
            modifier = Modifier.padding(
                vertical = Dimensions.Spacing.Small
            ),
            shape = RoundedCornerShape(Dimensions.CornerRadius.Small)
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
