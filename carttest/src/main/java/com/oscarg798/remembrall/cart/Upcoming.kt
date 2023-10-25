package com.oscarg798.remembrall.cart

internal sealed interface Upcoming {

    object NoChange: Upcoming
    data class Next(val model: Model, val effects: Set<Effect> = emptySet()) : Upcoming
    data class Effects(val effects: Set<Effect>) : Upcoming
}