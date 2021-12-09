package com.oscarg798.remembrall.ui_common.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.oscarg798.remembrall.ui_common.R

@Composable
fun AddButton(modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    LargeFloatingActionButton(modifier = modifier, onClick = onClick) {
        Image(
            painter = painterResource(id = R.drawable.ic_baseline_add_24),
            contentDescription = "",
            modifier = Modifier.size(50.dp)
        )
    }
}
