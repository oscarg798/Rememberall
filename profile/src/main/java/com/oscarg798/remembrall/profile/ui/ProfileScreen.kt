package com.oscarg798.remembrall.profile.ui

import com.oscarg798.remembrall.common.R as CommonR
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.oscarg798.remembrall.navigation.LocalNavigatorProvider
import com.oscarg798.remembrall.navigation.Page
import com.oscarg798.remembrall.navigation.Route
import com.oscarg798.remembrall.profile.ProfileViewModel
import com.oscarg798.remembrall.ui.theming.RemembrallPage
import com.oscarg798.remembrall.ui.theming.RemembrallScaffold
import com.oscarg798.remembrall.ui.theming.RemembrallTopBar
import com.oscarg798.remembrall.ui.theming.RemembrallTopBarTitle

internal object ProfilePage: Page {

    override fun build(builder: NavGraphBuilder) {
        return builder.profileScreen()
    }
}

private fun NavGraphBuilder.profileScreen() =
    composable(
        route = Route.PROFILE.path,
        deepLinks = listOf(
            navDeepLink {
                uriPattern = Route.PROFILE.uriPattern.toString()
            }
        )
    ) { backStackEntry ->

        val viewModel: ProfileViewModel = hiltViewModel(backStackEntry)
        val state by viewModel.state.collectAsState(ProfileViewModel.ViewState())
        val events by viewModel.events.collectAsState(null)
        val navigator = LocalNavigatorProvider.current

        LaunchedEffect(key1 = viewModel) {
            viewModel.getProfileInformation()
        }

        LaunchedEffect(events) {
            val event = events ?: return@LaunchedEffect
            if (event is ProfileViewModel.Event.NavigateToLogin) {
                navigator.navigate(Route.LOGIN)
            }
        }

        RemembrallScaffold(
            topBar = {
                RemembrallTopBar(
                    title = {
                        RemembrallTopBarTitle(stringResource(CommonR.string.profile_title))
                    }, backButtonAction = {
                    navigator.navigateBack()
                }
                )
            }
        ) {
            RemembrallPage {
                when {
                    state.loading -> LoadingProfile()
                    state.profileInformation != null -> ProfileDetails(
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
    }

const val UserNameId = "UserGivenName"
