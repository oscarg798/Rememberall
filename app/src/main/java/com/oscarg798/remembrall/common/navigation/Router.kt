package com.oscarg798.remembrall.common.navigation

import android.net.Uri
import android.os.Bundle
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.navOptions

sealed class Router(val route: String, val uriPattern: String) {

    object TaskList : Router(TaskListRoute, TaskListUriPattern)
    object AddTask : Router(AddTaskRoute, AddTaskUriPattern)
    object Profile : Router(ProfileRoute, ProfileUriPattern)
    object Splash : Router(SplashRoute, SplashUriPattern)

    fun getDeeplinkNavigationRoute(arguments: Bundle? = null): Uri {
        return uriPattern.toUri()
    }

    fun navigate(navController: NavController, arguments: Bundle? = null) {
        navController.navigate(
            getDeeplinkNavigationRoute(arguments),
            navOptions {
                this.launchSingleTop = true
            }
        )
    }
}

private val DeepLinkUri = "https://remembrall"

private val TaskListRoute = "taskList"
private val TaskListUriPattern = "$DeepLinkUri/$TaskListRoute"

private val AddTaskRoute = "addTask"
private val AddTaskUriPattern = "$DeepLinkUri/$AddTaskRoute"

private val ProfileRoute = "profile"
private val ProfileUriPattern = "$DeepLinkUri/$ProfileRoute"

private val SplashRoute = "splash"
private val SplashUriPattern = "$DeepLinkUri/$SplashRoute"
