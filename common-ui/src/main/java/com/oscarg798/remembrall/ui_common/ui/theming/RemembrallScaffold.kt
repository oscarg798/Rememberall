package com.oscarg798.remembrall.ui_common.ui.theming

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
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsWithImePadding

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
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable () -> Unit
) {
    RemembrallTheme {
        ProvideWindowInsets(windowInsetsAnimationsEnabled = true) {
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
}

private val NoElevation = 0.dp
private const val SingleLine = 1
