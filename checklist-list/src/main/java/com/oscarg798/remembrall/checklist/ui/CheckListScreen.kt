package com.oscarg798.remembrall.checklist.ui

import android.os.Bundle
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import com.oscarg798.remembrall.checklist.ChecklistViewModel
import com.oscarg798.remembrall.common_checklist.ui.AwesomeIconField
import com.oscarg798.remembrall.ui_common.navigation.LocalNavControllerProvider
import com.oscarg798.remembrall.ui_common.navigation.Router
import com.oscarg798.remembrall.ui_common.ui.AddButton
import com.oscarg798.remembrall.ui_common.ui.theming.RemembrallTheme

@Composable
fun CheckListScreen(backStackEntry: NavBackStackEntry) {

    val viewModel: ChecklistViewModel = hiltViewModel(backStackEntry)
    val state by viewModel.state.collectAsState(initial = ChecklistViewModel.ViewState())
    val events by viewModel.events.collectAsState(initial = null)
    val navController = LocalNavControllerProvider.current

    LaunchedEffect(key1 = events) {
        val event = events ?: return@LaunchedEffect

        if (event is ChecklistViewModel.Event.NavigateToAdd) {
            Router.AddChecklist.navigate(navController)
        } else if (event is ChecklistViewModel.Event.ShowDetail) {
            Router.ChecklistDetail.navigate(navController, Bundle().apply {
                putString(Router.ChecklistDetail.ChecklistIdArgument, event.checklistId)
            })
        }
    }

    LaunchedEffect(key1 = viewModel) {
        viewModel.fetchChecklists()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(RemembrallTheme.dimens.Medium)
    ) {

        if (state.loading) {
            LoadingScreen()
        } else if (state.checklists != null) {
            val checklists = state.checklists ?: return
            LazyColumn {
                items(checklists.toList(), key = { it.id }) { checklist ->
                    Card(
                        backgroundColor = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(RemembrallTheme.dimens.Medium),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = RemembrallTheme.dimens.Small)
                            .clickable { viewModel.onChecklistClicked(checklist) }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = RemembrallTheme.dimens.Small)
                        ) {
                            Text(
                                text = checklist.name.capitalize(Locale.current),
                                modifier = Modifier
                                    .padding(RemembrallTheme.dimens.Large)
                                    .weight(.8f),
                                style = MaterialTheme.typography.titleMedium.merge(
                                    TextStyle(MaterialTheme.colorScheme.onBackground)
                                )
                            )

                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .weight(.2f)
                                    .padding(horizontal = RemembrallTheme.dimens.Small)
                            ) {
                                AwesomeIconField(
                                    iconCode = checklist.icon,
                                    modifier = Modifier
                                        .padding(RemembrallTheme.dimens.Medium)
                                )
                            }
                        }
                    }
                }
            }
        }

        AddButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
        ) {
            viewModel.onAddRequested()
        }
    }
}