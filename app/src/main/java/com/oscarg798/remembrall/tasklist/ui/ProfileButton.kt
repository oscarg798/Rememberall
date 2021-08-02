package com.oscarg798.remembrall.tasklist.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.oscarg798.remembrall.R
import com.oscarg798.remembrall.ui_common.theming.Dimensions
import com.oscarg798.remembrall.ui_common.theming.SecondaryTextColor

@Composable
internal fun ProfileButton(
    isUserLoggedIn: Boolean,
    onProfileClicked: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.clickable { onProfileClicked() }
            .padding(Dimensions.Spacing.ExtraSmall)
    ) {
        Image(
            painterResource(R.drawable.ic_user_profile), contentDescription = "",
            colorFilter = ColorFilter.tint(
                if (isUserLoggedIn) {
                    MaterialTheme.colors.onSecondary
                } else {
                    SecondaryTextColor
                }
            ),
            modifier = Modifier.size(30.dp)
        )
    }
}
