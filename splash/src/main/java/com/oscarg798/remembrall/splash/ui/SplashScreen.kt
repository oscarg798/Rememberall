package com.oscarg798.remembrall.splash.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.oscarg798.remembrall.splash.domain.Effect
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
    val initialModel = remember(viewModel) {
        viewModel.model.value
    }
    val model by viewModel.model.collectAsState(initialModel)
    val uiEffects by viewModel.uiEffect.collectAsState(initial = null)
    val navController = LocalNavControllerProvider.current

    LaunchedEffect(uiEffects) {
        val effect = uiEffects ?: return@LaunchedEffect

        when (effect) {
            is Effect.UIEffect.NavigateToHome -> Router.Home.navigate(navController)
            Effect.UIEffect.NavigateToLogin -> Router.Login.navigate(navController)
        }
    }

    RemembrallScaffold { paddingValues ->
        RemembrallPage(
            modifier = Modifier
                .padding(paddingValues)
                .padding(RemembrallTheme.dimens.Medium)
                .fillMaxSize()
        ) {
            if (model.loading) {
                LoadingAnimation(modifier = Modifier.size(LoadingAnimationSize))
            }
        }
    }
}

private fun getDeepLinks() = listOf(
    navDeepLink {
        uriPattern = Router.Splash.uriPattern
    }
)

private val LoadingAnimationSize = 100.dp
