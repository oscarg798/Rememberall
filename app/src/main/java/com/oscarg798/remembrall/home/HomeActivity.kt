package com.oscarg798.remembrall.home

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.CompositionLocalProvider
import com.google.accompanist.pager.ExperimentalPagerApi
import com.oscarg798.remembrall.navigation.LocalNavigatorProvider
import com.oscarg798.remembrall.navigation.Navigator
import com.oscarg798.remembrall.navigationimpl.NavigatorFactory
import com.oscarg798.remembrall.ui.theming.RemembrallTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@ExperimentalPagerApi
@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    @Inject
    protected lateinit var navigatorFactory: NavigatorFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RemembrallTheme {

                MainScreen(navigatorFactory) {
                    finishAndRemoveTask()
                }


            }
        }
    }
}
