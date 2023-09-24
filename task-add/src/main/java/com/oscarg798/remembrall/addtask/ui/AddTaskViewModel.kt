package com.oscarg798.remembrall.addtask.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oscarg798.remebrall.coroutinesutils.CoroutineContextProvider
import com.oscarg798.remembrall.addtask.domain.Effect
import com.oscarg798.remembrall.addtask.domain.Event
import com.oscarg798.remembrall.addtask.domain.Model
import com.oscarg798.remembrall.mobiusutils.LoopInjector
import com.oscarg798.remembrall.mobiusutils.ViewModelConnection
import com.spotify.mobius.Connectable
import com.spotify.mobius.Connection
import com.spotify.mobius.First
import com.spotify.mobius.functions.Consumer
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltViewModel
internal class AddTaskViewModel @Inject constructor(
    loopInjector: LoopInjector<Model, Event, Effect>,
    uiEffectState: MutableSharedFlow<Effect.UIEffect>,
    coroutineContextProvider: CoroutineContextProvider,
) : ViewModel(),
    Connectable<Model, Event>,
    CoroutineContextProvider by coroutineContextProvider {

    private val _model = MutableStateFlow(Model())
    val model: StateFlow<Model> = _model.asStateFlow()
    val uiEffect: Flow<Effect.UIEffect> = uiEffectState.asSharedFlow()

    private val events = MutableSharedFlow<Event>()
    private val controller = loopInjector.provide(_model.value) {
        First.first(
            it,
            setOf(Effect.GetAvailableTaskPriorities(it.priority))
        )
    }

    init {
        controller.connect(this)
        controller.start()
    }

    fun onEvent(event: Event) {
        viewModelScope.launch { events.emit(event) }
    }

    override fun connect(output: Consumer<Event>): Connection<Model> {
        viewModelScope.launch { events.collectLatest { output.accept(it) } }
        return object : ViewModelConnection<Model>(_model) {}
    }

    override fun onCleared() {
        controller.stop()
        super.onCleared()
    }
}