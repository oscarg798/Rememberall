package com.oscarg798.remembrall.widgetimpl.usecase

import com.oscarg798.remembrall.widget.RemembrallWidgetState
import com.oscarg798.remembrall.widget.RemembrallWidgetUpdater
import com.oscarg798.remembrall.widgetimpl.domain.Effect
import javax.inject.Inject

internal interface ForceWidgetRefresh : suspend (Effect.ForceWidgetUpdate) -> Unit

internal class ForceWidgetRefreshImpl @Inject constructor(
    private val remembrallWidgetUpdater: RemembrallWidgetUpdater,
) : ForceWidgetRefresh {

    override suspend fun invoke(effect: Effect.ForceWidgetUpdate) {
        remembrallWidgetUpdater.update(RemembrallWidgetState.DataRefreshRequired)
    }
}