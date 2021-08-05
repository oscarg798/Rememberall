package com.oscarg798.remembrall.common_addedit.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.oscarg798.remembrall.common_addedit.R
import com.oscarg798.remembrall.ui_common.ui.RemembrallTextFielColorConfiguration

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
        maxLines = DescriptionMaxLines,
        modifier = Modifier
            .fillMaxWidth(),
        colors = RemembrallTextFielColorConfiguration()
    )
}

private const val DescriptionMaxLines = 6
