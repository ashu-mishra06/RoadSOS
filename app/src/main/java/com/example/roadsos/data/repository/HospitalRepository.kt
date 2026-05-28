package com.example.roadsos.data.repository

import com.example.roadsos.data.local.HospitalDao
import com.example.roadsos.data.local.NearbyHospital
import com.example.roadsos.utils.DistanceUtils

class HospitalRepository(

    private val dao: HospitalDao

) {

    suspend fun getNearestServices(

        userLat: Double,
        userLon: Double,

        category: String

    ): List<NearbyHospital> {

        val nearby =

            dao.getNearbyServices(

                minLat = userLat - 0.5,
                maxLat = userLat + 0.5,

                minLon = userLon - 0.5,
                maxLon = userLon + 0.5,

                category = category

            )

        return nearby.map {

            NearbyHospital(

                hospital = it,

                distance =

                    DistanceUtils.haversine(

                        lat1 = userLat,
                        lon1 = userLon,

                        lat2 = it.latitude,
                        lon2 = it.longitude
                    )
            )
        }

            .sortedBy { it.distance }

            .take(10)
    }
}