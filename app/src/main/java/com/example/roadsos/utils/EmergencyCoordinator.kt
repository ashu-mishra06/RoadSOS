package com.example.roadsos.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log

import com.example.roadsos.data.local.DatabaseProvider
import com.example.roadsos.data.repository.EmergencyRepository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EmergencyCoordinator(

    private val context: Context

) {

    fun startEmergencyFlow() {

        CoroutineScope(
            Dispatchers.IO
        ).launch {

            try {

                // -------------------------
                // GET LOCATION
                // -------------------------

                val locationHelper =
                    LocationHelper(context)

                val location =
                    locationHelper
                        .getCurrentLocation()

                if (location == null) {

                    Log.e(
                        "EMERGENCY",
                        "Location unavailable"
                    )

                    return@launch
                }

                val userLat = location.first
                val userLon = location.second

                // -------------------------
                // DATABASE
                // -------------------------

                val database =
                    DatabaseProvider
                        .getDatabase(context)

                val dao =
                    database.hospitalDao()

                val repository =
                    EmergencyRepository(dao)

                // -------------------------
                // FETCH SERVICES
                // -------------------------

                val hospitals =
                    repository.getNearestServices(
                        "hospital",
                        userLat,
                        userLon
                    )

                val police =
                    repository.getNearestServices(
                        "police",
                        userLat,
                        userLon
                    )

                val fireStations =
                    repository.getNearestServices(
                        "fire_station",
                        userLat,
                        userLon
                    )

                val towing =
                    repository.getNearestServices(
                        "towing",
                        userLat,
                        userLon
                    )

                // -------------------------
                // LOG RESULTS
                // -------------------------

                Log.d(
                    "EMERGENCY",
                    "Hospitals: ${hospitals.size}"
                )

                Log.d(
                    "EMERGENCY",
                    "Police: ${police.size}"
                )

                Log.d(
                    "EMERGENCY",
                    "Fire: ${fireStations.size}"
                )

                Log.d(
                    "EMERGENCY",
                    "Towing: ${towing.size}"
                )

                // -------------------------
                // SEND SMS
                // -------------------------

                val emergencyMessage =

                    """
                    🚨 RoadSOS Crash Alert
                    
                    Possible severe accident detected.
                    
                    Live Location:
                    https://maps.google.com/?q=$userLat,$userLon
                    """.trimIndent()

                SMSHelper.sendEmergencySMS(

                    context,

                    "8839067564",

                    emergencyMessage
                )

                // -------------------------
                // AUTO CALL
                // -------------------------

                if (hospitals.isNotEmpty()) {

                    val callIntent = Intent(

                        Intent.ACTION_CALL,

                        Uri.parse(
                            "tel:102"
                        )
                    )

                    callIntent.flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK

                    context.startActivity(callIntent)
                }

            } catch (e: Exception) {

                Log.e(
                    "EMERGENCY",
                    "Emergency flow failed: ${e.message}"
                )
            }
        }
    }
}