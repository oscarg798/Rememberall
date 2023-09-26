package com.oscarg798.remembrall.activityproviderimpl

import android.app.Activity
import android.os.Bundle
import com.oscarg798.remembrall.activityprovider.ActivityProvider
import java.lang.ref.WeakReference
import javax.inject.Inject

internal class ActivityProviderImpl @Inject constructor() : ActivityProvider {
    private var currentActivity: WeakReference<Activity> = WeakReference(null)

    override fun provide(): Activity? = currentActivity.get()

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        currentActivity = WeakReference(activity)
    }

    override fun onActivityStarted(activity: Activity) = Unit

    override fun onActivityResumed(activity: Activity) = Unit

    override fun onActivityPaused(activity: Activity) = Unit

    override fun onActivityStopped(activity: Activity) = Unit

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit

    override fun onActivityDestroyed(activity: Activity) {
        currentActivity.clear()
    }
}
