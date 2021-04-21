package com.oscarg798.remembrall.home

import androidx.activity.compose.BackHandler
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.KEY_ROUTE
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.oscarg798.remembrall.addtask.ui.addTaskScreen
import com.oscarg798.remembrall.common.navigation.Router
import com.oscarg798.remembrall.common.ui.PageConfigurator
import com.oscarg798.remembrall.common.ui.RemembrallScaffold
import com.oscarg798.remembrall.profile.ui.profileScreen
import com.oscarg798.remembrall.splash.SplashScreen
import com.oscarg798.remembrall.tasklist.ui.listScreen

@Composable
fun HomeScreen(onFinishRequest: () -> Unit) {
    val topBarBackgroundColor = MaterialTheme.colors.secondary
    val pageBackgroundColor = MaterialTheme.colors.secondary
    val snackbarHostState = remember { SnackbarHostState() }
    val navController = rememberNavController()
    val currentRoute = currentRoute(navController)

    val toolbarConfigurator =
        remember {
            mutableStateOf(
                PageConfigurator(
                    topBarBackgroundColor = topBarBackgroundColor,
                    pageBackgroundColor = pageBackgroundColor
                )
            )
        }

    RemembrallScaffold(toolbarConfigurator.value, snackbarHostState) {
        NavHost(navController = navController, startDestination = Router.Splash.route) {
            SplashScreen(navController, toolbarConfigurator)
            listScreen(
                navController = navController,
                pageConfigurator = toolbarConfigurator
            )
            addTaskScreen(
                navController = navController,
                pageConfigurator = toolbarConfigurator,
                snackbarHostState = snackbarHostState
            )
            profileScreen(
                pageConfigurator = toolbarConfigurator
            )
        }

        BackButtonHandler(currentRoute, onFinishRequest, navController)
    }
}

/**
 * We want to finish the activity when pressing back on
 * the tasks list, we want splash screen being shown just one time
 * maybe there is a nicer way to do this
 */
@Composable
private fun BackButtonHandler(
    currentRoute: String?,
    onFinishRequest: () -> Unit,
    navController: NavHostController
) {
    BackHandler {
        if (currentRoute == Router.TaskList.route) {
            onFinishRequest()
        } else {
            navController.popBackStack()
        }
    }
}

@Composable
private fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.arguments?.getString(KEY_ROUTE)
}
