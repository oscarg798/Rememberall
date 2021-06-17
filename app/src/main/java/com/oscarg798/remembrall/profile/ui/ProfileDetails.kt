package com.oscarg798.remembrall.profile.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import com.oscarg798.remembrall.common.extensions.SingleLine
import com.oscarg798.remembrall.common.model.Calendar
import com.oscarg798.remembrall.common.ui.theming.Dimensions

@Composable
internal fun ProfileDetails(
    profileInformation: ProfileInformation,
    onCalendarSelection: (Calendar) -> Unit,
    onNotificationActivated: (Boolean) -> Unit,
    onLogOutClick: () -> Unit
) {

    Column {
        Text(
            text = profileInformation.user.name,
            maxLines = SingleLine,
            style = MaterialTheme.typography.h2
                .merge(TextStyle(color = MaterialTheme.colors.onBackground)),
            modifier = Modifier.padding(Dimensions.Spacing.Medium)
        )

        CalendarSelector(
            profileInformation = profileInformation,
            onCalendarSelection = onCalendarSelection
        )

        NotificationCard(profileInformation.notificationsEnabled, onNotificationActivated)

        LogOutButton {
            onLogOutClick()
        }
    }
}
