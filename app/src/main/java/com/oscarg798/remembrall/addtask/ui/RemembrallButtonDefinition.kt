package com.oscarg798.remembrall.addtask.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import com.oscarg798.remembrall.common.ui.getButtonShape
import com.oscarg798.remembrall.common.ui.theming.Dimensions

@Composable
internal fun RemembrallButtonDefinition(
    content: @Composable () -> Unit,
    onClick: () -> Unit = {}
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimensions.Spacing.Medium)
            .layoutId(DoneButtonId),
        shape = getButtonShape()
    ) { content() }
}
