package com.oscarg798.remembrall.common_checklist.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.oscarg798.remembrall.common_checklist.R
import com.oscarg798.remembrall.common_checklist.model.ChecklistItem
import com.oscarg798.remembrall.ui_common.ui.theming.RemembrallTheme

@Composable
fun RemembrallCheckbox(
    modifier: Modifier = Modifier,
    checkListItem: ChecklistItem,
    enabled: Boolean,
    onRemoveClicked: () -> Unit,
    onItemStatusChange: (Boolean) -> Unit,
) {

    Row(modifier = modifier) {
        Checkbox(
            enabled = enabled,
            checked = checkListItem.completed,
            onCheckedChange = onItemStatusChange,
            modifier = Modifier
                .weight(.1f)
                .height(30.dp)
        )

        Text(
            text = checkListItem.name,
            style = MaterialTheme.typography.bodyMedium.merge(
                TextStyle(MaterialTheme.colorScheme.onBackground)
            ),
            modifier = Modifier
                .padding(horizontal = RemembrallTheme.dimens.Small)
                .weight(.8f)
        )

        Icon(
            painter = painterResource(R.drawable.ic_delete),
            contentDescription = null,
            modifier = Modifier
                .weight(.1f)
                .clickable {
                    if (enabled) {
                        onRemoveClicked()
                    }
                },
            tint = MaterialTheme.colorScheme.secondary
        )
    }
    Divider(
        Modifier
            .fillMaxWidth()
            .padding(RemembrallTheme.dimens.ExtraSmall)
    )
}