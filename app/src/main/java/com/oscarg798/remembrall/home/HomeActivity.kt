package com.oscarg798.remembrall.home

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.oscarg798.remembrall.navigation.LocalNavigatorProvider
import com.oscarg798.remembrall.navigation.Page
import com.oscarg798.remembrall.navigation.Route
import com.oscarg798.remembrall.navigationimpl.NavigatorFactory
import com.oscarg798.remembrall.ui.navigation.LocalNavControllerProvider
import com.oscarg798.remembrall.ui.theming.RemembrallTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@ExperimentalPagerApi
@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    @Inject
    protected lateinit var navigatorFactory: NavigatorFactory

    @Inject
    protected lateinit var pages: Set<@JvmSuppressWildcards Page>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RemembrallTheme {

                val navController = rememberNavController()
                val navigator = remember { navigatorFactory.create(navController) }

                CompositionLocalProvider(
                    LocalNavControllerProvider provides navController,
                    LocalNavigatorProvider provides navigator
                ) {
                    NavHost(navController = navController, startDestination = Route.SPLASH.path) {
                        pages.forEach { it.build(this) }
                    }
                }
            }
        }
    }
}
