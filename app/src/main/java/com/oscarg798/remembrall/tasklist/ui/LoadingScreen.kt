package com.oscarg798.remembrall.tasklist.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.oscarg798.remembrall.common.ui.Shimmer
import com.oscarg798.remembrall.common.ui.theming.Dimensions

@Composable
fun TaskListLoading() {
    LazyColumn {
        items(Examples.toList()) { items ->
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
                    Modifier.fillParentMaxWidth()
                        .height(100.dp)
                )
            }
        }
    }
}

private val Examples = 1..3
