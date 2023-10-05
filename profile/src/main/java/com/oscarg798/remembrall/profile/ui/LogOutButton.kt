package com.oscarg798.remembrall.profile.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.oscarg798.remembrall.ui.dimensions.dimensions
import com.oscarg798.remembrall.ui.theming.RemembrallTheme
import com.oscarg798.remembrall.ui.theming.getButtonShape
import com.oscarg798.remembrall.ui.theming.getButtonTextStyle

@Composable
internal fun LogOutButton(onClick: () -> Unit) {

    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(MaterialTheme.dimensions.Medium),
        shape = getButtonShape()
    ) {
        Text(text = "Lout Out", style = getButtonTextStyle())
    }
}
