package com.oscarg798.remembrall.activityprovider

import android.app.Activity
import android.app.Application

interface ActivityProvider : Application.ActivityLifecycleCallbacks {

    fun provide(): Activity?
}
