package com.oscarg798.remembrall.list.ui.profilebutton

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.oscarg798.remembrall.ui.dimensions.dimensions
import com.oscarg798.remembrall.ui.theming.RemembrallTheme

@Composable
internal fun LoadingProfileButton() {
    CircularProgressIndicator(
        modifier = Modifier.padding(MaterialTheme.dimensions.ExtraSmall)
            .size(ProfileButtonSize),
        color = MaterialTheme.colorScheme.onSecondary
    )
}

private val ProfileButtonSize = 30.dp
