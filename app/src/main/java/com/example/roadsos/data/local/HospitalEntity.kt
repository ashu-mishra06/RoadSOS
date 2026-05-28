package com.example.roadsos.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "emergency_services"
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