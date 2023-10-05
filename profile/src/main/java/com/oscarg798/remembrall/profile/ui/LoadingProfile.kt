package com.oscarg798.remembrall.profile.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.dp
import com.oscarg798.remembrall.ui.Shimmer
import com.oscarg798.remembrall.ui.dimensions.dimensions
import com.oscarg798.remembrall.ui.theming.RemembrallTheme

@Composable
internal fun LoadingProfile() {
    Column(
        Modifier
            .padding(horizontal =MaterialTheme.dimensions.Medium)
    ) {
        LoadingItem()
        LoadingItem()

        Card(
            backgroundColor = MaterialTheme.colorScheme.surface,
            modifier = Modifier.padding(
                vertical =MaterialTheme.dimensions.Small
            ),
            shape = RoundedCornerShape(MaterialTheme.dimensions.Small)
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
    cornerRadius = CornerRadius(MaterialTheme.dimensions.Medium.value),
    modifier = Modifier
        .padding(MaterialTheme.dimensions.Medium)
        .fillMaxWidth()
        .height(LoadingTextHeight)
)

private val LoadingTextHeight = 30.dp
