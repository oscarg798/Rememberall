package com.oscarg798.remembrall.splash.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.oscarg798.remembrall.common.navigation.Router
import com.oscarg798.remembrall.splash.SplashViewModel
import com.oscarg798.remembrall.ui_common.theming.Dimensions
import com.oscarg798.remembrall.ui_common.theming.RemembrallPage
import com.oscarg798.remembrall.ui_common.theming.RemembrallScaffold
import kotlinx.coroutines.flow.collect

fun NavGraphBuilder.SplashScreen(
    navController: NavController
) = composable(
    Router.Splash.route,
    deepLinks = getDeepLinks()
) { backStackEntry ->

    val viewModel: SplashViewModel = hiltNavGraphViewModel(backStackEntry)
    val snackbarHostState = remember { SnackbarHostState() }

    RemembrallScaffold(snackbarHostState = snackbarHostState) {
        RemembrallPage {
            Box(
                modifier = Modifier.padding(Dimensions.Spacing.Medium).fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LoadingAnimation(modifier = Modifier.size(LoadingAnimationSize))
            }
        }
    }

    LaunchedEffect(viewModel) {
        viewModel.init()
        viewModel.events.collect {
            Router.TaskList.navigate(navController)
        }
    }
}

private fun getDeepLinks() = listOf(
    navDeepLink {
        uriPattern = Router.Splash.uriPattern
    }
)

private val LoadingAnimationSize = 100.dp
