package com.oscarg798.remembrall.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.oscarg798.remembrall.addtask.ui.addTaskScreen
import com.oscarg798.remembrall.common.viewmodel.ViewModelStore
import com.oscarg798.remembrall.edittask.editTaskScreen
import com.oscarg798.remembrall.profile.ui.profileScreen
import com.oscarg798.remembrall.splash.ui.SplashScreen
import com.oscarg798.remembrall.taskdetail.ui.taskDetailsScreen
import com.oscarg798.remembrall.tasklist.ui.listScreen
import com.oscarg798.remembrall.ui_common.navigation.Router

@Composable
fun HomeScreen(onFinishRequest: () -> Unit) {
    val navController = rememberNavController()
    val viewModelStore = remember { ViewModelStore() }

    NavHost(navController = navController, startDestination = Router.Splash.route) {
        SplashScreen(navController)
        listScreen(
            navController = navController,
            onFinishRequest = onFinishRequest
        )
        addTaskScreen(navController = navController)
        profileScreen()
        taskDetailsScreen(navController = navController, viewModelStore = viewModelStore)
        editTaskScreen(navController = navController, viewModelStore = viewModelStore)
    }
}
