package com.example.roadsos.sensors

import kotlin.math.sqrt

class CrashDetector {

    companion object {

        private const val CRASH_THRESHOLD = 25.0
    }

    fun processSensorData(

        x: Float,
        y: Float,
        z: Float

    ): Boolean {

        val magnitude = sqrt(

            (x * x + y * y + z * z).toDouble()

        )

        return magnitude > CRASH_THRESHOLD
    }
}