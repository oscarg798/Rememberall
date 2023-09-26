package com.oscarg798.remembrall.login

import androidx.test.core.app.ActivityScenario
import com.facebook.testing.screenshot.Screenshot
import com.oscarg798.remembrall.common_auth.di.AuthModule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Rule
import org.junit.Test

@UninstallModules(AuthModule::class)
@HiltAndroidTest
class LoginActivityTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Test
    fun LoginScreenShouldBePurple() {
        val scenario = ActivityScenario.launch(LoginActivity::class.java)
        scenario.onActivity {
            Screenshot.snapActivity(it)
                .setName("LoginScreenShouldBePurple")
                .record()
        }
    }
}
