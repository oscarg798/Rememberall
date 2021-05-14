package com.oscarg798.remembrall.addtask.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.stringResource
import com.oscarg798.remembrall.R
import com.oscarg798.remembrall.common.extensions.SingleLine

@Composable
internal fun TaskNameField(
    name: String,
    enabled: Boolean,
    onNameUpdated: (String) -> Unit
) {
    TextField(
        value = name,
        onValueChange = onNameUpdated,
        enabled = enabled,
        label = {
            Text(text = stringResource(R.string.task_name_label))
        },
        maxLines = SingleLine,
        modifier = Modifier
            .fillMaxWidth()
            .layoutId(TaskNameId),
        colors = addTaskFieldColorConfiguration()
    )
}
