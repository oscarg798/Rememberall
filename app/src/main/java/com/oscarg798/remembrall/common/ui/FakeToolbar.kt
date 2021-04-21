package com.oscarg798.remembrall.common.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun FakeTopBar(pageConfigurator: PageConfigurator) {
    Box(
        Modifier.fillMaxWidth().height(FakeTopBarHeight)
            .background(pageConfigurator.topBarBackgroundColor)
    )
}

private val FakeTopBarHeight = 50.dp
