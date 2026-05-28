package com.example.roadsos.data.local

import androidx.room.Dao
import androidx.room.Query

@Dao
interface HospitalDao {

    @Query("""

        SELECT *

        FROM emergency_services

        WHERE latitude BETWEEN :minLat AND :maxLat

        AND longitude BETWEEN :minLon AND :maxLon

        AND category = :category

    """)

    suspend fun getNearbyServices(

        minLat: Double,
        maxLat: Double,

        minLon: Double,
        maxLon: Double,

        category: String

    ): List<HospitalEntity>
}