package com.oscarg798.remembrall.home

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.oscarg798.remembrall.homeutils.HomeContent
import com.oscarg798.remembrall.navigation.LocalNavigatorProvider
import com.oscarg798.remembrall.navigation.Page
import com.oscarg798.remembrall.navigation.Route
import com.oscarg798.remembrall.ui.theming.RemembrallScaffold
import com.oscarg798.remembrall.ui.theming.RemembrallTopBar
import com.oscarg798.remembrall.ui.theming.RemembrallTopBarTitle
import javax.inject.Inject

internal class HomePage @Inject constructor(
    private val homeContent: HomeContent
) : Page {

    override fun build(builder: NavGraphBuilder) {
        return builder.homeScreen { homeContent.Content(it) }
    }
}

private fun NavGraphBuilder.homeScreen(
    content: @Composable (NavBackStackEntry) -> Unit,
) = composable(
    route = Route.HOME.path,
    deepLinks = listOf(
        navDeepLink {
            uriPattern = Route.HOME.uriPattern.toString()
        }
    )
) { backStackEntry ->

    val navigator = LocalNavigatorProvider.current

    RemembrallScaffold(
        topBar = {
            RemembrallTopBar(
                title = {
                    RemembrallTopBarTitle(stringResource(R.string.home_title))
                },
                actions = {
                    ToolbarRightAction {
                        navigator.navigate(Route.PROFILE)
                    }
                }
            )
        }
    ) {
        content(backStackEntry)
    }

    BackHandler {
        navigator.close()
    }
}

@Composable
private fun ToolbarRightAction(
    onProfileButtonClicked: () -> Unit
) {
    ProfileButton(
        onProfileClicked = onProfileButtonClicked
    )
}
