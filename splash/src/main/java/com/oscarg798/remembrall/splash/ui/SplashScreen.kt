package com.oscarg798.remembrall.splash.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.oscarg798.remembrall.splash.SplashViewModel
import com.oscarg798.remembrall.ui_common.navigation.LocalNavControllerProvider
import com.oscarg798.remembrall.ui_common.navigation.Router
import com.oscarg798.remembrall.ui_common.ui.theming.RemembrallPage
import com.oscarg798.remembrall.ui_common.ui.theming.RemembrallScaffold
import com.oscarg798.remembrall.ui_common.ui.theming.RemembrallTheme

fun NavGraphBuilder.splashScreen() = composable(
    Router.Splash.route,
    deepLinks = getDeepLinks()
) { backStackEntry ->

    val viewModel: SplashViewModel = hiltViewModel(backStackEntry)
    val events by viewModel.events.collectAsState(initial = null)
    val navController = LocalNavControllerProvider.current

    RemembrallScaffold {
        RemembrallPage {
            Box(
                modifier = Modifier
                    .padding(RemembrallTheme.dimens.Medium)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LoadingAnimation(modifier = Modifier.size(LoadingAnimationSize))
            }
        }
    }

    LaunchedEffect(viewModel) {
        viewModel.init()
    }

    LaunchedEffect(events) {
        val event = events ?: return@LaunchedEffect

        if (event is SplashViewModel.Event.NavigateToLogin) {
            Router.Login.navigate(navController)
        } else if (event is SplashViewModel.Event.NavigateToHome) {
            Router.Home.navigate(navController)
        }
    }
}

private fun getDeepLinks() = listOf(
    navDeepLink {
        uriPattern = Router.Splash.uriPattern
    }
)

private val LoadingAnimationSize = 100.dp
