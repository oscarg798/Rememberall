package com.oscarg798.remembrall.ui.theming

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.ProvideWindowInsets
import com.oscarg798.remembrall.ui.icons.R as IconsR

@Composable
fun RemembrallTopBarTitle(title: String) {
    Text(
        title,
        style = MaterialTheme.typography.displayMedium.merge(
            TextStyle(color = MaterialTheme.colorScheme.onBackground)
        ),
        maxLines = SingleLine
    )
}

@Composable
fun RemembrallTopBar(
    topBarBackgroundColor: Color = MaterialTheme.colorScheme.background,
    backButtonAction: (() -> Unit)? = null,
    title: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    elevation: Dp = NoElevation
) {
    TopAppBar(
        backgroundColor = topBarBackgroundColor,
        elevation = elevation,
        title = title,
        actions = actions,
        navigationIcon = {
            if (backButtonAction != null) {
                Icon(
                    painter = painterResource(IconsR.drawable.ic_back),
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        backButtonAction()
                    }
                )
            }
        }
    )
}

@Composable
fun RemembrallScaffold(
    topBar: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    RemembrallTheme {
        ProvideWindowInsets(windowInsetsAnimationsEnabled = false) {
            Scaffold(
                topBar = topBar,
                floatingActionButton = floatingActionButton,
                modifier = Modifier.fillMaxHeight(),
                snackbarHost = snackbarHost
            ) {
                content(it)
            }
        }
    }
}

private val NoElevation = 0.dp
private const val SingleLine = 1
