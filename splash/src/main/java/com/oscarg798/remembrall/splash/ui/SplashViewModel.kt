package com.oscarg798.remembrall.splash.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oscarg798.remebrall.coroutinesutils.CoroutineContextProvider
import com.oscarg798.remembrall.mobiusutils.LoopInjector
import com.oscarg798.remembrall.mobiusutils.ViewModelConnection
import com.oscarg798.remembrall.splash.domain.Effect
import com.oscarg798.remembrall.splash.domain.Event
import com.oscarg798.remembrall.splash.domain.Model
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
internal class SplashViewModel @Inject constructor(
    loopInjector: LoopInjector<Model, Event, Effect>,
    uiEffectState: MutableSharedFlow<Effect.UIEffect>,
    coroutineContextProvider: CoroutineContextProvider
) : ViewModel(),
    Connectable<Model, Event>,
    CoroutineContextProvider by coroutineContextProvider {

    private val _model = MutableStateFlow(Model(true))
    private val events = MutableSharedFlow<Event>()
    private val controller =
        loopInjector.provide(_model.value) { First.first(it, setOf(Effect.CheckSessionState)) }

    val uiEffect: Flow<Effect.UIEffect> = uiEffectState.asSharedFlow()
    val model: StateFlow<Model> = _model.asStateFlow()

    init {
        controller.connect(this)
        controller.start()
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