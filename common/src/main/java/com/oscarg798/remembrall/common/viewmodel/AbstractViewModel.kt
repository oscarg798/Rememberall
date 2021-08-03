package com.oscarg798.remembrall.common.viewmodel

import androidx.lifecycle.ViewModel
import com.oscarg798.remembrall.common.coroutines.CoroutineContextProvider
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

abstract class AbstractViewModel<ViewState, Event>(
    private val initialState: ViewState,
    protected val coroutineContextProvider: CoroutineContextProvider
) : ViewModel() {

    protected val _event = MutableSharedFlow<Event>(
        extraBufferCapacity = 1
    )

    private val _state = MutableSharedFlow<ViewState>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private val mutex: Mutex = Mutex()

    val state: Flow<ViewState>
        get() = _state

    val events: Flow<Event>
        get() = _event

    protected suspend fun update(reducer: (ViewState) -> ViewState) {
        mutex.withLock {
            _state.emit(
                reducer(currentState())
            )
        }
    }

    protected fun currentState() = _state.replayCache.firstOrNull() ?: initialState
}
