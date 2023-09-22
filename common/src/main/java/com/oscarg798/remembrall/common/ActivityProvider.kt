package com.oscarg798.remembrall.common

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton
import java.lang.ref.WeakReference

interface ActivityProvider : ActivityLifecycleCallbacks {

    fun provide(): Activity?
}

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

@Module
@InstallIn(SingletonComponent::class)
internal interface ActivityProviderBindings {

    @Binds
    @Singleton
    fun bindActivityProvider(impl: ActivityProviderImpl): ActivityProvider

    @Binds
    @Singleton
    fun bindActivityLifecycleCallback(impl: ActivityProvider): ActivityLifecycleCallbacks
}