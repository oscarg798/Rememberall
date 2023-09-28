package com.oscarg798.remembrall.home

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.google.accompanist.pager.ExperimentalPagerApi
import com.oscarg798.remembrall.list.ui.TaskListScreen
import com.oscarg798.remembrall.ui.navigation.LocalNavControllerProvider
import com.oscarg798.remembrall.ui.navigation.Router
import com.oscarg798.remembrall.ui.theming.RemembrallScaffold
import com.oscarg798.remembrall.ui.theming.RemembrallTopBar
import com.oscarg798.remembrall.ui.theming.RemembrallTopBarTitle

@ExperimentalPagerApi
fun NavGraphBuilder.homeScreen(onFinishRequest: () -> Unit) = composable(
    route = Router.Home.route,
    deepLinks = Router.Home.getDeepLinks()
) { backStackEntry ->

    val navController = LocalNavControllerProvider.current

    RemembrallScaffold(
        topBar = {
            RemembrallTopBar(
                title = {
                    RemembrallTopBarTitle(stringResource(R.string.home_title))
                },
                actions = {
                    ToolbarRightAction {
                        Router.Profile.navigate(navController)
                    }
                }
            )
        }
    ) {
        TaskListScreen(
            backStackEntry
        )
    }

    BackHandler {
        onFinishRequest()
    }
}

@Composable
fun ToolbarRightAction(
    onProfileButtonClicked: () -> Unit
) {
    ProfileButton(
        onProfileClicked = onProfileButtonClicked
    )
}
