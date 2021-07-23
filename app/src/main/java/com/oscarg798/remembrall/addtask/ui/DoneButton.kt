package com.oscarg798.remembrall.addtask.ui

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.oscarg798.remembrall.R
import com.oscarg798.remembrall.common.ui.getButtonTextStyle

@Composable
internal fun DoneButton(
    loading: Boolean = false,
    onButtonClick: () -> Unit
) {
    DoneButtonDefinition(
        content = {
            if (loading) {
                CircularProgressIndicator(color = MaterialTheme.colors.onSecondary)
            } else {
                Text(
                    stringResource(R.string.done_label),
                    style = getButtonTextStyle()
                )
            }
        },
        onClick = {
            if (!loading) {
                onButtonClick()
            }
        }
    )
}
