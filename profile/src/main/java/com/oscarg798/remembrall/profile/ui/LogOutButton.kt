package com.oscarg798.remembrall.profile.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.oscarg798.remembrall.ui_common.theming.Dimensions
import com.oscarg798.remembrall.ui_common.theming.getButtonShape
import com.oscarg798.remembrall.ui_common.theming.getButtonTextStyle

@Composable
internal fun LogOutButton(onClick: () -> Unit) {

    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimensions.Spacing.Medium),
        shape = getButtonShape()
    ) {
        Text(text = "Lout Out", style = getButtonTextStyle())
    }
}
