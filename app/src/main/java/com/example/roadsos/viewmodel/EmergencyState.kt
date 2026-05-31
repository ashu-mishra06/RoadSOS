package com.example.roadsos.viewmodel

data class EmergencyState(

    val isCrashDetected: Boolean = false,

    val countdown: Int = 10,

    val emergencyTriggered: Boolean = false
)