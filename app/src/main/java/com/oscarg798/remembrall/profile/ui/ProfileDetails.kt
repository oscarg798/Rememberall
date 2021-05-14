package com.oscarg798.remembrall.profile.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.TextStyle
import com.oscarg798.remembrall.common.extensions.SingleLine
import com.oscarg798.remembrall.common.model.Calendar

@Composable
internal fun ProfileDetails(
    profileInformation: ProfileInformation,
    onCalendarSelection: (Calendar) -> Unit,
    onNotificationActivated: (Boolean) -> Unit,
    onLogOutClick: () -> Unit
) {

    Text(
        text = profileInformation.user.name,
        maxLines = SingleLine,
        style = MaterialTheme.typography.h2
            .merge(TextStyle(color = MaterialTheme.colors.onBackground)),
        modifier = Modifier.layoutId(UserNameId)
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
