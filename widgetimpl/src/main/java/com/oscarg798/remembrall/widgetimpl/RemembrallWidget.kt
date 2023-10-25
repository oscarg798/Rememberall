package com.oscarg798.remembrall.widgetimpl

import android.content.Context
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.LocalContext
import androidx.glance.appwidget.CircularProgressIndicator
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.itemsIndexed
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
import androidx.glance.layout.padding
import androidx.glance.text.FontStyle
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import com.oscarg798.remembrall.navigation.Route
import com.oscarg798.remembrall.widget.RemembrallWidgetState
import com.oscarg798.remembrall.widgetimpl.navigation.WidgetNavigator
import com.oscarg798.remembrall.widgetimpl.di.WidgetEntryPoint
import com.oscarg798.remembrall.widgetimpl.domain.Effect
import com.oscarg798.remembrall.widgetimpl.domain.Event
import com.oscarg798.remembrall.widgetimpl.domain.TaskWindow
import dagger.hilt.android.EntryPointAccessors

internal object RemembrallWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val appContext = context.applicationContext ?: error("We need a context")
        provideContent {

            val state = currentState(key = RemembrallWidgetPreferencesKeys.StateKey)?.let {
                RemembrallWidgetState.valueOf(it)
            }


            val entryPoint = remember {
                EntryPointAccessors.fromApplication(
                    appContext, WidgetEntryPoint::class.java
                )
            }

            val navigator = remember(entryPoint) { entryPoint.widgetNavigator() }
            val viewModel = remember(entryPoint) { entryPoint.viewModel() }

            LaunchedEffect(key1 = state) {
                if (state == null) return@LaunchedEffect
                if (state == RemembrallWidgetState.Removed) {
                    viewModel.onCleared()
                    return@LaunchedEffect
                }

                viewModel.onEvent(Event.OnStateChanged(state))
            }

            val model by viewModel.model.collectAsState()
            val effects by viewModel.uiEffects.collectAsState(null)

            LaunchedEffect(effects) {
                val effect = effects ?: return@LaunchedEffect

                when (effect) {
                    is Effect.UIEffect.NavigateToDetail -> navigator.navigate(
                        route = Route.DETAIL,
                        arguments = Bundle().apply {
                            putString(Route.TaskIdArgument, effect.taskId)
                        }
                    )
                    Effect.UIEffect.NavigateToLogin -> navigator.navigate(Route.LOGIN)
                }
            }

            if (model.tasks == null) {
                Box(
                    modifier = GlanceModifier
                        .background(GlanceTheme.colors.background)
                        .padding(16.dp)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        GlanceModifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator()
                        Text(
                            text = if (model.sessionState == null) {
                                context.getString(R.string.getting_session_state_placeholder)
                            } else {
                                context.getString(R.string.getting_agenda_placeholder)
                            }, style = TextStyle(
                                color = GlanceTheme.colors.onBackground,
                                fontStyle = FontStyle.Italic
                            )
                        )
                    }
                }

            } else if (model.tasks!!.isNotEmpty()) {
                LazyColumn(
                    modifier = GlanceModifier
                        .fillMaxSize()
                        .background(GlanceTheme.colors.background)
                        .padding(16.dp),
                ) {
                    item(itemId = 123L) {
                        model.taskWindow?.let { taskWindow ->
                            WidgetHeader(taskWindow, GlanceModifier.fillMaxSize())
                        }
                    }
                    item(itemId = 998) {
                        Spacer(GlanceModifier.height(8.dp))
                    }
                    itemsIndexed(model.tasks!!, itemId = { index, _ ->
                        index.toLong()
                    }) { _, item ->
                        Column(modifier = GlanceModifier.fillMaxWidth()) {
                            TaskItem(
                                title = item.title,
                                description = item.description,
                                dueDate = item.dueDate,
                                modifier = GlanceModifier.fillMaxWidth()
                                    .padding(top = 4.dp, bottom = 4.dp)
                            ) {
                                viewModel.onEvent(Event.OnTaskClicked(item.id))
                            }
                            Spacer(GlanceModifier.height(8.dp))
                        }
                    }
                    item(itemId = 999) {
                        Spacer(GlanceModifier.height(8.dp))
                    }
                    item(itemId = 124L) {
                        Button(
                            text = context.getString(R.string.btn_refresh_text),
                            modifier = GlanceModifier.fillMaxWidth(),
                            onClick = {
                                viewModel.onEvent(Event.OnRefreshClicked)
                            }
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = GlanceModifier
                        .fillMaxSize()
                        .background(GlanceTheme.colors.background)
                        .padding(16.dp),
                ) {
                    model.taskWindow?.let { taskWindow ->
                        item(itemId = 123L) {
                            model.taskWindow?.let { taskWindow ->
                                WidgetHeader(taskWindow, GlanceModifier.fillMaxSize())
                            }
                        }
                    }

                    item(itemId = 998) {
                        Spacer(GlanceModifier.height(8.dp))
                    }

                    item {
                        Text(text = context.getString(R.string.empty_state_message))
                    }

                    item(itemId = 999) {
                        Spacer(GlanceModifier.height(8.dp))
                    }

                    item {
                        Button(
                            text = context.getString(R.string.btn_refresh_text),
                            modifier = GlanceModifier.fillMaxWidth(),
                            onClick = {
                                viewModel.onEvent(Event.OnRefreshClicked)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun WidgetHeader(
    taskWindow: TaskWindow,
    modifier: GlanceModifier
) {
    val context = LocalContext.current

    Column(modifier) {
        Text(
            context.getString(R.string.widget_title),
            style = TextStyle(
                color = GlanceTheme.colors.onBackground,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
            )
        )

        Text(
            context.getString(
                R.string.widget_subtitle,
                taskWindow.startFormatted,
                taskWindow.endFormatted
            ),
            style = TextStyle(
                color = GlanceTheme.colors.onBackground,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
            )
        )

    }
}
