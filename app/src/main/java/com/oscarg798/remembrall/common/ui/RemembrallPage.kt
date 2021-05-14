package com.oscarg798.remembrall.common.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun RemembrallPage(
    pageConfigurator: PageConfigurator,
    content: @Composable () -> Unit
) {
    Box(
        Modifier
            .fillMaxSize()
            .background(
                color = pageConfigurator.pageBackgroundColor
            )
    ) {
        content()
    }
}
