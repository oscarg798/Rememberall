package com.oscarg798.remembrall.widget

import androidx.glance.GlanceId

interface RemembrallWidgetUpdater {

    suspend fun update(state: RemembrallWidgetState)

    suspend fun update(state: RemembrallWidgetState, ids: List<GlanceId>)
}