package com.oscarg798.remembrall.login

import android.app.Application
import com.google.firebase.FirebaseApp

open class CustomApplication : Application() {

    override fun onCreate() {
        FirebaseApp.initializeApp(this)
        super.onCreate()
    }
}
