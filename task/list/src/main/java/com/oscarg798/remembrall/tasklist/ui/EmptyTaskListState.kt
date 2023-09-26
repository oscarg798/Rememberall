package com.oscarg798.remembrall.tasklist.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.oscarg798.remembrall.tasklist.R
import com.oscarg798.remembrall.ui_common.ui.AddButton
import com.oscarg798.remembrall.ui_common.ui.theming.RemembrallTheme

@Composable
internal fun EmptyTaskList(
    onAddButtonClicked: () -> Unit
) {

    Box() {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
        ) {
            Image(
                painter = painterResource(R.drawable.ic_agenda),
                contentDescription = null,
                modifier = Modifier
                    .padding(horizontal = RemembrallTheme.dimens.Medium)
                    .size(EmptyStateIconSize)
            )

            Text(
                text = stringResource(R.string.task_list_empty),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h3
                    .merge(TextStyle(MaterialTheme.colors.onBackground)),
                modifier = Modifier.padding(horizontal = RemembrallTheme.dimens.Medium)
            )
        }

        AddButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(RemembrallTheme.dimens.Medium)
        ) {
            onAddButtonClicked()
        }
    }
}

private val EmptyStateIconSize = 50.dp
