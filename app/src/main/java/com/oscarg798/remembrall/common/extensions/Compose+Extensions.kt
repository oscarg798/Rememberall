package com.oscarg798.remembrall

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstrainScope

fun ConstrainScope.toParentTop(margin: Dp = NoMargin): ConstrainScope {
    top.linkTo(parent.top, margin = margin)
    return this
}

fun ConstrainScope.toParentStart(margin: Dp = NoMargin): ConstrainScope {
    start.linkTo(parent.start, margin = margin)
    return this
}

fun ConstrainScope.toParentEnd(): ConstrainScope {
    end.linkTo(parent.end)
    return this
}

fun ConstrainScope.horizontalToParent(
    bias: Float = 0.5f,
    startMargin: Dp = NoMargin,
    endMargin: Dp = NoMargin
): ConstrainScope {
    linkTo(
        start = parent.start,
        end = parent.end,
        bias = bias,
        startMargin = startMargin,
        endMargin = endMargin
    )
    return this
}

fun ConstrainScope.verticalToParent(
    bias: Float = 0.5f,
    topMargin: Dp = NoMargin,
    bottomMargin: Dp = NoMargin
): ConstrainScope {
    linkTo(
        top = parent.top,
        bottom = parent.bottom,
        bias = bias,
        topMargin = topMargin,
        bottomMargin = bottomMargin
    )
    return this
}

val NoMargin = 0.dp
const val AlignToStart = 0f
const val AlignToEnd = 1f
const val SingleLine = 1
