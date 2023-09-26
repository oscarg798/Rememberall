package com.oscarg798.remembrall.tasklist.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.oscarg798.remembrall.common.model.DisplayableTask
import com.oscarg798.remembrall.tasklist.TaskListViewModel
import com.oscarg798.remembrall.tasklist.model.DisplayableTasksGroup
import com.oscarg798.remembrall.tasklist.model.TaskGroup
import com.oscarg798.remembrall.ui.AddButton
import com.oscarg798.remembrall.ui.Shimmer
import com.oscarg798.remembrall.ui.theming.RemembrallTheme

@Composable
internal fun TaskList(
    viewModel: TaskListViewModel,
    tasks: Map<TaskGroup.MonthGroup, DisplayableTasksGroup>,
    loading: Boolean,
    initialIndex: Int = -1,
    options: List<TaskCardOptions.Option>,
    onAddButtonClicked: () -> Unit,
    onClick: (String) -> Unit,
    onOptionClicked: (DisplayableTask, TaskCardOptions.Option) -> Unit
) {
    val listState = rememberLazyListState()

    LaunchedEffect(key1 = viewModel.hashCode() + tasks.hashCode()) {
        if (loading || tasks.isEmpty() || initialIndex == -1) return@LaunchedEffect
        listState.scrollToItem(initialIndex)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(top = RemembrallTheme.dimens.Medium)
                .fillMaxSize()
        ) {
            if (loading) {
                loadingList()
            } else {
                tasks.entries.forEach { entry ->
                    item(key = entry.key.toString()) {
                        Text(
                            text = entry.key.toString(),
                            style = MaterialTheme.typography.displaySmall
                                .merge(TextStyle(color = MaterialTheme.colorScheme.onBackground)),
                            modifier = Modifier
                                .padding(horizontal = RemembrallTheme.dimens.Large)
                                .fillMaxWidth()
                        )
                    }

                    val groups = entry.value
                    items(groups.itemsByDay.keys.toList(), key = { it.toString() }) { dayGroup ->
                        Row(
                            Modifier.padding(
                                vertical = RemembrallTheme.dimens.Small,
                                horizontal = RemembrallTheme.dimens.Medium
                            )
                        ) {
                            DayGroupField(
                                dayGroup = dayGroup,
                                modifier = Modifier.padding(
                                    top = RemembrallTheme.dimens.Medium,
                                    start = RemembrallTheme.dimens.Small
                                )
                            )
                            Column(
                                modifier = Modifier.padding(
                                    start = RemembrallTheme.dimens.Small,
                                )
                            ) {
                                groups.itemsByDay[dayGroup]?.map {
                                    TaskItem(
                                        task = it,
                                        onClick = { taskId ->
                                            onClick(taskId)
                                        },
                                        options = options,
                                        onOptionClicked = onOptionClicked,
                                        modifier = Modifier.padding(
                                            vertical = RemembrallTheme.dimens.Medium
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        AddButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(RemembrallTheme.dimens.Medium)
        ) {
            onAddButtonClicked()
        }
    }
}

@Composable
private fun DayGroupField(dayGroup: TaskGroup.DayGroup, modifier: Modifier = Modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        Text(
            text = dayGroup.dayName,
            style = MaterialTheme.typography.bodyLarge.merge(
                TextStyle(
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
        )
        Text(
            text = dayGroup.dayNumber,
            style = MaterialTheme.typography.bodyLarge.merge(
                TextStyle(
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
        )
    }
}

private fun LazyListScope.loadingList() {
    items(Examples.toList()) {
        Card(
            backgroundColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(RemembrallTheme.dimens.Medium),
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = RemembrallTheme.dimens.Small,
                    horizontal = RemembrallTheme.dimens.Medium
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
