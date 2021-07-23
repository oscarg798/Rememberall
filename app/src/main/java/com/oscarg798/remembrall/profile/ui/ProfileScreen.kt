package com.oscarg798.remembrall.profile.ui

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.oscarg798.remembrall.R
import com.oscarg798.remembrall.common.navigation.Router
import com.oscarg798.remembrall.common.ui.RemembrallPage
import com.oscarg798.remembrall.common.ui.RemembrallScaffold
import com.oscarg798.remembrall.common.ui.RemembrallTopBar
import com.oscarg798.remembrall.common.ui.RemembrallTopBarTitle
import com.oscarg798.remembrall.common.ui.registerActivityResultCallback
import com.oscarg798.remembrall.profile.ProfileViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

fun NavGraphBuilder.profileScreen() =
    composable(route = Router.Profile.route, deepLinks = getDeepLinks()) { backStackEntry ->

        val viewModel: ProfileViewModel = hiltNavGraphViewModel(backStackEntry)
        val state by viewModel.state.collectAsState(ProfileViewModel.ViewState())
        val snackbarHostState = remember { SnackbarHostState() }
        val coroutineScope = rememberCoroutineScope()
        val launcher = getAuthObserverLauncher(viewModel)

        RemembrallScaffold(
            topBar = {
                RemembrallTopBar(
                    title = {
                        RemembrallTopBarTitle(stringResource(R.string.profile_title))
                    }
                )
            },
            snackbarHostState = snackbarHostState
        ) {
            RemembrallPage {
                when {
                    state.loading -> LoadingProfile()
                    state.profileInformation == null -> SignInButton {
                        viewModel.signIn()
                    }
                    else -> ProfileDetails(
                        profileInformation = state.profileInformation!!,
                        onNotificationActivated = {
                            viewModel.onNotificationValueChange(it)
                        },
                        onLogOutClick = {
                            viewModel.logout()
                        },
                        onCalendarSelection = {
                            viewModel.onCalendarSelected(it)
                        }
                    )
                }
            }
        }

        LaunchedEffect(viewModel) {
            viewModel.getProfileInformation()
        }

        observeEvents(coroutineScope, viewModel, launcher)
    }

private fun observeEvents(
    coroutineScope: CoroutineScope,
    viewModel: ProfileViewModel,
    launcher: ActivityResultLauncher<GoogleSignInOptions>
) {
    coroutineScope.launch {
        viewModel.events.collect { event ->
            when (event) {
                is ProfileViewModel.Event.RequestAuth -> launcher.launch(event.options)
            }
        }
    }
}

@Composable
private fun getAuthObserverLauncher(viewModel: ProfileViewModel) =
    registerActivityResultCallback(object :
            ActivityResultContract<GoogleSignInOptions, ActivityResult>() {
            override fun createIntent(context: Context, input: GoogleSignInOptions): Intent {
                return GoogleSignIn.getClient(context, input).signInIntent
            }

            override fun parseResult(resultCode: Int, intent: Intent?): ActivityResult {
                return ActivityResult(resultCode, intent)
            }
        }) {
        viewModel.onAuth()
    }

private fun getDeepLinks() = listOf(
    navDeepLink {
        uriPattern = Router.Profile.uriPattern
    }
)

const val UserNameId = "UserGivenName"
const val SignInButtonId = "SignIn"
