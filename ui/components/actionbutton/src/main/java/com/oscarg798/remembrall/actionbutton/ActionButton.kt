package com.oscarg798.remembrall.actionbutton

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ActionButton(
    @DrawableRes icon: Int,
    modifier: Modifier = Modifier,
    enabled: Boolean= true,
    tintColor: Color = MaterialTheme.colorScheme.onSurface,
    onLongClicked: () -> Unit = {},
    onClick: () -> Unit
) {
    ActionButtonContainer(
        modifier = modifier,
        enabled = enabled,
        onLongClicked = onLongClicked,
        onClick = onClick
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = "Select a date for the note",
            tint = tintColor
        )
    }
}

@Composable
fun ActionButtonWithDropDown(
    @DrawableRes icon: Int,
    modifier: Modifier = Modifier,
    enabled: Boolean,
    dropDownContent: @Composable () -> Unit,
    tintColor: Color = MaterialTheme.colorScheme.onSurface,
    onLongClicked: () -> Unit = {},
    onClick: () -> Unit
) {
    ActionButtonContainer(
        modifier = modifier,
        enabled = enabled,
        onLongClicked = onLongClicked,
        onClick = onClick
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = "Select a date for the note",
            tint = tintColor
        )
        dropDownContent()
    }
}

@Composable
private fun ActionButtonContainer(
    modifier: Modifier,
    enabled: Boolean,
    onLongClicked: () -> Unit,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    val haptic = LocalHapticFeedback.current

    Box(
        modifier = modifier
            .minimumInteractiveComponentSize()
            .combinedClickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = false, radius = 24.dp),
                onClick = onClick,
                enabled = enabled,
                onLongClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onLongClicked()
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Preview
@Composable
private fun ActionButtonPreview() {
    Row {
        ActionButton(
            icon = androidx.core.R.drawable.ic_call_answer,
            enabled = true,
            onClick = {})
    }
}