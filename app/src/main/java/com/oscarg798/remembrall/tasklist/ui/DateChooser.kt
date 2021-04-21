package com.oscarg798.remembrall.tasklist.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.oscarg798.remembrall.common.model.Day
import com.oscarg798.remembrall.common.ui.theming.Dimensions

/**
 * unused due design change this might be used later
 */
@Composable
fun DateChooser(days: List<Day>) {
    val listState = rememberLazyListState()

    LazyRow(
        state = listState,
        modifier = Modifier
            .padding(horizontal = Dimensions.Spacing.Medium)
            .fillMaxWidth()
    ) {
        items(days) { item ->
            DayItem(name = item.name, value = item.value, isToday = item.isToday)
        }
    }

    LaunchedEffect(key1 = days) {
        scrollToToday(days, listState)
    }
}

private suspend fun scrollToToday(
    days: List<Day>,
    listState: LazyListState
) {
    val index = days.indexOfFirst { it.isToday }
    listState.scrollToItem(getScrollPosition(index, days.size))
}

private fun getScrollPosition(index: Int, length: Int) = if (index - OffSet >= 0) {
    index - OffSet
} else {
    index + OffSet
}

private const val OffSet = 2
