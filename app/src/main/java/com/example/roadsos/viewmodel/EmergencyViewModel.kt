package com.example.roadsos.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.roadsos.utils.EmergencyManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EmergencyViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val _state =
        MutableStateFlow(
            EmergencyState()
        )

    val state: StateFlow<EmergencyState> =
        _state

    private var countdownJob: Job? = null

    fun triggerCrash() {

        if (
            _state.value.isCrashDetected
        ) return

        countdownJob?.cancel()

        _state.value =
            EmergencyState(
                isCrashDetected = true,
                countdown = 10,
                emergencyTriggered = false
            )

        countdownJob =
            viewModelScope.launch {

                for (i in 10 downTo 1) {

                    _state.value =
                        _state.value.copy(
                            countdown = i
                        )

                    delay(1000)

                    if (
                        !_state.value.isCrashDetected
                    ) {
                        return@launch
                    }
                }

                triggerEmergency()
            }
    }

    fun cancelEmergency() {

        countdownJob?.cancel()

        _state.value =
            EmergencyState(
                isCrashDetected = false,
                countdown = 10,
                emergencyTriggered = false
            )
    }

    fun triggerEmergency() {

        countdownJob?.cancel()

        EmergencyManager(
            getApplication()
        ).triggerEmergency()

        _state.value =
            EmergencyState(
                isCrashDetected = false,
                countdown = 10,
                emergencyTriggered = true
            )
    }

    fun resetEmergencyState() {

        countdownJob?.cancel()

        _state.value =
            EmergencyState()
    }
}