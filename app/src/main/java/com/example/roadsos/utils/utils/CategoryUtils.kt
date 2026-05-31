package com.example.roadsos.utils

object CategoryUtils {

    fun readableName(category: String): String {

        return when (category) {

            "trauma_l1" ->
                "Level 1 Trauma Centers"

            "trauma_l2" ->
                "Level 2 Trauma Centers"

            "district_emergency" ->
                "District Emergency Services"

            "police_station" ->
                "Police Stations"

            "vehicle_repair" ->
                "Vehicle Repair Shops"

            "puncture_shop" ->
                "Puncture Shops"

            "vehicle_showroom" ->
                "Vehicle Showrooms"

            else ->
                category.uppercase()
        }
    }
}