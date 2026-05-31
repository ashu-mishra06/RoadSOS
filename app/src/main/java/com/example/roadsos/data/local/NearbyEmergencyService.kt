package com.example.roadsos.data.local

data class NearbyEmergencyService(

    val name: String,

    val category: String,

    val latitude: Double,

    val longitude: Double,

    val distance: Double,

    val isVerified: Int
)