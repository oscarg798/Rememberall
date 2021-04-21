package com.oscarg798.remembrall.common.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.oscarg798.remembrall.SingleLine
import com.oscarg798.remembrall.common.ui.theming.RemembrallTheme
import com.oscarg798.remembrall.tasklist.ui.AddButton

@Composable
fun RemembrallScaffold(
    pageConfigurator: PageConfigurator,
    snackbarHostState: SnackbarHostState,
    content: @Composable () -> Unit
) {

    RemembrallTheme {
        Scaffold(
            scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState),
            topBar = {
                TopAppBar(
                    backgroundColor = pageConfigurator.topBarBackgroundColor,
                    elevation = NoElevation,
                    title = {
                        if (pageConfigurator.title != null) {
                            Text(
                                pageConfigurator.title,
                                maxLines = SingleLine
                            )
                        }
                    },
                    actions = {
                        if (pageConfigurator.toolbarRightButton != null) {
                            pageConfigurator.toolbarRightButton.invoke()
                        }
                    }
                )
            },
            floatingActionButton = {
                if (pageConfigurator.addButtonEnabled) {
                    AddButton {
                        pageConfigurator.onAddButtonClicked()
                    }
                }
            },
            modifier = Modifier.fillMaxHeight()
        ) {
            Column(Modifier.background(pageConfigurator.topBarBackgroundColor)) {
                content()
            }
        }
    }
}

private val NoElevation = 0.dp
