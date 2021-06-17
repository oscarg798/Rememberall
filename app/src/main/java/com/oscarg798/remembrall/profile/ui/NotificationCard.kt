package com.oscarg798.remembrall.profile.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import com.oscarg798.remembrall.common.ui.theming.Dimensions

@Composable
internal fun NotificationCard(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.padding(Dimensions.Spacing.Medium)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Daily agenda",
                style = MaterialTheme.typography.body1
                    .merge(TextStyle(color = MaterialTheme.colors.onBackground)),
                modifier = Modifier.weight(0.8f).padding(Dimensions.Spacing.Medium)
            )

            Switch(
                checked, onCheckedChange = onCheckedChange,
                Modifier.weight(0.2f)
            )
        }
    }
}
