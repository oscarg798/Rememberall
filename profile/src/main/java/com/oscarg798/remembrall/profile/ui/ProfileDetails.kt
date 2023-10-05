package com.oscarg798.remembrall.profile.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import com.oscarg798.remembrall.ui.dimensions.dimensions
import com.oscarg798.remembrall.ui.extensions.SingleLine
import com.oscarg798.remembrall.ui.theming.RemembrallTheme
import com.remembrall.oscarg798.calendar.Calendar

@Composable
internal fun ProfileDetails(
    profileInformation: com.oscarg798.remembrall.profile.model.ProfileInformation,
    onCalendarSelection: (Calendar) -> Unit,
    onNotificationActivated: (Boolean) -> Unit,
    onLogOutClick: () -> Unit
) {

    Column {
        Text(
            text = profileInformation.user.name,
            maxLines = SingleLine,
            style = MaterialTheme.typography.displayMedium
                .merge(TextStyle(color = MaterialTheme.colorScheme.onBackground)),
            modifier = Modifier.padding(MaterialTheme.dimensions.Medium)
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
