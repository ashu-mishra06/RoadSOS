package com.example.roadsos.data.settings

data class AppSettings(
    val isDarkMode: Boolean = true,
    val languageCode: String = "en",
    val isAutoEmergencyCallEnabled: Boolean = true
)