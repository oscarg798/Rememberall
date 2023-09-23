package com.oscarg798.remembrall.login.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.oscarg798.remembrall.login.R
import com.oscarg798.remembrall.login.domain.Effect
import com.oscarg798.remembrall.login.domain.Event
import com.oscarg798.remembrall.ui_common.navigation.LocalNavControllerProvider
import com.oscarg798.remembrall.ui_common.navigation.Router
import com.oscarg798.remembrall.ui_common.ui.RemembrallButton
import com.oscarg798.remembrall.ui_common.ui.theming.RemembrallScaffold
import com.oscarg798.remembrall.ui_common.ui.theming.RemembrallTopBar
import com.oscarg798.remembrall.ui_common.ui.theming.RemembrallTopBarTitle

fun NavGraphBuilder.loginScreen(onFinishRequest: () -> Unit) =
    composable(
        route = Router.Login.route,
        deepLinks = Router.Login.getDeepLinks()
    ) { backStackEntry ->
        val viewModel: LoginViewModel = hiltViewModel(backStackEntry)

        val initialState = remember(viewModel) {
            viewModel.model.value
        }
        val context = LocalContext.current
        val state by viewModel.model.collectAsState(initialState)
        val uiEffects by viewModel.uiEffect.collectAsState(initial = null)
        val snackbarHostState = remember { SnackbarHostState() }
        val navController = LocalNavControllerProvider.current

        LaunchedEffect(key1 = uiEffects) {
            val effect = uiEffects ?: return@LaunchedEffect

            when (effect) {
                Effect.UIEffect.NavigateBack -> navController.popBackStack()
                Effect.UIEffect.NavigateToHome -> Router.Home.navigate(navController)
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
                RemembrallTopBar(
                    title = {
                        RemembrallTopBarTitle(stringResource(R.string.login_title))
                    }
                )
            }
        ) {
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                RemembrallButton(
                    text = stringResource(R.string.common_signin_button_text),
                    loading = state.loading
                ) {
                    viewModel.onEvent(Event.SignIn)
                }
            }
        }

        BackHandler { onFinishRequest() }
    }

