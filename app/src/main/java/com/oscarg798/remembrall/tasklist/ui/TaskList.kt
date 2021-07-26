package com.oscarg798.remembrall.tasklist.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.oscarg798.remembrall.common.model.DisplayableTask
import com.oscarg798.remembrall.common.ui.Shimmer
import com.oscarg798.remembrall.common.ui.theming.Dimensions
import com.oscarg798.remembrall.tasklist.ui.model.DisplayableTaskGroup

@Composable
internal fun TaskList(
    tasks: List<DisplayableTaskGroup>,
    loading: Boolean,
    onClick: (String) -> Unit,
    onRemove: (DisplayableTask) -> Unit
) {
    LazyColumn(
        Modifier
            .padding(top = Dimensions.Spacing.Medium)
            .fillMaxSize()
    ) {
        if (loading) {
            loadingList()
        } else {
            tasks.forEach {
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
                    TaskItem(task = task, onClick = onClick) {
                        onRemove(task)
                    }
                }
            }
        }
    }
}

private fun LazyListScope.loadingList() {
    items(Examples.toList()) {
        Card(
            backgroundColor = MaterialTheme.colors.surface,
            shape = RoundedCornerShape(Dimensions.CornerRadius.Medium),
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = Dimensions.Spacing.Small,
                    horizontal = Dimensions.Spacing.Medium
                )
        ) {
            Shimmer(
                Modifier
                    .fillParentMaxWidth()
                    .height(100.dp)
            )
        }
    }
}

private val Examples = 1..3
