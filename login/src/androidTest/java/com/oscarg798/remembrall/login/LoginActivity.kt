package com.oscarg798.remembrall.login

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.oscarg798.remembrall.login.ui.loginScreen
import com.oscarg798.remembrall.ui.navigation.LocalNavControllerProvider
import com.oscarg798.remembrall.ui.navigation.Router
import com.oscarg798.remembrall.ui.theming.RemembrallTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            CompositionLocalProvider(LocalNavControllerProvider provides navController) {
                com.oscarg798.remembrall.ui.theming.RemembrallTheme {
                    NavHost(navController = navController, startDestination = Router.Login.route) {
                        loginScreen(onFinishRequest = {
                            finish()
                        })
                    }
                }
            }
        }
    }
}
