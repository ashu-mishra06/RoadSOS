package com.example.roadsos.utils

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class EmergencyActionStatus(
    val smsStatus: String = "Emergency SMS not started",
    val callStatus: String = "Emergency call not started",
    val smsSentCount: Int = 0,
    val smsFailedCount: Int = 0,
    val callAttempted: Boolean = false
)

object EmergencyActionStatusManager {

    private val _status =
        MutableStateFlow(
            EmergencyActionStatus()
        )

    val status =
        _status.asStateFlow()

    fun reset() {
        _status.value =
            EmergencyActionStatus()
    }

    fun updateSmsStatus(
        message: String,
        sentCount: Int = _status.value.smsSentCount,
        failedCount: Int = _status.value.smsFailedCount
    ) {
        _status.value =
            _status.value.copy(
                smsStatus = message,
                smsSentCount = sentCount,
                smsFailedCount = failedCount
            )
    }

    fun updateCallStatus(
        message: String,
        attempted: Boolean = _status.value.callAttempted
    ) {
        _status.value =
            _status.value.copy(
                callStatus = message,
                callAttempted = attempted
            )
    }
}