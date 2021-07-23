package com.oscarg798.remembrall.taskdetails

import android.app.Activity
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.oscarg798.remembrall.R
import com.oscarg798.remembrall.common.extensions.requireArguments
import com.oscarg798.remembrall.common.navigation.Router
import com.oscarg798.remembrall.common.ui.RemembrallPage
import com.oscarg798.remembrall.common.ui.RemembrallScaffold
import com.oscarg798.remembrall.common.ui.RemembrallTopBar
import com.oscarg798.remembrall.common.ui.RemembrallTopBarTitle
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.components.ActivityComponent
import java.util.HashMap

fun NavGraphBuilder.taskDetailsScreen(viewModelStore: ViewModelStore) =
    composable(route = Router.TaskDetail.route, deepLinks = getDeepLinks()) { backStackEntry ->

        val taskId = backStackEntry.requireArguments()
            .getString(Router.TaskDetail.TaskIdArgument)!!
        TaskDetail(viewModelStore, taskId)
    }

@Composable
private fun TaskDetail(viewModelStore: ViewModelStore, taskId: String) {
    val context = LocalContext.current as Activity
    val viewModel: TaskDetailsViewModel = viewModelStore.get("${TaskDetailViewModelKey}_$taskId") {
        createViewModel(taskId, context)
    } as TaskDetailsViewModel

    val snackbarHostState = remember { SnackbarHostState() }

    val state by viewModel.state.collectAsState(TaskDetailsViewModel.ViewState())

    RemembrallScaffold(
        topBar = {
            RemembrallTopBar(
                title = {
                    RemembrallTopBarTitle(stringResource(R.string.profile_title))
                }
            )
        },
        snackbarHostState = snackbarHostState
    ) {
        RemembrallPage {
            if (state.loading) {
                Text("Loading")
            } else if (state.task != null) {
                Text(state.task!!.name)
            }
        }
    }
}

private fun getDeepLinks() = listOf(
    navDeepLink {
        uriPattern = Router.TaskDetail.uriPattern
    }
)

@EntryPoint
@InstallIn(ActivityComponent::class)
internal interface TaskDetailsViewModelEntryPoint {

    fun factory(): TaskDetailsViewModel.TaskDetailsViewModelFactory
}

class ViewModelStore {
    private val cachedViewModels = HashMap<String, ViewModel>()

    fun <T : ViewModel> get(key: String, create: () -> T) =
        cachedViewModels[key] ?: create.invoke().also {
            cachedViewModels[key] = it
        }
}

fun <T : ViewModel> ViewModelStore.viewModel(
    key: String,
    create: () -> T,
): T = get(key, create) as T

fun createViewModel(
    taskId: String,
    activity: Activity
): TaskDetailsViewModel {
    return EntryPointAccessors.fromActivity(
        activity,
        TaskDetailsViewModelEntryPoint::class.java
    ).factory().create(
        taskId = taskId
    )
}

private const val TaskDetailViewModelKey = "TaskDetailViewModelKey"
