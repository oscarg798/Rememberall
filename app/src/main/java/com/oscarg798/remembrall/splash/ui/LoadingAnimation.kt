package com.oscarg798.remembrall.splash.ui

// import com.airbnb.lottie.compose.LottieAnimation
// import com.airbnb.lottie.compose.LottieAnimationSpec
// import com.airbnb.lottie.compose.rememberLottieAnimationState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.oscarg798.remembrall.R

@Composable
fun LoadingAnimation(modifier: Modifier, playing: Boolean = true) {
    val composition = rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.splash))
    LottieAnimation(
        composition = composition.value,
        iterations = LottieConstants.IterateForever,
        clipSpec = LottieClipSpec.Progress(0f, 1f),
        modifier = modifier
    )
}
