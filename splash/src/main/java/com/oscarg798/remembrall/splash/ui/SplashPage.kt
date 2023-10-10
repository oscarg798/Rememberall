package com.oscarg798.remembrall.splash.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.oscarg798.remembrall.navigation.LocalNavigatorProvider
import com.oscarg798.remembrall.navigation.Page
import com.oscarg798.remembrall.navigation.Route
import com.oscarg798.remembrall.splash.domain.Effect
import com.oscarg798.remembrall.ui.dimensions.dimensions
import com.oscarg798.remembrall.ui.theming.RemembrallPage
import com.oscarg798.remembrall.ui.theming.RemembrallScaffold

internal object SplashPage : Page {

    override fun build(builder: NavGraphBuilder) {
        return builder.splashScreen()
    }
}

private fun NavGraphBuilder.splashScreen() = composable(
    Route.SPLASH.path,
    deepLinks = getDeepLinks()
) { backStackEntry ->

    val viewModel: SplashViewModel = hiltViewModel(backStackEntry)
    val initialModel = remember(viewModel) {
        viewModel.model.value
    }
    val model by viewModel.model.collectAsStateWithLifecycle(initialModel)
    val uiEffects by viewModel.uiEffect.collectAsStateWithLifecycle(null)
    val navigator = LocalNavigatorProvider.current

    LaunchedEffect(uiEffects) {
        val effect = uiEffects ?: return@LaunchedEffect

        navigator.navigate(
            when (effect) {
                is Effect.UIEffect.NavigateToHome -> Route.HOME
                Effect.UIEffect.NavigateToLogin -> Route.LOGIN
            }
        )

    }

    RemembrallScaffold { paddingValues ->
        RemembrallPage(
            modifier = Modifier
                .padding(paddingValues)
                .padding(MaterialTheme.dimensions.Medium)
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
        uriPattern = Route.SPLASH.uriPattern.toString()
    }
)

private val LoadingAnimationSize = 100.dp
