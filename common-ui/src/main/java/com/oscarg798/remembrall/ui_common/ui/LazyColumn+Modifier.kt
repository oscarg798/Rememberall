package com.oscarg798.remembrall.ui_common.ui

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.zIndex

fun Modifier.draggedItem(
    offset: Float?,
    orientation: Orientation = Orientation.Vertical,
): Modifier = composed {
    Modifier
        .zIndex(offset?.let { 1f } ?: 0f)
        .rotate(offset?.let { -5.0f } ?: 0f)
        .graphicsLayer {
            with(offset ?: 0f) {
                if (orientation == Orientation.Vertical) {
                    translationY = this
                } else {
                    translationX = this
                }
            }
            shadowElevation = offset?.let { 8f } ?: 0f
        }
}
