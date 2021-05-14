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
internal fun TaskDescriptionField(
    description: String,
    enabled: Boolean,
    onDescriptionIUpdated: (String) -> Unit
) {

    TextField(
        value = description,
        enabled = enabled,
        onValueChange = onDescriptionIUpdated,
        label = {
            Text(text = stringResource(R.string.task_description_label))
        },
        maxLines = SingleLine,
        modifier = Modifier
            .fillMaxWidth()
            .layoutId(TaskDescriptionId),
        colors = addTaskFieldColorConfiguration()
    )
}
