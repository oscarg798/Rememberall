package com.oscarg798.remembrall.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.oscarg798.remembrall.common.navigation.Router
import com.oscarg798.remembrall.common.ui.PageConfigurator
import com.oscarg798.remembrall.common.ui.theming.Dimensions
import kotlinx.coroutines.flow.collect

fun NavGraphBuilder.SplashScreen(
    navController: NavController,
    pageConfigurator: MutableState<PageConfigurator>,
) = composable(
    Router.Splash.route,
    deepLinks = getDeepLinks()
) { backStackEntry ->
    val viewModel: SplashViewModel = hiltNavGraphViewModel(backStackEntry)

    val pageConfiguration = PageConfigurator(
        topBarBackgroundColor = MaterialTheme.colors.background,
        pageBackgroundColor = MaterialTheme.colors.background,
        addButtonEnabled = false
    )

    Box(
        modifier = Modifier.padding(Dimensions.Spacing.Medium).fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LoadingAnimation(modifier = Modifier.size(LoadingAnimationSize))
    }

    LaunchedEffect(viewModel) {
        viewModel.init()
        pageConfigurator.value = pageConfiguration
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
