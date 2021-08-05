package com.oscarg798.remembrall.checklistdetail.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.unit.dp
import com.oscarg798.remembrall.ui_common.ui.Shimmer
import com.oscarg798.remembrall.ui_common.ui.theming.RemembrallTheme

@Composable
internal fun LoadingScreen() {
    LazyColumn {
        items(CheckboxExamples) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Shimmer(
                    cornerRadius = CornerRadius(RemembrallTheme.dimens.Small.value),
                    modifier = Modifier
                        .weight(.1f)
                        .padding(RemembrallTheme.dimens.Small)
                        .height(20.dp)
                )
                Shimmer(
                    cornerRadius = CornerRadius(8f),
                    modifier = Modifier
                        .weight(.7f)
                        .padding(RemembrallTheme.dimens.Small)
                        .height(30.dp)
                )

                Shimmer(
                    cornerRadius = CornerRadius(8f),
                    modifier = Modifier
                        .weight(.1f)
                        .padding(RemembrallTheme.dimens.Small)
                        .height(20.dp)
                )
            }

            Divider(Modifier.fillMaxWidth().padding(RemembrallTheme.dimens.ExtraSmall))
        }
    }
}

private const val CheckboxExamples = 7