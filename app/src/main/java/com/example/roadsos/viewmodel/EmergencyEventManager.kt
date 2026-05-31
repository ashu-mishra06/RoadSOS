package com.example.roadsos.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object EmergencyEventManager {

    private val _crashEventPending =
        MutableStateFlow(false)

    val crashEventPending =
        _crashEventPending.asStateFlow()

    fun triggerCrashEvent() {
        _crashEventPending.value = true
    }

    fun markCrashEventHandled() {
        _crashEventPending.value = false
    }
}