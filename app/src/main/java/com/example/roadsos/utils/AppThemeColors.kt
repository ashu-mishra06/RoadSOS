package com.example.roadsos.utils

import androidx.compose.ui.graphics.Color

data class RoadSOSThemeColors(
    val backgroundTop: Color,
    val backgroundMiddle: Color,
    val backgroundBottom: Color,
    val card: Color,
    val cardStrong: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val navBar: Color,
    val navIndicator: Color,
    val navSelected: Color,
    val navUnselected: Color,
    val border: Color
)

fun roadSOSThemeColors(
    isDarkMode: Boolean
): RoadSOSThemeColors {

    return if (isDarkMode) {

        RoadSOSThemeColors(
            backgroundTop = Color(0xFF07111F),
            backgroundMiddle = Color(0xFF0B1220),
            backgroundBottom = Color(0xFF111827),
            card = Color.White.copy(alpha = 0.07f),
            cardStrong = Color(0xFF121C2E),
            textPrimary = Color.White,
            textSecondary = Color(0xFFCBD5E1),
            navBar = Color(0xFF182234).copy(alpha = 0.96f),
            navIndicator = Color(0xFF334155).copy(alpha = 0.75f),
            navSelected = Color(0xFFE0F2FE),
            navUnselected = Color(0xFF94A3B8),
            border = Color.White.copy(alpha = 0.08f)
        )

    } else {

        RoadSOSThemeColors(
            backgroundTop = Color(0xFFF8FAFC),
            backgroundMiddle = Color(0xFFEFF6FF),
            backgroundBottom = Color(0xFFFFFFFF),
            card = Color.White.copy(alpha = 0.94f),
            cardStrong = Color(0xFFFFFFFF),
            textPrimary = Color(0xFF0F172A),
            textSecondary = Color(0xFF475569),
            navBar = Color(0xFFF8FAFC).copy(alpha = 0.98f),
            navIndicator = Color(0xFFE0F2FE),
            navSelected = Color(0xFF0369A1),
            navUnselected = Color(0xFF64748B),
            border = Color(0xFFCBD5E1).copy(alpha = 0.70f)
        )
    }
}