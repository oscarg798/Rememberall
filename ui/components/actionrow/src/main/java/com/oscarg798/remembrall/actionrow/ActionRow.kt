package com.oscarg798.remembrall.actionrow

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.oscarg798.remembrall.ui.dimensions.dimensions

@Composable
fun ActionRow(
    modifier: Modifier,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = modifier
            .minimumInteractiveComponentSize()
            .background(color = MaterialTheme.colorScheme.surfaceVariant, shape = CircleShape)
            .padding(MaterialTheme.dimensions.Small),
        horizontalArrangement = Arrangement.spacedBy(
            MaterialTheme.dimensions.Small,
            Alignment.CenterHorizontally
        ),
        content = content
    )
}