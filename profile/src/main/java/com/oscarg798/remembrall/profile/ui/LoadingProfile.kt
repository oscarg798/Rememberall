package com.oscarg798.remembrall.profile.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.dp
import com.oscarg798.remembrall.ui_common.ui.Shimmer
import com.oscarg798.remembrall.ui_common.ui.theming.RemembrallTheme

@Composable
internal fun LoadingProfile() {
    Column(
        Modifier
            .padding(horizontal = RemembrallTheme.dimens.Medium)
    ) {
        LoadingItem()
        LoadingItem()

        Card(
            modifier = Modifier.padding(
                vertical = RemembrallTheme.dimens.Small
            ),
            shape = RoundedCornerShape(RemembrallTheme.dimens.Small)
        ) {
            LazyColumn {
                items(listOf(1, 2, 3)) {
                    Shimmer(
                        Modifier
                            .layoutId(UserNameId)
                            .fillMaxWidth()
                            .height(LoadingTextHeight)
                    )
                }
            }
        }

        LoadingItem()
        LoadingItem()
    }
}

@Composable
private fun LoadingItem() = Shimmer(
    cornerRadius = CornerRadius(RemembrallTheme.dimens.Medium.value),
    modifier = Modifier
        .padding(RemembrallTheme.dimens.Medium)
        .fillMaxWidth()
        .height(LoadingTextHeight)
)

private val LoadingTextHeight = 30.dp
