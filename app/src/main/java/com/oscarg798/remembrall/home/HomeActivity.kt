package com.oscarg798.remembrall.home

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.google.accompanist.pager.ExperimentalPagerApi
import com.oscarg798.remembrall.ui_common.ui.theming.RemembrallTheme
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalPagerApi
@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    @ExperimentalPagerApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RemembrallTheme {
                MainScreen {
                    finishAndRemoveTask()
                }
            }
        }
    }
}
