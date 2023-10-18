package com.oscarg798.remembrall.list.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.oscarg798.remembrall.common.R as CommonR
import com.oscarg798.remembrall.ui.AddButton
import com.oscarg798.remembrall.ui.icons.R as IconsR
import androidx.compose.material3.MaterialTheme
import com.oscarg798.remembrall.ui.dimensions.dimensions
import com.oscarg798.remembrall.ui.dimensions.typo

@Composable
internal fun EmptyTaskList(
    modifier: Modifier,
    onAddButtonClicked: () -> Unit
) {

    Box(modifier) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
        ) {
            Image(
                painter = painterResource(IconsR.drawable.ic_agenda),
                contentDescription = null,
                modifier = Modifier
                    .padding(horizontal = MaterialTheme.dimensions.Medium)
                    .size(EmptyStateIconSize)
            )

            Text(
                text = stringResource(CommonR.string.task_list_empty),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typo.h3
                    .merge(TextStyle(MaterialTheme.colorScheme.onBackground)),
                modifier = Modifier.padding(horizontal = MaterialTheme.dimensions.Medium)
            )
        }

        AddButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(MaterialTheme.dimensions.Medium)
        ) {
            onAddButtonClicked()
        }
    }
}

private val EmptyStateIconSize = 50.dp
