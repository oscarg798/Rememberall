package com.oscarg798.remembrall.common.ui

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.oscarg798.remembrall.common.extensions.SingleLine
import com.oscarg798.remembrall.common.ui.theming.RemembrallTheme

@Composable
fun RemembrallTopBarTitle(title: String) {
    Text(
        title,
        maxLines = SingleLine
    )
}

@Composable
fun RemembrallTopBar(
    topBarBackgroundColor: Color = MaterialTheme.colors.secondary,
    title: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    elevation: Dp = NoElevation
) {
    TopAppBar(
        backgroundColor = topBarBackgroundColor,
        elevation = elevation,
        title = title,
        actions = actions
    )
}

@Composable
fun RemembrallScaffold(
    topBar: @Composable () -> Unit = {},
    snackbarHostState: SnackbarHostState,
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable () -> Unit
) {
    RemembrallTheme {
        Scaffold(
            scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState),
            topBar = topBar,
            floatingActionButton = floatingActionButton,
            modifier = Modifier.fillMaxHeight()
        ) {
            content()
        }
    }
}

private val NoElevation = 0.dp
