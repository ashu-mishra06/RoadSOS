package com.example.roadsos.utils

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log

object VibrationHelper {

    fun vibrateTick(context: Context) {

        try {

            val vibrator: Vibrator? =

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {

                    val manager =
                        context.getSystemService(
                            Context.VIBRATOR_MANAGER_SERVICE
                        ) as VibratorManager

                    manager.defaultVibrator

                } else {

                    @Suppress("DEPRECATION")
                    context.getSystemService(
                        Context.VIBRATOR_SERVICE
                    ) as Vibrator
                }

            if (vibrator == null) {

                Log.e(
                    "VIBRATION",
                    "Vibrator is null"
                )

                return
            }

            if (!vibrator.hasVibrator()) {

                Log.e(
                    "VIBRATION",
                    "Device has no vibrator"
                )

                return
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        180,
                        180
                    )
                )

            } else {

                @Suppress("DEPRECATION")
                vibrator.vibrate(180)
            }

            Log.d(
                "VIBRATION",
                "Vibration triggered"
            )

        } catch (e: Exception) {

            Log.e(
                "VIBRATION",
                "Vibration failed: ${e.message}"
            )
        }
    }
}