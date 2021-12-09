package com.oscarg798.remembrall.checklist.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.unit.dp
import com.oscarg798.remembrall.ui_common.ui.Shimmer
import com.oscarg798.remembrall.ui_common.ui.theming.RemembrallTheme

@Composable
internal fun LoadingScreen() {
    LazyColumn(Modifier.fillMaxSize()) {
        items(Checklists) {
            Card(
                backgroundColor = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(RemembrallTheme.dimens.Medium),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = RemembrallTheme.dimens.Small)
            ) {

                Shimmer(
                    cornerRadius = CornerRadius(RemembrallTheme.dimens.Medium.value),
                    modifier = Modifier
                        .padding(RemembrallTheme.dimens.Large)
                        .size(10.dp, 30.dp)
                )
            }
        }
    }
}

private const val Checklists = 4