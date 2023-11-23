package com.oscarg798.remembrall.cart.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextField
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.oscarg798.remembrall.cart.CartViewModel
import com.oscarg798.remembrall.cart.Event
import com.oscarg798.remembrall.navigation.Page
import com.oscarg798.remembrall.navigation.Route
import com.oscarg798.remembrall.ui.theming.RemembrallPage
import com.oscarg798.remembrall.ui.theming.RemembrallScaffold
import javax.inject.Inject


internal class CartPage @Inject constructor() : Page {
    override fun build(builder: NavGraphBuilder) {
        builder.CartScreen()
    }
}

private fun NavGraphBuilder.CartScreen() = composable(
    Route.CART.path, deepLinks = listOf(
        navDeepLink {
            uriPattern = Route.LOGIN.uriPattern.toString()
        })
) {
    val viewModel: CartViewModel = hiltViewModel(it)
    val state by viewModel.model.collectAsState()


    RemembrallScaffold {
        RemembrallPage(
            Modifier
                .padding(it)
                .fillMaxSize()
        ) {

            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {

                TextField(value = "", onValueChange = {})
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Permissions")
                    Checkbox(checked = state.hasPermissions, onCheckedChange = { checked ->
                        viewModel.onEvent(Event.MutatePermissions(checked))
                    })
                }

                TextComponent(
                    currentText = state.currentText,
                    options = state.options,
                    trailingIcon = state.trailingIcon,
                    placeholder = "Tu Ubicacion",
                    onEvent = viewModel::onEvent,
                )

                Text(text = "texto en viewModel ${state.currentText}")
            }
        }
    }
}

@Composable
private fun TextComponent(
    currentText: String,
    placeholder: String,
    @DrawableRes trailingIcon: Int?,
    options: List<String>? = null,
    onEvent: (Event) -> Unit
) {
    var hasFocus by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = hasFocus) {
        onEvent(Event.OnFocusChanged(hasFocus))
    }

    OutlinedTextField(
        value = if (hasFocus || currentText.isNotEmpty()) currentText else placeholder,
        onValueChange = {
            onEvent(Event.OnTextChanged(it))
        },
        modifier = Modifier.onFocusChanged {
            hasFocus = it.hasFocus
        },
        label = {
            Text(text = "Codigo o direccion")
        },
        trailingIcon = {
            if (trailingIcon != null) {
                IconButton(onClick = {
                    onEvent(Event.OnTrailingIconClicked)
                }) {
                    Icon(painter = painterResource(id = trailingIcon), contentDescription = "")
                }
            }
        })
}