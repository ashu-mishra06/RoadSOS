package com.example.roadsos.utils

import android.content.Context
import android.widget.Toast

object EmergencyAlertManager {

    fun sendEmergencySmsToSavedContacts(
        context: Context,
        userName: String,
        bloodGroup: String,
        emergencyContacts: List<String>,
        latitude: Double?,
        longitude: Double?
    ) {
        val cleanContacts =
            emergencyContacts
                .map { it.trim() }
                .filter { it.isNotBlank() }
                .distinct()

        if (cleanContacts.isEmpty()) {

            EmergencyActionStatusManager.updateSmsStatus(
                message = "No emergency contacts saved",
                sentCount = 0,
                failedCount = 0
            )

            Toast.makeText(
                context,
                "No emergency contact saved.",
                Toast.LENGTH_LONG
            ).show()

            return
        }

        EmergencyActionStatusManager.updateSmsStatus(
            message = "Sending emergency SMS...",
            sentCount = 0,
            failedCount = 0
        )

        val emergencyMessage =
            buildEmergencyMessage(
                userName = userName,
                bloodGroup = bloodGroup,
                latitude = latitude,
                longitude = longitude
            )

        var successCount =
            0

        var failedCount =
            0

        cleanContacts.forEach { contact ->

            val sent =
                SMSHelper.sendEmergencySMS(
                    context = context,
                    phoneNumber = contact,
                    message = emergencyMessage
                )

            if (sent) {
                successCount++
            } else {
                failedCount++
            }
        }

        if (successCount > 0) {

            EmergencyActionStatusManager.updateSmsStatus(
                message = "Emergency SMS sent to $successCount contact(s)",
                sentCount = successCount,
                failedCount = failedCount
            )

            Toast.makeText(
                context,
                "Emergency SMS sent to $successCount contact(s).",
                Toast.LENGTH_SHORT
            ).show()

        } else {

            EmergencyActionStatusManager.updateSmsStatus(
                message = "Emergency SMS failed",
                sentCount = successCount,
                failedCount = failedCount
            )

            Toast.makeText(
                context,
                "Emergency SMS could not be sent.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun buildEmergencyMessage(
        userName: String,
        bloodGroup: String,
        latitude: Double?,
        longitude: Double?
    ): String {

        val locationText =
            if (latitude != null && longitude != null) {
                "Location: https://maps.google.com/?q=$latitude,$longitude"
            } else {
                "Location: Not available"
            }

        return """
            RoadSOS EMERGENCY ALERT
            
            Possible accident detected.
            
            Name: ${userName.ifBlank { "Unknown" }}
            Blood Group: ${bloodGroup.ifBlank { "Unknown" }}
            
            $locationText
            
            Please check immediately and contact emergency services if needed.
        """.trimIndent()
    }
}