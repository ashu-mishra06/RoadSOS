package com.example.roadsos.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "emergency_services",
    indices = [
        Index(
            value = ["category"],
            name = "idx_category"
        ),
        Index(
            value = ["latitude", "longitude"],
            name = "idx_location"
        )
    ]
)
data class HospitalEntity(

    @PrimaryKey
    val id: Int,

    val name: String,

    val category: String,

    val latitude: Double,

    val longitude: Double,

    val is_verified: Int
)