package com.oscarg798.remembrall.addtask.ui

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.oscarg798.remembrall.ui_common.theming.getButtonTextStyle

@Composable
internal fun RemembrallButton(
    text: String,
    loading: Boolean = false,
    onButtonClick: () -> Unit
) {
    RemembrallButtonDefinition(
        content = {
            if (loading) {
                CircularProgressIndicator(color = MaterialTheme.colors.onSecondary)
            } else {
                Text(
                    text,
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
