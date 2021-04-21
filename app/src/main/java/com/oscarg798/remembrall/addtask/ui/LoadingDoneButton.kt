package com.oscarg798.remembrall.addtask.ui

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
internal fun LoadingDoneButton() {
    DoneButtonDefinition(
        content = {
            CircularProgressIndicator(color = MaterialTheme.colors.onSecondary)
        }
    )
}
