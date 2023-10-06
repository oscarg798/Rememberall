package com.oscarg798.remembrall.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.oscarg798.remembrall.addtask.ui.addTaskScreen
import com.oscarg798.remembrall.common.viewmodel.ViewModelStore
import com.oscarg798.remembrall.detail.ui.taskDetailScreen
import com.oscarg798.remembrall.login.ui.loginScreen
import com.oscarg798.remembrall.navigation.LocalNavigatorProvider
import com.oscarg798.remembrall.navigationimpl.NavigatorFactory
import com.oscarg798.remembrall.profile.ui.profileScreen
import com.oscarg798.remembrall.splash.ui.splashScreen
import com.oscarg798.remembrall.ui.navigation.LocalNavControllerProvider
import com.oscarg798.remembrall.ui.navigation.Router

@ExperimentalPagerApi
@Composable
fun MainScreen(navigatorFactory: NavigatorFactory, onFinishRequest: () -> Unit) {
    val navController = rememberNavController()
    val navigator = remember { navigatorFactory.create(navController) }

    CompositionLocalProvider(
        LocalNavControllerProvider provides navController,
        LocalNavigatorProvider provides navigator
    ) {
        NavHost(navController = navController, startDestination = Router.Splash.route) {
            splashScreen()
            homeScreen() { onFinishRequest() }
            loginScreen(onFinishRequest = onFinishRequest)
            addTaskScreen()
            profileScreen()
            taskDetailScreen()
        }
    }
}
