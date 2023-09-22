package com.oscarg798.remembrall.login.effecthandler

internal interface EffectTransformer<Effect, Event> : suspend (Effect) -> Event