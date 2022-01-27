package com.oscarg798.remembrall.login

import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.test.runner.AndroidJUnitRunner
import com.facebook.testing.screenshot.ScreenshotRunner

class CustomTestRunner : AndroidJUnitRunner(){

    override fun onCreate(args: Bundle) {
        ScreenshotRunner.onCreate(this, args);
        super.onCreate(args)
    }

    override fun finish(resultCode: Int, results: Bundle?) {
        ScreenshotRunner.onDestroy();
        super.finish(resultCode, results)
    }

    override fun newApplication(cl: ClassLoader?, name: String?, context: Context?): Application {
        return super.newApplication(cl, HiltTestApplication_Application::class.java.name, context)
    }
}