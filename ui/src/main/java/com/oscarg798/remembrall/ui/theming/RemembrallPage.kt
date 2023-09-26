package com.oscarg798.remembrall.ui.theming

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun RemembrallPage(
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier
            .fillMaxSize()
            .background(
                color = backgroundColor
            )
    ) {
        content()
    }
}
