package com.oscarg798.remembrall.login.ui

import android.content.Context
import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.oscarg798.remembrall.login.LoginViewModel
import com.oscarg798.remembrall.login.R
import com.oscarg798.remembrall.ui_common.navigation.LocalNavControllerProvider
import com.oscarg798.remembrall.ui_common.navigation.Router
import com.oscarg798.remembrall.ui_common.ui.RemembrallButton
import com.oscarg798.remembrall.ui_common.ui.theming.RemembrallScaffold
import com.oscarg798.remembrall.ui_common.ui.theming.RemembrallTopBar
import com.oscarg798.remembrall.ui_common.ui.theming.RemembrallTopBarTitle
import com.oscarg798.remembrall.ui_common.ui.theming.registerActivityResultCallback

fun NavGraphBuilder.loginScreen(onFinishRequest: () -> Unit) =
    composable(
        route = Router.Login.route,
        deepLinks = Router.Login.getDeepLinks()
    ) { backStackEntry ->

        val viewModel: LoginViewModel = hiltViewModel(backStackEntry)
        val state by viewModel.state.collectAsState(LoginViewModel.ViewState())
        val events by viewModel.events.collectAsState(initial = null)
        val authLauncher = getAuthObserverLauncher { viewModel.onExternalLogin() }
        val snackbarHostState = remember { SnackbarHostState() }
        val navController = LocalNavControllerProvider.current

        LaunchedEffect(key1 = events) {
            val event = events ?: return@LaunchedEffect

            when (event) {
                is LoginViewModel.Event.RequestAuth -> authLauncher.launch(event.googleSignInOptions)
                is LoginViewModel.Event.NavigateToHome -> Router.TaskList.navigate(navController)
                is LoginViewModel.Event.ShowErrorMessage -> snackbarHostState.showSnackbar(event.message)
            }
        }

        RemembrallScaffold(
            topBar = {
                RemembrallTopBar(
                    title = {
                        RemembrallTopBarTitle(stringResource(R.string.login_title))
                    }
                )
            }, snackbarHostState = snackbarHostState
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
                    viewModel.signIn()
                }
            }
        }

        BackHandler { onFinishRequest() }
    }

@Composable
private fun getAuthObserverLauncher(onAuth: () -> Unit) =
    registerActivityResultCallback(object :
        ActivityResultContract<GoogleSignInOptions, ActivityResult>() {
        override fun createIntent(context: Context, input: GoogleSignInOptions): Intent {
            return GoogleSignIn.getClient(context, input).signInIntent
        }

        override fun parseResult(resultCode: Int, intent: Intent?): ActivityResult {
            return ActivityResult(resultCode, intent)
        }
    }) {
        onAuth()
    }

