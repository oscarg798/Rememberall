package com.oscarg798.remembrall.widgetimpl

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.oscarg798.remembrall.widget.RemembrallWidgetState
import com.oscarg798.remembrall.widgetimpl.di.WidgetEntryPoint
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RemembrallReceiver : GlanceAppWidgetReceiver() {

    override val glanceAppWidget: GlanceAppWidget = RemembrallWidget

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        super.onDeleted(context, appWidgetIds)

        val widgetUpdater = EntryPointAccessors.fromApplication(
            context.applicationContext,
            WidgetEntryPoint::class.java
        ).widgetUpdater()

        GlobalScope.launch {
            widgetUpdater.update(RemembrallWidgetState.Removed)
        }
    }

}