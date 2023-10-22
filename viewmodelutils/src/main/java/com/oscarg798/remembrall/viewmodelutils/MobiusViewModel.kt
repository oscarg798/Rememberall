package com.oscarg798.remembrall.viewmodelutils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oscarg798.remebrall.coroutinesutils.CoroutineContextProvider
import com.oscarg798.remembrall.mobiusutils.LoopInjector
import com.oscarg798.remembrall.mobiusutils.ViewModelConnection
import com.spotify.mobius.Connectable
import com.spotify.mobius.Connection
import com.spotify.mobius.Init
import com.spotify.mobius.functions.Consumer
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

//TODO: save state handling
abstract class MobiusViewModel<Model, Event, Effect> constructor(
    initialModel: Model,
    init: Init<Model, Effect>,
    loopInjector: LoopInjector<Model, Event, Effect>,
    coroutineContextProvider: CoroutineContextProvider,
) : ViewModel(),
    Connectable<Model, Event>,
    CoroutineContextProvider by coroutineContextProvider {

    private val _model = MutableStateFlow(initialModel)
    val model: StateFlow<Model> = _model.asStateFlow()

    private val events = MutableSharedFlow<Event>()

    protected val controller = loopInjector.provide(_model.value, init)

    init {
        controller.connect(this)
        controller.start()
    }

    fun onEvent(event: Event) {
        viewModelScope.launch { events.emit(event) }
    }

    override fun connect(output: Consumer<Event>): Connection<Model> {
        viewModelScope.launch { events.collectLatest { event -> output.accept(event) } }
        return object : ViewModelConnection<Model>(_model) {}
    }

    public override fun onCleared() {
        controller.stop()
        super.onCleared()
    }
}