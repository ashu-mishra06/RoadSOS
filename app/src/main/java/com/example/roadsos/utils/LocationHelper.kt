package com.example.roadsos.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.tasks.await

class LocationHelper(
    private val context: Context
) {

    private val fusedLocationClient =
        LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(): Pair<Double, Double>? {

        val fineLocation =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            )

        if (fineLocation != PackageManager.PERMISSION_GRANTED) {
            return null
        }

        return try {

            val location =
                fusedLocationClient.lastLocation.await()

            if (location != null) {

                Pair(
                    location.latitude,
                    location.longitude
                )

            } else {
                null
            }

        } catch (e: Exception) {

            e.printStackTrace()
            null
        }
    }
}