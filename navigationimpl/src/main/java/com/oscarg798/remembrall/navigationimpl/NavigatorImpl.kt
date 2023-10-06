package com.oscarg798.remembrall.navigationimpl

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.navOptions
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
    @Assisted private val navController: NavController,
    private val deeplinkRouteFactories: Map<Class<out Route>, @JvmSuppressWildcards DeeplinkRouteFactory>
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

    private fun getDeeplinkRouteFactory(route: Route): DeeplinkRouteFactory {
        return deeplinkRouteFactories[route::class.java] ?: DefaultDeeplinkRouteFactory
    }

    @AssistedFactory
    interface Factory : NavigatorFactory {

        override fun create(navController: NavController): NavigatorImpl
    }
}