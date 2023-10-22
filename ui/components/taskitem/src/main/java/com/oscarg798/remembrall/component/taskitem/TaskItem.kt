package com.oscarg798.remembrall.component.taskitem

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.oscarg798.remembrall.actionbutton.ActionButton
import com.oscarg798.remembrall.ui.dimensions.dimensions
import com.oscarg798.remembrall.ui.icons.R
import com.oscarg798.remembrall.ui.theming.RemembrallTheme
import com.oscarg798.remembrall.uicolor.SecondaryTextColor

@Composable
fun TaskItem(
    title: String,
    owned: Boolean,
    modifier: Modifier = Modifier,
    description: String? = null,
    priority: String? = null,
    dueDate: String? = null,
    onOptionsClicked: () -> Unit,
    onTaskClicked: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.surface,
        ),
        shape = RoundedCornerShape(MaterialTheme.dimensions.Medium),
        modifier = modifier.clickable { onTaskClicked() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(MaterialTheme.dimensions.Medium),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            priority?.let {
                TaskPriority(priority, Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(MaterialTheme.dimensions.Small))
            }

            TaskTitle(
                title = title,
                owned = owned,
                modifier = Modifier.fillMaxWidth(),
                onOptionsClicked = onOptionsClicked
            )

            description?.let {
                TaskDescription(description = it, modifier = Modifier.fillMaxWidth())
            }

            dueDate?.let {
                Row(Modifier.fillMaxWidth()) {
                    IconedText(
                        text = it,
                        iconRes = R.drawable.ic_calendar,
                        modifier = Modifier.wrapContentSize()
                    )
                }
            }
        }
    }
}

@Composable
private fun TaskPriority(priority: String, modifier: Modifier = Modifier) {
    Column(modifier) {
        Text(
            text = priority,
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold,
            )
        )

        Divider(
            color = SecondaryTextColor,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = MaterialTheme.dimensions.ExtraSmall)
                .height(HorizontalDividerHeight)
        )
    }

}

@Composable
private fun TaskTitle(
    title: String,
    owned: Boolean,
    modifier: Modifier,
    onOptionsClicked: () -> Unit
) {
    Row(
        modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium
                .copy(color = MaterialTheme.colorScheme.onSurface),
            maxLines = SingleLine,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(if (owned) .9f else 1f)
                .wrapContentHeight()
        )

        if (owned) {
            ActionButton(
                icon = R.drawable.ic_more,
                modifier = Modifier.weight(.1f),
                onClick = onOptionsClicked
            )
        }
    }
}

@Composable
private fun TaskDescription(description: String, modifier: Modifier = Modifier) {
    Text(
        text = description,
        style = MaterialTheme.typography.bodySmall.copy(
            color = MaterialTheme.colorScheme.onSurface
        ),
        maxLines = DescriptionMaxLines,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
    )
}

@Composable
private fun IconedText(
    text: String,
    modifier: Modifier,
    iconRes: Int,
) {
    ConstraintLayout(modifier) {
        val (textField, icon) = createRefs()
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium.copy(
                color = SecondaryTextColor
            ),
            modifier = Modifier.constrainAs(textField) {
                linkTo(parent.top, parent.bottom)
                linkTo(parent.start, icon.start, endMargin = 4.dp, bias = 0f)
                width = Dimension.wrapContent
            }
        )

        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = "priority icon",
            tint = SecondaryTextColor,
            modifier = Modifier
                .constrainAs(icon) {
                    linkTo(textField.top, textField.bottom)
                    linkTo(textField.end, parent.end, bias = 0f)
                }
                .size(12.dp)
        )

    }
}

@Composable
@Preview(device = Devices.NEXUS_5)
private fun TaskItemPreview() {
    RemembrallTheme {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(MaterialTheme.dimensions.Medium),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.Small)
        ) {
            repeat(5) {
                item(it) {
                    TaskItem(
                        title = "We need to buy something",
                        owned = true,
                        priority = if (it == 3) "High" else null,
                        description = "Lets buy it from the store, maybe the groceries",
                        dueDate = "Mon 29 September 2023",
                        onOptionsClicked = { },
                        modifier = Modifier.scale(1f)
                    ) {
                    }
                }
            }
        }
    }
}

private val HorizontalDividerHeight = 1.dp
private const val SingleLine = 1
private const val DescriptionMaxLines = 2