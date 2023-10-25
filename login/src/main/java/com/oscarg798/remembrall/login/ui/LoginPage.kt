package com.oscarg798.remembrall.login.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.oscarg798.remembrall.login.R
import com.oscarg798.remembrall.login.domain.Effect
import com.oscarg798.remembrall.login.domain.Event
import com.oscarg798.remembrall.navigation.LocalNavigatorProvider
import com.oscarg798.remembrall.navigation.Page
import com.oscarg798.remembrall.navigation.Route
import com.oscarg798.remembrall.ui.RemembrallButton
import com.oscarg798.remembrall.ui.components.toolbar.RemembrallToolbar
import com.oscarg798.remembrall.ui.theming.RemembrallPage
import com.oscarg798.remembrall.ui.theming.RemembrallScaffold
import com.oscarg798.remembrall.ui.theming.RemembrallTopBar
import com.oscarg798.remembrall.ui.theming.RemembrallTopBarTitle

internal object LoginPage : Page {

    override fun build(builder: NavGraphBuilder) {
        return builder.loginScreen()
    }
}

private fun NavGraphBuilder.loginScreen() =
    composable(
        route = Route.LOGIN.path,
        deepLinks = listOf(
            navDeepLink {
                uriPattern = Route.LOGIN.uriPattern.toString()
            }
        )
    ) { backStackEntry ->
        val viewModel: LoginViewModel = hiltViewModel(backStackEntry)

        val initialState = remember(viewModel) {
            viewModel.model.value
        }
        val context = LocalContext.current
        val state by viewModel.model.collectAsStateWithLifecycle(initialState)
        val uiEffects by viewModel.uiEffect.collectAsStateWithLifecycle(null)
        val snackbarHostState = remember { SnackbarHostState() }
        val navigator = LocalNavigatorProvider.current

        LaunchedEffect(key1 = uiEffects) {
            val effect = uiEffects ?: return@LaunchedEffect

            when (effect) {
                Effect.UIEffect.NavigateBack -> navigator.navigateBack()
                Effect.UIEffect.NavigateToHome -> navigator.navigate(Route.HOME)
                Effect.UIEffect.ShowErrorMessage.LoginError -> snackbarHostState.showSnackbar(
                    context.getString(R.string.login_error)
                )

                Effect.UIEffect.ShowErrorMessage.UnknownError -> snackbarHostState.showSnackbar(
                    context.getString(R.string.generic_error)
                )
            }
        }

        RemembrallScaffold(
            topBar = {
                RemembrallToolbar(
                    backEnabled = false,
                    modifier = Modifier,
                    title = {
                        RemembrallTopBarTitle(stringResource(R.string.login_title))
                    },
                )
            }
        ) {
            RemembrallPage(modifier = Modifier.padding(it)) {
                Column(
                    Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    RemembrallButton(
                        text = stringResource(R.string.login_title),
                        loading = state.loading
                    ) {
                        viewModel.onEvent(Event.SignIn)
                    }
                }
            }

        }

        BackHandler { navigator.close() }
    }
