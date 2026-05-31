package com.example.roadsos.utils

import android.content.Context
import android.util.Log
import com.example.roadsos.data.local.DatabaseProvider
import com.example.roadsos.data.repository.EmergencyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EmergencyManager(
    private val context: Context
) {

    fun triggerEmergency() {

        CoroutineScope(Dispatchers.IO).launch {

            try {

                val locationHelper =
                    LocationHelper(context)

                val location =
                    locationHelper.getCurrentLocation()

                val latitude =
                    location?.first ?: 0.0

                val longitude =
                    location?.second ?: 0.0

                val database =
                    DatabaseProvider.getDatabase(context)

                val repository =
                    EmergencyRepository(
                        database.hospitalDao()
                    )

                val traumaL1 =
                    repository.getNearestServices(
                        category = "trauma_l1",
                        userLat = latitude,
                        userLon = longitude
                    )

                val traumaL2 =
                    repository.getNearestServices(
                        category = "trauma_l2",
                        userLat = latitude,
                        userLon = longitude
                    )

                val districtEmergency =
                    repository.getNearestServices(
                        category = "district_emergency",
                        userLat = latitude,
                        userLon = longitude
                    )

                val police =
                    repository.getNearestServices(
                        category = "police_station",
                        userLat = latitude,
                        userLon = longitude
                    )

                val nearestTrauma =
                    traumaL1.firstOrNull()
                        ?: traumaL2.firstOrNull()

                val nearestDistrictEmergency =
                    districtEmergency.firstOrNull()

                val nearestPolice =
                    police.firstOrNull()

                val traumaText =
                    nearestTrauma?.let {
                        """
                        Nearest Trauma Center:
                        ${it.name}
                        Distance: ${"%.2f".format(it.distance)} km
                        """.trimIndent()
                    } ?: "Nearest Trauma Center: Not found"

                val districtText =
                    nearestDistrictEmergency?.let {
                        """
                        Nearest District Emergency:
                        ${it.name}
                        Distance: ${"%.2f".format(it.distance)} km
                        """.trimIndent()
                    } ?: "Nearest District Emergency: Not found"

                val policeText =
                    nearestPolice?.let {
                        """
                        Nearest Police Station:
                        ${it.name}
                        Distance: ${"%.2f".format(it.distance)} km
                        """.trimIndent()
                    } ?: "Nearest Police Station: Not found"

                val emergencyMessage =

                    """
                    🚨 RoadSOS Crash Alert
                    
                    Possible severe accident detected.
                    
                    Live Location:
                    https://maps.google.com/?q=$latitude,$longitude
                    
                    $traumaText
                    
                    $districtText
                    
                    $policeText
                    
                    Sent via RoadSOS offline emergency system.
                    """.trimIndent()

                SMSHelper.sendEmergencySMS(
                    context = context,
                    phoneNumber = "918839067564",
                    message = emergencyMessage
                )

                Log.d(
                    "EMERGENCY",
                    "Emergency SMS Triggered with nearest services"
                )

            } catch (e: Exception) {

                Log.e(
                    "EMERGENCY",
                    "Emergency Failed: ${e.message}",
                    e
                )
            }
        }
    }
}