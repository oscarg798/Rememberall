package com.oscarg798.remembrall.common.navigation

import android.net.Uri
import android.os.Bundle
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.navOptions
import com.oscarg798.remembrall.common.navigation.Router.TaskDetail.TaskIdArgument

sealed class Router(val route: String, val uriPattern: String) {

    object TaskList : Router(TaskListRoute, TaskListUriPattern)
    object AddTask : Router(AddTaskRoute, AddTaskUriPattern)
    object Profile : Router(ProfileRoute, ProfileUriPattern)
    object Splash : Router(SplashRoute, SplashUriPattern)

    object TaskDetail : Router(TaskDetailRoute, TaskDetailUriPattern) {

        override fun getDeeplinkNavigationRoute(arguments: Bundle?): Uri {
            require(arguments != null && arguments.containsKey(TaskIdArgument))
            return uriPattern.replace(
                "{$TaskIdArgument}",
                arguments.getString(
                    TaskIdArgument
                )!!
            ).toUri()
        }

        const val TaskIdArgument = "TaskId"
    }

    open fun getDeeplinkNavigationRoute(arguments: Bundle? = null): Uri {
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

private const val DeepLinkUri = "https://remembrall"

private val TaskListRoute = "taskList"
private val TaskListUriPattern = "$DeepLinkUri/$TaskListRoute"

private const val TaskDetailRoute = "taskDetail"
private const val TaskDetailUriPattern = "$DeepLinkUri/$TaskDetailRoute/{$TaskIdArgument}"

private const val AddTaskRoute = "addTask"
private const val AddTaskUriPattern = "$DeepLinkUri/$AddTaskRoute"

private const val ProfileRoute = "profile"
private const val ProfileUriPattern = "$DeepLinkUri/$ProfileRoute"

private const val SplashRoute = "splash"
private const val SplashUriPattern = "$DeepLinkUri/$SplashRoute"
