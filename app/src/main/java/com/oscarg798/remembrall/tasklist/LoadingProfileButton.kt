package com.oscarg798.remembrall.tasklist

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.oscarg798.remembrall.ui_common.theming.Dimensions

@Composable
internal fun LoadingProfileButton() {
    CircularProgressIndicator(
        modifier = Modifier.padding(Dimensions.Spacing.ExtraSmall)
            .size(ProfileButtonSize),
        color = MaterialTheme.colors.onSecondary
    )
}

private val ProfileButtonSize = 30.dp
