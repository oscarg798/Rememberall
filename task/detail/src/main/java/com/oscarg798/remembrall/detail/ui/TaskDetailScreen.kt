package com.oscarg798.remembrall.detail.ui

import com.oscarg798.remembrall.ui.icons.R as IconsR
import android.app.Activity
import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.oscarg798.remembrall.actionbutton.ActionButton
import com.oscarg798.remembrall.actionrow.ActionRow
import com.oscarg798.remembrall.detail.R
import com.oscarg798.remembrall.detail.di.TaskDetailEntryPoint
import com.oscarg798.remembrall.detail.domain.DisplayableTask
import com.oscarg798.remembrall.detail.domain.Effect
import com.oscarg798.remembrall.detail.domain.Event
import com.oscarg798.remembrall.navigation.LocalNavigatorProvider
import com.oscarg798.remembrall.navigation.Page
import com.oscarg798.remembrall.navigation.Route
import com.oscarg798.remembrall.ui.components.toolbar.RemembrallToolbar
import com.oscarg798.remembrall.ui.dimensions.dimensions
import com.oscarg798.remembrall.ui.dimensions.typo
import com.oscarg798.remembrall.ui.extensions.requireArguments
import com.oscarg798.remembrall.ui.theming.RemembrallPage
import com.oscarg798.remembrall.ui.theming.RemembrallScaffold
import com.oscarg798.remembrall.uicolor.SecondaryTextColor
import com.oscarg798.remembrall.viewmodelutils.provide
import dagger.hilt.android.EntryPointAccessors

internal object TaskDetailPage : Page {

    override fun build(builder: NavGraphBuilder) {
        return builder.taskDetailScreen()
    }
}

private fun NavGraphBuilder.taskDetailScreen() = composable(Route.DETAIL.path, deepLinks = listOf(
    navDeepLink {
        uriPattern = Route.DETAIL.uriPattern.toString()
    }
)) { backstackEntry ->
    val activity = LocalContext.current as Activity
    val navigator = LocalNavigatorProvider.current
    val taskId = remember(backstackEntry) {
        backstackEntry.requireArguments()
            .getString(Route.TaskIdArgument)!!
    }

    val entryPoint = remember(taskId) {
        provideEntryPoint(activity)
    }

    val viewModel: TaskDetailViewModel = viewModel(
        viewModelStoreOwner = backstackEntry,
        factory = provide {
            entryPoint.viewModelFactory().create(taskId)
        }
    )

    val initialModel = remember(viewModel) { viewModel.model.value }
    val model by viewModel.model.collectAsStateWithLifecycle(initialValue = initialModel)
    val effects by viewModel.uiEffects.collectAsStateWithLifecycle(initialValue = null)
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = effects) {
        val effect = effects ?: return@LaunchedEffect

        when (effect) {
            is Effect.UIEffect.NavigateToEdit -> {
                navigator.navigate(
                    route = Route.ADD,
                    arguments = Bundle().apply {
                        putString(Route.TaskIdArgument, effect.taskId)
                    }
                )
            }

            is Effect.UIEffect.ShowError -> snackbarHostState.showSnackbar(
                activity.getString(R.string.error_marking_task_as_completed)
            )

            Effect.UIEffect.CloseScreen -> navigator.navigateBack()
        }
    }

    RemembrallScaffold(
        topBar = {
            RemembrallToolbar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = MaterialTheme.dimensions.Medium,
                        start = MaterialTheme.dimensions.Medium,
                        end = MaterialTheme.dimensions.Medium
                    ),
                actions = {
                    ActionButton(
                        icon = IconsR.drawable.ic_close,
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = CircleShape
                            )
                            .clip(CircleShape)
                    ) {
                        viewModel.onEvent(Event.OnBackButtonClicked)
                    }
                }
            ) {
                viewModel.onEvent(Event.OnBackButtonClicked)
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        RemembrallPage(modifier = Modifier.padding(paddingValues)) {
            model.task?.let { task ->
                TaskDetailScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            vertical = MaterialTheme.dimensions.Medium,
                            horizontal = MaterialTheme.dimensions.Large
                        ),
                    task = task,
                    loading = model.loading,
                    onEvent = viewModel::onEvent
                )
            }
        }
    }
}


