package com.oscarg798.remembrall

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.oscarg798.remembrall.model.Day

@Preview
@Composable
fun DateChooser(
    days: List<Day> = listOf(
        Day("Mon", "01"),
        Day("Tue", "02"),
        Day("Wed", "03"),
        Day("Thur", "04"),
        Day("Fri", "05"),
        Day("Mon", "06"),
        Day("Tue", "07", true),
        Day("Wed", "08"),
        Day("Thur", "09"),
        Day("Fri", "10")
    )
) {
    LazyRow(modifier = Modifier.fillMaxWidth()) {
        items(days) { item ->
            DayItem(name = item.name, value = item.value)
        }
    }
}
