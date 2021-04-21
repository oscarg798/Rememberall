package com.oscarg798.remembrall.common.ui

import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun showSnackBar(
    message: String,
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope,
    onDismiss: () -> Unit = {}
) {

    coroutineScope.launch {
        val state = snackbarHostState.showSnackbar(
            message = message,
            duration = SnackbarDuration.Short
        )

        if (state == SnackbarResult.Dismissed) {
            onDismiss()
        }
    }
}