@Composable
private fun TaskDetailScreen(
    loading: Boolean,
    modifier: Modifier,
    task: DisplayableTask, onEvent: (Event) -> Unit
) {
    ConstraintLayout(modifier) {
        val (detailField, actionRow) = createRefs()
        TaskDetail(
            task = task,
            modifier = Modifier
                .constrainAs(detailField) {
                    linkTo(parent.start, parent.end)
                    linkTo(parent.top, actionRow.top, bottomMargin = 8.dp)
                    height = Dimension.fillToConstraints
                },
        )

        ActionRow(
            modifier = Modifier
                .constrainAs(actionRow) {
                    linkTo(parent.start, parent.end)
                    width = Dimension.fillToConstraints
                    bottom.linkTo(parent.bottom)
                }
        ) {
            if (loading) {
                Box(Modifier.padding(MaterialTheme.dimensions.ExtraSmall)) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            } else {
                ActionButton(
                    icon = IconsR.drawable.ic_edit,
                    onClick = { onEvent(Event.OnEditActionClicked) },
                )

                ActionButton(
                    icon = IconsR.drawable.ic_delete,
                    onClick = { onEvent(Event.OnDeleteButtonClicked) },
                )
            }
        }
    }
}


@Composable
private fun TaskDetail(
    task: DisplayableTask,
    modifier: Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.Medium)
    ) {

        Text(
            text = task.title,
            style = MaterialTheme.typo.h4.copy(
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold
            ),
        )

        task.dueDate?.let { dueDate ->
            Text(
                text = dueDate,
                style = MaterialTheme.typo.caption.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontStyle = FontStyle.Italic,
                    color = SecondaryTextColor
                ),
                textAlign = TextAlign.Start,
            )
        }

        if (task.attendees.isNotEmpty()) {
            val attendeesString = buildAnnotatedString {
                withStyle(
                    SpanStyle(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = MaterialTheme.typo.body2.fontSize
                    )
                ) {
                    append("On this task : ")
                }
                withStyle(
                    SpanStyle(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontStyle = FontStyle.Italic,
                        fontSize = MaterialTheme.typo.body2.fontSize
                    )
                ) {
                    append(task.attendees.joinToString(", "))
                }
            }
            Text(
                text = attendeesString,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typo.caption.copy(
                    color = MaterialTheme.colorScheme.onSurface
                ),
            )
        }

        task.description?.let { description ->
            Text(
                text = description,
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                style = MaterialTheme.typo.body1.copy(
                    color = MaterialTheme.colorScheme.onSurface
                ),
            )
        }
    }
}

private fun provideEntryPoint(
    activity: Activity
) = EntryPointAccessors.fromActivity(
    activity,
    TaskDetailEntryPoint::class.java
)

@Composable
@Preview(device = Devices.NEXUS_5)
private fun TaskDetailPreview() {
    com.oscarg798.remembrall.ui.theming.RemembrallTheme {
        RemembrallScaffold {
            TaskDetailScreen(
                modifier = Modifier
                    .background(Color.Red)
                    .fillMaxSize()
                    .padding(it)
                    .padding(MaterialTheme.dimensions.Medium),
                task = task,
                loading = false
            ) {}
        }
    }
}

private val task = DisplayableTask(
    id = "1",
    owned = true,
    title = generateString(3),
    attendees = setOf(generateString()),
    description = generateString(3_000),
    dueDate = "Monday 29 Sep, 23"
)

private fun generateString(wordLength: Int = 3) =
    LoremIpsum(wordLength).values.shuffled().joinToString(" ")

