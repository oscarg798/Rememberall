package com.oscarg798.remembrall.mobiusutils

import com.spotify.mobius.Connection
import kotlinx.coroutines.flow.MutableStateFlow

abstract class ViewModelConnection<Model>(private val modelFlow: MutableStateFlow<Model>) :
    Connection<Model> {

    override fun dispose() {
        // do nothing view models clean their scopes
    }

    override fun accept(value: Model) {
        modelFlow.value = value
    }
}
