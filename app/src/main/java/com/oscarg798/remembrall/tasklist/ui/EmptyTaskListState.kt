package com.oscarg798.remembrall.tasklist.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.oscarg798.remembrall.R
import com.oscarg798.remembrall.common.ui.theming.Dimensions

@Composable
internal fun EmptyTaskList() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(R.drawable.ic_agenda),
                contentDescription = null,
                modifier = Modifier
                    .padding(horizontal = Dimensions.Spacing.Medium)
                    .size(EmptyStateIconSize)
            )

            Text(
                text = stringResource(R.string.task_list_empty),
                style = MaterialTheme.typography.h3
                    .merge(TextStyle(MaterialTheme.colors.onBackground)),
                modifier = Modifier.padding(horizontal = Dimensions.Spacing.Medium)
            )
        }
    }
}

private val EmptyStateIconSize = 50.dp
