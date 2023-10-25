package com.oscarg798.remembrall.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.lifecycleScope
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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    @Inject
    protected lateinit var navigatorFactory: NavigatorFactory

    @Inject
    protected lateinit var pages: Set<@JvmSuppressWildcards Page>

    private val navUris = MutableSharedFlow<Uri>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RemembrallTheme {

                val navController = rememberNavController()
                val navigator = remember { navigatorFactory.create(navController) }

                LaunchedEffect(navController) {
                    navUris.collect {
                        navController.navigate(it)
                    }
                }

                CompositionLocalProvider(
                    LocalNavControllerProvider provides navController,
                    LocalNavigatorProvider provides navigator
                ) {
                    NavHost(navController = navController, startDestination = Route.CART.path) {
                        pages.forEach { it.build(this) }
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val uri = intent.data ?: return
        lifecycleScope.launch { navUris.emit(uri) }
    }
}
