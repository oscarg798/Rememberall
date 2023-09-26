package com.oscarg798.remembrall.ui.navigation

import android.net.Uri
import android.os.Bundle
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.NavDeepLink
import androidx.navigation.navDeepLink
import androidx.navigation.navOptions
import com.oscarg798.remembrall.ui.navigation.Router.ChecklistDetail.ChecklistIdArgument
import com.oscarg798.remembrall.ui.navigation.Router.TaskDetail.TaskIdArgument

sealed class Router(val route: String, val uriPattern: String) {

    object TaskList : Router(TaskListRoute, TaskListUriPattern)
    object AddTask : Router(AddTaskRoute, AddTaskUriPattern)
    object Profile : Router(ProfileRoute, ProfileUriPattern)
    object Splash : Router(SplashRoute, SplashUriPattern)
    object Login : Router(LoginRoute, LoginUriPattern)
    object Home : Router(HomeRoute, HomeUriPattern)
    object AddChecklist : Router(AddCheckList, AddCheckListUriPattern)

    object Edit : Router(EditRoute, EditUriPattern) {
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

    object ChecklistDetail : Router(ChecklistDetailRoute, ChecklistDetailUriPattern) {

        override fun getDeeplinkNavigationRoute(arguments: Bundle?): Uri {
            require(arguments != null && arguments.containsKey(ChecklistIdArgument))
            return uriPattern.replace(
                "{$ChecklistIdArgument}",
                arguments.getString(
                    ChecklistIdArgument
                )!!
            ).toUri()
        }

        const val ChecklistIdArgument = "ChecklistId"
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

    fun getDeepLinks(): List<NavDeepLink> = listOf(
        navDeepLink {
            uriPattern = this@Router.uriPattern
        }
    )
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

private const val EditRoute = "edit"
private const val EditUriPattern = "$DeepLinkUri/$EditRoute/{$TaskIdArgument}"

private const val LoginRoute = "login"
private const val LoginUriPattern = "$DeepLinkUri/$LoginRoute"

private const val HomeRoute = "home"
private const val HomeUriPattern = "$DeepLinkUri/$HomeRoute"

private const val AddCheckList = "checklist-add"
private const val AddCheckListUriPattern = "$DeepLinkUri/$AddCheckList"

private const val ChecklistDetailRoute = "checklist-detail"
private const val ChecklistDetailUriPattern = "$DeepLinkUri/$ChecklistDetailRoute/" +
    "{$ChecklistIdArgument}"
