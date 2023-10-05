package com.oscarg798.remembrall.profile.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import com.oscarg798.remembrall.ui.dimensions.dimensions
import com.oscarg798.remembrall.ui.theming.RemembrallTheme

@Composable
internal fun NotificationCard(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        backgroundColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.padding(MaterialTheme.dimensions.Medium)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Daily agenda",
                style = MaterialTheme.typography.bodyMedium
                    .merge(TextStyle(color = MaterialTheme.colorScheme.onBackground)),
                modifier = Modifier.weight(0.8f).padding(MaterialTheme.dimensions.Medium)
            )

            Switch(
                checked, onCheckedChange = onCheckedChange,
                Modifier.weight(0.2f)
            )
        }
    }
}
