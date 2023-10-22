package com.oscarg798.remembrall.widgetimpl

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import com.oscarg798.remebrall.coroutinesutils.CoroutineContextProvider
import com.oscarg798.remembrall.widget.RemembrallWidgetState
import com.oscarg798.remembrall.widget.RemembrallWidgetUpdater
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.withContext

internal class RemembrallWidgetUpdaterImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val coroutinesContextProvider: CoroutineContextProvider,
) : RemembrallWidgetUpdater, CoroutineContextProvider by coroutinesContextProvider {


    override suspend fun update(state: RemembrallWidgetState, ids: List<GlanceId>) {
        ids.forEach { glanceId ->
            dispatchState(state, glanceId)
        }
    }

    override suspend fun update(state: RemembrallWidgetState) {
        GlanceAppWidgetManager(context).getGlanceIds(RemembrallWidget::class.java)
            .forEach { glanceId ->
                dispatchState(state, glanceId)
            }
    }

    private suspend fun dispatchState(state: RemembrallWidgetState, glanceId: GlanceId) =
        withContext(io) {
            updateAppWidgetState(context, glanceId) { mutablePreferences ->
                mutablePreferences[RemembrallWidgetPreferencesKeys.StateKey] = state.name
            }
        }
}