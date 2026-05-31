package com.example.roadsos.utils

import android.content.Context
import android.util.Log
import com.example.roadsos.data.local.DatabaseProvider
import com.example.roadsos.data.repository.EmergencyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object DatabaseTestHelper {

    fun testEmergencyDatabase(context: Context) {

        CoroutineScope(Dispatchers.IO).launch {

            try {

                val locationHelper =
                    LocationHelper(context)

                val location =
                    locationHelper.getCurrentLocation()

                if (location == null) {

                    Log.e(
                        "DB_TEST",
                        "Location is null. Turn on GPS and allow location permission."
                    )

                    return@launch
                }

                val database =
                    DatabaseProvider.getDatabase(context)

                val dao =
                    database.hospitalDao()

                val totalCount =
                    dao.getTotalServiceCount()

                val availableCategories =
                    dao.getAllCategories()

                Log.d(
                    "DB_TEST",
                    "TOTAL DB ROWS: $totalCount"
                )

                Log.d(
                    "DB_TEST",
                    "AVAILABLE CATEGORIES: $availableCategories"
                )

                val repository =
                    EmergencyRepository(
                        database.hospitalDao()
                    )

                val services =
                    repository.getAllEmergencyServices(
                        userLat = location.first,
                        userLon = location.second
                    )

                Log.d(
                    "DB_TEST",
                    "Current location: ${location.first}, ${location.second}"
                )

                services.forEach { entry ->

                    Log.d(
                        "DB_TEST",
                        "CATEGORY: ${entry.key}, COUNT: ${entry.value.size}"
                    )

                    entry.value.forEach { service ->

                        Log.d(
                            "DB_TEST",
                            "${service.name} | ${service.category} | ${"%.2f".format(service.distance)} km"
                        )
                    }
                }

            } catch (e: Exception) {

                Log.e(
                    "DB_TEST",
                    "Database test failed: ${e.message}"
                )

                e.printStackTrace()
            }
        }
    }
}