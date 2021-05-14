package com.oscarg798.remembrall.profile.ui

import android.content.Context
import android.content.Intent
import androidx.activity.compose.registerForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.oscarg798.remembrall.R
import com.oscarg798.remembrall.common.extensions.horizontalToParent
import com.oscarg798.remembrall.common.extensions.toParentTop
import com.oscarg798.remembrall.common.extensions.verticalToParent
import com.oscarg798.remembrall.common.navigation.Router
import com.oscarg798.remembrall.common.ui.PageConfigurator
import com.oscarg798.remembrall.common.ui.RemembrallPage
import com.oscarg798.remembrall.common.ui.theming.Dimensions
import com.oscarg798.remembrall.profile.ProfileViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

fun NavGraphBuilder.profileScreen(
    pageConfigurator: MutableState<PageConfigurator>
) = composable(Router.Profile.route, deepLinks = getDeepLinks()) { backStackEntry ->

    val viewModel: ProfileViewModel = hiltNavGraphViewModel(backStackEntry)
    val state by viewModel.state.collectAsState(ProfileViewModel.ViewState())
    val pageConfiguration = getPageConfiguration()

    val coroutineScope = rememberCoroutineScope()
    val launcher = getAuthObserverLauncher(viewModel)

    RemembrallPage(pageConfigurator = pageConfiguration) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize(),
            constraintSet = getConstraints(state)
        ) {
            when {
                state.loading -> LoadingProfile()
                state.profileInformation == null -> SignInButton {
                    viewModel.signIn()
                }
                else -> ProfileDetails(
                    state.profileInformation!!,
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
        pageConfigurator.value = pageConfiguration
        viewModel.getProfileInformation()
    }

    observeEvents(coroutineScope, viewModel, launcher)
}

@Composable
private fun getPageConfiguration() = PageConfigurator.buildPageConfigurator().copy(
    title = stringResource(R.string.profile_title),
    addButtonEnabled = false,
)

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

private fun getConstraints(state: ProfileViewModel.ViewState) = ConstraintSet {
    val signInButton = createRefFor(SignInButtonId)
    val userName = createRefFor(UserNameId)
    val calendarSelector = createRefFor(CalendarSelectorId)
    val logOutButton = createRefFor(LogOutButtonId)
    val notificationCard = createRefFor(NotificationCardId)

    when {
        state.profileInformation == null && !state.loading -> constrain(signInButton) {
            verticalToParent()
            horizontalToParent()
        }
        else -> {
            constrain(userName) {
                toParentTop(margin = Dimensions.Spacing.Medium)
                horizontalToParent(
                    startMargin = Dimensions.Spacing.Medium,
                    endMargin = Dimensions.Spacing.Medium
                )
            }

            constrain(calendarSelector) {
                top.linkTo(userName.bottom)
                horizontalToParent(
                    startMargin = Dimensions.Spacing.Medium,
                    endMargin = Dimensions.Spacing.Medium
                )
            }

            constrain(notificationCard) {
                top.linkTo(calendarSelector.bottom)
                horizontalToParent()
            }

            constrain(logOutButton) {
                linkTo(top = notificationCard.bottom, bottom = parent.bottom)
                horizontalToParent()
            }
        }
    }
}

@Composable
private fun getAuthObserverLauncher(viewModel: ProfileViewModel) =
    registerForActivityResult(object :
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

const val LogOutButtonId = "LogOutButton"
const val UserNameId = "UserGivenName"
const val SignInButtonId = "SignIn"
const val CalendarSelectorId = "CalendarSelector"
const val NotificationCardId = "NotificationCard"
private const val AlignToBottom = 1f
