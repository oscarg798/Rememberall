package com.oscarg798.remembrall.navigationimpl

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.navOptions
import com.oscarg798.remembrall.activityprovider.ActivityProvider
import com.oscarg798.remembrall.navigation.DeeplinkRouteFactory
import com.oscarg798.remembrall.navigation.Navigator
import com.oscarg798.remembrall.navigation.Route
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

interface NavigatorFactory {

    fun create(navController: NavController): Navigator
}

internal class NavigatorImpl @AssistedInject constructor(
   private val activityProvider: ActivityProvider,
    @Assisted private val navController: NavController,
    private val deeplinkRouteFactories: Map<Route, @JvmSuppressWildcards DeeplinkRouteFactory>,
) : Navigator {

    override fun navigate(route: Route, arguments: Bundle?) {
        navController.navigate(
            getDeeplinkRouteFactory(route).invoke(route.uriPattern, arguments),
            navOptions {
                this.launchSingleTop = true
            }
        )
    }

    override fun navigateBack() {
        navController.popBackStack()
    }

    override fun close() {
        activityProvider.provide()?.finishAffinity()
       activityProvider.provide()?.finishAndRemoveTask()
    }

    private fun getDeeplinkRouteFactory(route: Route): DeeplinkRouteFactory {
        return deeplinkRouteFactories[route] ?: DefaultDeeplinkRouteFactory
    }

    @AssistedFactory
    interface Factory : NavigatorFactory {

        override fun create(navController: NavController): NavigatorImpl
    }
}