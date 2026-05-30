package com.example.roadsos.data.local

import androidx.room.Dao
import androidx.room.Query

@Dao
interface HospitalDao {


    @Query(
        """
        SELECT * FROM emergency_services
        WHERE category = :category
        AND latitude BETWEEN :minLat AND :maxLat
        AND longitude BETWEEN :minLon AND :maxLon
        """
    )
    suspend fun getServicesByCategoryInBox(

        category: String,

        minLat: Double,
        maxLat: Double,

        minLon: Double,
        maxLon: Double

    ): List<HospitalEntity>

    @Query("SELECT DISTINCT category FROM emergency_services")
    suspend fun getAllCategories(): List<String>

    @Query("SELECT COUNT(*) FROM emergency_services")
    suspend fun getTotalServiceCount(): Int
}