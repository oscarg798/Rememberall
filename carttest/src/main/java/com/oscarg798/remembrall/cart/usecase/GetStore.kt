package com.oscarg798.remembrall.cart.usecase

import com.oscarg798.remembrall.cart.Store
import javax.inject.Inject
import kotlinx.coroutines.delay

internal interface GetStore: suspend ()-> Store

internal class GetStoreImpl @Inject constructor(): GetStore {

    override suspend fun invoke(): Store {
        delay(300)
        return Store("Big Boobs", "Spain")
    }
}