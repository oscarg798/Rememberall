package com.oscarg798.remembrall

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.oscarg798.remembrall.auth.Session
import com.oscarg798.remembrall.widget.RemembrallWidgetState
import com.oscarg798.remembrall.widget.RemembrallWidgetUpdater
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltAndroidApp
class RemembrallApplication : Application(), Configuration.Provider {

    @Inject
    internal lateinit var workerFactory: HiltWorkerFactory

    @Inject
    internal lateinit var activityCallbacks: ActivityLifecycleCallbacks

    @Inject
    internal lateinit var session: Session

    @Inject
    internal lateinit var widgetUpdater: RemembrallWidgetUpdater

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(activityCallbacks)
        keepWidgetStateSynced()
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    private fun keepWidgetStateSynced() {
    }
}
