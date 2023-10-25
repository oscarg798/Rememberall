package com.oscarg798.remembrall.widgetimpl

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.ImageProvider
import androidx.glance.action.clickable
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider

@Composable
fun TaskItem(
    title: String,
    modifier: GlanceModifier,
    description: String? = null,
    priority: String? = null,
    dueDate: String? = null,
    onTaskClicked: () -> Unit
) {
    Column(
        modifier = modifier
            .background(ImageProvider(R.drawable.bg_card))
            .padding(16.dp)
            .clickable { onTaskClicked() }
    ) {
        priority?.let {
            TaskPriority(priority = priority, modifier = GlanceModifier.fillMaxWidth())
            Spacer(modifier = GlanceModifier.height(8.dp))
        }

        Text(
            text = title,
            style = TextStyle(
                color = GlanceTheme.colors.onSurface,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            ),
            maxLines = SingleLine,
            modifier = GlanceModifier.defaultWeight()
        )

        description?.let {
            Text(
                text = description,
                style = TextStyle(
                    color = GlanceTheme.colors.onSurface,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                ),
                maxLines = DescriptionMaxLines,
                modifier = modifier
            )
        }

        dueDate?.let {
            Text(
                text = it,
                style = TextStyle(
                    color = ColorProvider(Color(0xFFC0BFBF)),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                ),
                maxLines = DescriptionMaxLines,
                modifier = modifier
            )
        }

    }
}

@Composable
private fun TaskPriority(priority: String, modifier: GlanceModifier) {
    Column(modifier) {
        Text(
            text = priority,
            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium),
            modifier = GlanceModifier.fillMaxWidth(),
            maxLines = SingleLine
        )

        Row(
            modifier = GlanceModifier
                .fillMaxWidth()
                .height(HorizontalDividerHeight)
                .padding(top = 4.dp)
        ) {}
    }
}

private val HorizontalDividerHeight = 1.dp
private const val SingleLine = 1
private const val DescriptionMaxLines = 2