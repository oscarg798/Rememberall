package com.oscarg798.remembrall.tasklist.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.oscarg798.remembrall.R

@Composable
internal fun AddButton(onClick: () -> Unit) =
    FloatingActionButton(onClick = onClick) {
        Image(
            painter = painterResource(id = R.drawable.ic_baseline_add_24),
            contentDescription = "",
            modifier = Modifier.size(30.dp)
        )
    }
