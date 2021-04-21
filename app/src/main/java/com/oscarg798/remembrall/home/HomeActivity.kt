package com.oscarg798.remembrall.home

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.oscarg798.remembrall.common.ui.theming.RemembrallTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RemembrallTheme {
                HomeScreen {
                    finishAndRemoveTask()
                }
            }
        }
    }
}
