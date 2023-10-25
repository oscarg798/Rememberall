package com.oscarg798.remembrall.widgetimpl.navigation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.oscarg798.remembrall.navigation.DeeplinkRouteFactory
import com.oscarg798.remembrall.navigation.Navigator
import com.oscarg798.remembrall.navigation.Route
import com.oscarg798.remembrall.task.navigation.TaskDetailDeepLinkRouteFactory
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Qualifier

@Qualifier
internal annotation class WidgetNavigatorQualifier

internal class WidgetNavigator @Inject constructor(
    @ApplicationContext private val context: Context,
    private val taskDetailDeepLinkRouteFactory: TaskDetailDeepLinkRouteFactory
) : Navigator {

    override fun navigate(route: Route, arguments: Bundle?) {
        val uri =
            when (route) {
                Route.DETAIL -> taskDetailDeepLinkRouteFactory(route.uriPattern, arguments)
                else -> route.uriPattern
            }
        context.startActivity(Intent().apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            data = uri
        })
    }

    override fun navigateBack() {
        error("Navigate back not supported in Widgets")
    }

    override fun close() {
        error("Close not supported in Widgets")
    }
}