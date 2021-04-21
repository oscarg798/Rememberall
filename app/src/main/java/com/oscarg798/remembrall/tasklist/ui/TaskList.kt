package com.oscarg798.remembrall.tasklist.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import com.oscarg798.remembrall.common.ui.theming.Dimensions
import com.oscarg798.remembrall.tasklist.ui.model.DisplayableTask
import com.oscarg798.remembrall.tasklist.ui.model.TaskListScreenConfiguration

@Composable
internal fun TaskList(
    screenConfiguration: TaskListScreenConfiguration,
    onRemove: (DisplayableTask) -> Unit
) {
    LazyColumn(
        Modifier
            .padding(top = Dimensions.Spacing.Medium)
            .fillMaxSize()
    ) {
        screenConfiguration.tasks.forEach {
            item {
                Text(
                    text = it.label,
                    style = MaterialTheme.typography.h3
                        .merge(TextStyle(color = MaterialTheme.colors.onBackground)),
                    modifier = Modifier
                        .padding(horizontal = Dimensions.Spacing.Large)
                        .fillMaxWidth()
                )
            }

            items(it.tasks.toList()) { task ->
                TaskItem(task) {
                    onRemove(task)
                }
            }
        }
    }
}
