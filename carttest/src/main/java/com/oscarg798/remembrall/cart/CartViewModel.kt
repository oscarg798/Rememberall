package com.oscarg798.remembrall.cart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oscarg798.remembrall.cart.effecthandler.EffectHandler
import com.oscarg798.remembrall.cart.ui.PermissionChecker
import com.oscarg798.remembrall.cart.ui.TextComponentDecorator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

@HiltViewModel
internal class CartViewModel @Inject constructor(
    private val effectHandler: EffectHandler,
    private val permissionChecker: PermissionChecker,
    private val textComponentDecorator: TextComponentDecorator,
) : ViewModel() {

    private val _model =
        MutableStateFlow(Model(hasPermissions = permissionChecker.hasPermissions()))
    private val events = MutableSharedFlow<Event>()

    val model = _model.asStateFlow()
    private val mutex = Mutex()

    init {

        viewModelScope.launch {
            events.collect {
                update(it)
            }
        }
    }

    fun onEvent(event: Event) {
        viewModelScope.launch { events.emit(event) }
    }

    private suspend fun dispatchEffects(effects: Set<Effect>) {
        effectHandler.invoke(effects, ::onEvent)
    }

    private suspend fun update(event: Event) = withContext(Dispatchers.IO) {
        when (event) {
            is Event.OnTextChanged -> updateModel {
                it.copy(
                    currentText = event.text,
                    trailingIcon = textComponentDecorator.getTrailingIcon(text = event.text)
                )
            }

            is Event.OnFocusChanged -> Unit
            Event.OnTrailingIconClicked -> updateModel {
                if (model.value.trailingIcon == R.drawable.ic_clear) {
                    it.copy(
                        currentText = "",
                        trailingIcon = textComponentDecorator.getTrailingIcon(text = "")
                    )
                } else {
                    it
                }
            }

            is Event.MutatePermissions -> {
                permissionChecker.mutate(event.hasPermissions)
                updateModel {
                    it.copy(
                        hasPermissions = event.hasPermissions,
                        trailingIcon = textComponentDecorator.getTrailingIcon(text = it.currentText)
                    )
                }
            }
        }
    }

    private suspend fun updateModel(updater: (Model) -> Model) {
        mutex.withLock {
            _model.update { updater(it) }
        }
    }

}