package com.example.roadsos.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

object MapIntentHelper {

    fun openLocation(
        context: Context,
        latitude: Double,
        longitude: Double,
        label: String
    ) {
        try {

            val encodedLabel =
                Uri.encode(label.ifBlank { "RoadSOS Location" })

            val geoUri =
                Uri.parse(
                    "geo:$latitude,$longitude?q=$latitude,$longitude($encodedLabel)"
                )

            val mapIntent =
                Intent(
                    Intent.ACTION_VIEW,
                    geoUri
                )

            mapIntent.setPackage("com.google.android.apps.maps")

            if (mapIntent.resolveActivity(context.packageManager) != null) {

                context.startActivity(mapIntent)

            } else {

                openLocationInBrowser(
                    context = context,
                    latitude = latitude,
                    longitude = longitude
                )
            }

        } catch (e: Exception) {

            e.printStackTrace()

            Toast.makeText(
                context,
                "Unable to open map",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun openNavigation(
        context: Context,
        latitude: Double,
        longitude: Double
    ) {
        try {

            val navigationUri =
                Uri.parse(
                    "google.navigation:q=$latitude,$longitude&mode=d"
                )

            val navigationIntent =
                Intent(
                    Intent.ACTION_VIEW,
                    navigationUri
                )

            navigationIntent.setPackage("com.google.android.apps.maps")

            if (navigationIntent.resolveActivity(context.packageManager) != null) {

                context.startActivity(navigationIntent)

            } else {

                openLocationInBrowser(
                    context = context,
                    latitude = latitude,
                    longitude = longitude
                )
            }

        } catch (e: Exception) {

            e.printStackTrace()

            Toast.makeText(
                context,
                "Unable to open navigation",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun openLocationInBrowser(
        context: Context,
        latitude: Double,
        longitude: Double
    ) {
        try {

            val browserUri =
                Uri.parse(
                    "https://www.google.com/maps/search/?api=1&query=$latitude,$longitude"
                )

            val browserIntent =
                Intent(
                    Intent.ACTION_VIEW,
                    browserUri
                )

            if (browserIntent.resolveActivity(context.packageManager) != null) {

                context.startActivity(browserIntent)

            } else {

                Toast.makeText(
                    context,
                    "No app found to open location",
                    Toast.LENGTH_SHORT
                ).show()
            }

        } catch (e: Exception) {

            e.printStackTrace()

            Toast.makeText(
                context,
                "Unable to open location",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}