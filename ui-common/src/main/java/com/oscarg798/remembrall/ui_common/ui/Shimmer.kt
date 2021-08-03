package com.oscarg798.remembrall.ui_common

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode
import androidx.core.graphics.toColorInt

@Composable
fun Shimmer(modifier: Modifier, colors: List<Color> = ShaderColors) {
    val alpha by rememberInfiniteTransition().animateFloat(
        initialValue = Invisible,
        targetValue = Visible,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = ShaderDuration),
            repeatMode = RepeatMode.Restart
        )
    )

    Canvas(modifier) {
        drawRect(
            ShaderBrush(
                LinearGradientShader(
                    from = Offset.Zero,
                    to = Offset(size.width, size.height),
                    colors = colors,
                    colorStops = listOf(Invisible, alpha, Visible),
                    tileMode = TileMode.Mirror
                )
            ),
            size = size
        )
    }
}

private val ShaderColors = listOf(
    Color.Transparent,
    "#40AAAAAA".toColor(),
    Color.Transparent
)

fun String.toColor() = Color(toColorInt())

private const val ShaderDuration = 1000
private const val Invisible = 0f
private const val Visible = 1f
