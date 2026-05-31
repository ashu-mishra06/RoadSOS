package com.example.roadsos.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.telephony.SmsManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat

object SMSHelper {

    fun sendEmergencySMS(
        context: Context,
        phoneNumber: String,
        message: String
    ): Boolean {
        if (phoneNumber.isBlank()) {

            Log.e(
                "SMS_STATUS",
                "Emergency phone number is empty"
            )

            return false
        }

        if (
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.SEND_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            Log.e(
                "SMS_STATUS",
                "SEND_SMS permission denied. Automatic SMS not sent."
            )

            Toast.makeText(
                context,
                "SMS permission denied. Emergency SMS not sent.",
                Toast.LENGTH_LONG
            ).show()

            return false
        }

        return try {

            val smsManager =
                SmsManager.getDefault()

            val messageParts =
                smsManager.divideMessage(message)

            smsManager.sendMultipartTextMessage(
                phoneNumber,
                null,
                messageParts,
                null,
                null
            )

            Log.d(
                "SMS_STATUS",
                "Automatic emergency SMS sent successfully to $phoneNumber"
            )

            true

        } catch (e: Exception) {

            Log.e(
                "SMS_STATUS",
                "Automatic SMS failed: ${e.message}"
            )

            false
        }
    }
}