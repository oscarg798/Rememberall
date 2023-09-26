package com.oscarg798.remembrall.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.oscarg798.remembrall.ui.icons.R as IconsR

@Composable
fun AddButton(modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    LargeFloatingActionButton(modifier = modifier, onClick = onClick) {
        Image(
            painter = painterResource(id = IconsR.drawable.ic_add),
            contentDescription = "",
            modifier = Modifier.size(50.dp)
        )
    }
}
