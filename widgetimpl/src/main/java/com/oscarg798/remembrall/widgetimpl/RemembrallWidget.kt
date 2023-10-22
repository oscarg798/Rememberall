package com.oscarg798.remembrall.widgetimpl

import android.content.Context
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.CircularProgressIndicator
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.text.Text
import com.oscarg798.remembrall.widget.RemembrallWidgetState
import com.oscarg798.remembrall.widgetimpl.di.WidgetEntryPoint
import com.oscarg798.remembrall.widgetimpl.domain.Event
import dagger.hilt.android.EntryPointAccessors

internal object RemembrallWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val appContext = context.applicationContext ?: error("We need a context")
        provideContent {

            val state = currentState(key = RemembrallWidgetPreferencesKeys.StateKey)?.let {
                RemembrallWidgetState.valueOf(it)
            }

            val viewModel = remember {
                EntryPointAccessors.fromApplication(
                    appContext, WidgetEntryPoint::class.java
                ).viewModel()
            }

            LaunchedEffect(key1 = state) {
                if (state == null) return@LaunchedEffect
                if (state == RemembrallWidgetState.Removed) {
                    viewModel.onCleared()
                    return@LaunchedEffect
                }

                viewModel.onEvent(Event.OnStateChanged(state))
            }

            val model by viewModel.model.collectAsState()

            Column(GlanceModifier.fillMaxSize()) {
                if (model.sessionState == null ||
                    model.isUserLoggedIn() &&
                    model.tasks == null
                ) {
                    Box(GlanceModifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else if (!model.isUserLoggedIn()) {
                    Button(text = "Log In", onClick = {})
                } else if (model.tasks != null) {
                    Column(
                        modifier = GlanceModifier
                            .fillMaxSize()
                            .background(GlanceTheme.colors.background),
                    ) {
                        model.tasks?.forEach {
                            TaskItem(
                                title = it.title,
                                owned = it.owned,
                                description = it.description,
                                dueDate = it.dueDate,
                                modifier = GlanceModifier.fillMaxWidth(),
                                onOptionsClicked = { /*TODO*/ }) {
                            }
                            Spacer(GlanceModifier.height(8.dp))
                        }
                    }
                } else {
                    Text(text = "Nothing here ")
                }
            }
        }
    }
}
