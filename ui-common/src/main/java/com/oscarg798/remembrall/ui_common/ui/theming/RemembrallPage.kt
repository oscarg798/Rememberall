package com.oscarg798.remembrall.ui_common.theming

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun RemembrallPage(
    backgroundColor: Color = MaterialTheme.colors.background,
    content: @Composable () -> Unit
) {
    Box(
        Modifier
            .fillMaxSize()
            .background(
                color = backgroundColor
            )
    ) {
        content()
    }
}
