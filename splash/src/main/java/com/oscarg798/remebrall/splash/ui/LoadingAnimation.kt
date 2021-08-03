package com.oscarg798.remebrall.splash.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.oscarg798.remebrall.splash.R

@Composable
fun LoadingAnimation(modifier: Modifier) {
    val composition = rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.splash))
    LottieAnimation(
        composition = composition.value,
        iterations = LottieConstants.IterateForever,
        clipSpec = LottieClipSpec.Progress(0f, 1f),
        modifier = modifier
    )
}
