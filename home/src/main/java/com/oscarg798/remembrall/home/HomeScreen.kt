package com.oscarg798.remembrall.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.oscarg798.remembrall.checklist.ui.CheckListScreen
import com.oscarg798.remembrall.tasklist.ui.TaskListScreen
import com.oscarg798.remembrall.ui_common.navigation.LocalNavControllerProvider
import com.oscarg798.remembrall.ui_common.navigation.Router
import com.oscarg798.remembrall.ui_common.ui.theming.RemembrallPage
import com.oscarg798.remembrall.ui_common.ui.theming.RemembrallScaffold
import com.oscarg798.remembrall.ui_common.ui.theming.RemembrallTheme
import com.oscarg798.remembrall.ui_common.ui.theming.RemembrallTopBar
import com.oscarg798.remembrall.ui_common.ui.theming.RemembrallTopBarTitle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@ExperimentalPagerApi
fun NavGraphBuilder.homeScreen(onFinishRequest: () -> Unit) = composable(
    route = Router.Home.route,
    deepLinks = Router.Home.getDeepLinks()
) { backStackEntry ->

    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()
    val navController = LocalNavControllerProvider.current

    val tabs = remember {
        listOf(
            TabItem(R.string.task_list_screen_title) {
                TaskListScreen(
                    backStackEntry
                )
            },
            TabItem(R.string.checklist_title) {
                CheckListScreen(
                    backStackEntry
                )
            }
        )
    }

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
        Column {
            Tabs(pagerState, tabs, coroutineScope)
            TabsContent(tabs = tabs, pagerState = pagerState)
        }
    }

    BackHandler {
        onFinishRequest()
    }
}

@ExperimentalPagerApi
@Composable
private fun Tabs(
    pagerState: PagerState,
    tabs: List<TabItem>,
    coroutineScope: CoroutineScope
) {
    TabRow(
        selectedTabIndex = pagerState.currentPage,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
            )
        }, backgroundColor = MaterialTheme.colorScheme.background
    ) {
        tabs.forEachIndexed { index, tabItem ->
            Tab(selected = pagerState.currentPage == index, onClick = {
                coroutineScope.launch { pagerState.animateScrollToPage(index) }

            }) {
                Text(
                    text = stringResource(id = tabItem.title),
                    modifier= Modifier.padding(RemembrallTheme.dimens.Small),
                    style = MaterialTheme.typography.bodyLarge.merge(TextStyle(MaterialTheme.colorScheme.onSurface))
                )
            }
        }
    }
}

@ExperimentalPagerApi
@Composable
private fun TabsContent(tabs: List<TabItem>, pagerState: PagerState) {
    HorizontalPager(count = tabs.size, state = pagerState) { index ->
        RemembrallPage {
            tabs[index].screen()
        }
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

