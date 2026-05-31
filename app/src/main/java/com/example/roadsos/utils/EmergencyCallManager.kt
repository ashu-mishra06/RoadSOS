package com.example.roadsos.utils

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat

object EmergencyCallManager {

    private const val UNIFIED_EMERGENCY_NUMBER =
        "112"

    fun callEmergencyServiceIfEnabled(
        context: Context,
        isAutoCallEnabled: Boolean
    ) {
        if (!isAutoCallEnabled) {

            EmergencyActionStatusManager.updateCallStatus(
                message = "Auto emergency call is OFF",
                attempted = false
            )

            Log.d(
                "CALL_STATUS",
                "Auto emergency call disabled by user."
            )

            return
        }

        EmergencyActionStatusManager.updateCallStatus(
            message = "Starting emergency call...",
            attempted = true
        )

        val hasCallPermission =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CALL_PHONE
            ) == PackageManager.PERMISSION_GRANTED

        if (!hasCallPermission) {

            EmergencyActionStatusManager.updateCallStatus(
                message = "Call permission missing",
                attempted = true
            )

            Toast.makeText(
                context,
                "Call permission missing. Emergency call not started.",
                Toast.LENGTH_LONG
            ).show()

            Log.e(
                "CALL_STATUS",
                "CALL_PHONE permission missing."
            )

            return
        }

        try {

            val intent =
                Intent(
                    Intent.ACTION_CALL,
                    Uri.parse("tel:$UNIFIED_EMERGENCY_NUMBER")
                )

            intent.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
            )

            context.startActivity(intent)

            EmergencyActionStatusManager.updateCallStatus(
                message = "Emergency call started to $UNIFIED_EMERGENCY_NUMBER",
                attempted = true
            )

            Log.d(
                "CALL_STATUS",
                "Emergency call started to $UNIFIED_EMERGENCY_NUMBER"
            )

        } catch (e: Exception) {

            EmergencyActionStatusManager.updateCallStatus(
                message = "Emergency call failed",
                attempted = true
            )

            Log.e(
                "CALL_STATUS",
                "Emergency call failed: ${e.message}"
            )

            Toast.makeText(
                context,
                "Emergency call could not be started.",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}