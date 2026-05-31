package com.example.roadsos.data.repository

import android.location.Location
import com.example.roadsos.data.local.HospitalDao
import com.example.roadsos.data.local.NearbyEmergencyService

class EmergencyRepository(
    private val dao: HospitalDao
) {

    suspend fun getNearestServices(
        category: String,
        userLat: Double,
        userLon: Double
    ): List<NearbyEmergencyService> {

        val latRange = 1.0
        val lonRange = 1.0

        val services =
            dao.getServicesByCategoryInBox(
                category = category,
                minLat = userLat - latRange,
                maxLat = userLat + latRange,
                minLon = userLon - lonRange,
                maxLon = userLon + lonRange
            )

        return services
            .map { service ->

                val result = FloatArray(1)

                Location.distanceBetween(
                    userLat,
                    userLon,
                    service.latitude,
                    service.longitude,
                    result
                )

                NearbyEmergencyService(
                    name = service.name,
                    category = service.category,
                    latitude = service.latitude,
                    longitude = service.longitude,
                    distance = (result[0] / 1000).toDouble(),
                    isVerified = service.is_verified
                )
            }
            .sortedBy { it.distance }
            .take(5)
    }

    suspend fun getAllEmergencyServices(
        userLat: Double,
        userLon: Double
    ): Map<String, List<NearbyEmergencyService>> {

        val categories =
            listOf(
                "trauma_l1",
                "trauma_l2",
                "district_emergency",
                "police_station",
                "vehicle_repair",
                "puncture_shop",
                "vehicle_showroom"
            )

        return categories.associateWith { category ->

            getNearestServices(
                category = category,
                userLat = userLat,
                userLon = userLon
            )
        }
    }
}