package com.oscarg798.remembrall.profile.ui

import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.oscarg798.remembrall.profile.ProfileViewModel
import com.oscarg798.remembrall.profile.R
import com.oscarg798.remembrall.ui_common.navigation.LocalNavControllerProvider
import com.oscarg798.remembrall.ui_common.navigation.Router
import com.oscarg798.remembrall.ui_common.ui.theming.RemembrallPage
import com.oscarg798.remembrall.ui_common.ui.theming.RemembrallScaffold
import com.oscarg798.remembrall.ui_common.ui.theming.RemembrallTopBar
import com.oscarg798.remembrall.ui_common.ui.theming.RemembrallTopBarTitle

fun NavGraphBuilder.profileScreen() =
    composable(
        route = Router.Profile.route,
        deepLinks = Router.Profile.getDeepLinks()
    ) { backStackEntry ->

        val viewModel: ProfileViewModel = hiltViewModel(backStackEntry)
        val state by viewModel.state.collectAsState(ProfileViewModel.ViewState())
        val events by viewModel.events.collectAsState(null)
        val snackbarHostState = remember { SnackbarHostState() }
        val navController = LocalNavControllerProvider.current

        LaunchedEffect(key1 = viewModel) {
            viewModel.getProfileInformation()
        }

        LaunchedEffect(events) {
            val event = events ?: return@LaunchedEffect
            if(event is ProfileViewModel.Event.NavigateToLogin){
                Router.Login.navigate(navController = navController)
            }
        }

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
