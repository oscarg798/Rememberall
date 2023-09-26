package com.oscarg798.remembrall.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.oscarg798.remembrall.ui.icons.R as IconsR
import com.oscarg798.remembrall.ui.theming.RemembrallTheme

@Composable
internal fun ProfileButton(
    onProfileClicked: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .clickable { onProfileClicked() }
            .padding(RemembrallTheme.dimens.ExtraSmall)
    ) {
        Image(
            painterResource(IconsR.drawable.ic_user_profile), contentDescription = "Go to profile",
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
            modifier = Modifier.size(30.dp)
        )
    }
}
