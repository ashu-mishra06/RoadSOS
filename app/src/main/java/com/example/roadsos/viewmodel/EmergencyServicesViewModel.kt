package com.example.roadsos.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.roadsos.data.local.DatabaseProvider
import com.example.roadsos.data.local.NearbyEmergencyService
import com.example.roadsos.data.repository.EmergencyRepository
import com.example.roadsos.utils.LocationHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EmergencyServicesViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val context =
        application.applicationContext

    private val repository =
        EmergencyRepository(
            DatabaseProvider
                .getDatabase(context)
                .hospitalDao()
        )

    private val locationHelper =
        LocationHelper(context)

    private val _services =
        MutableStateFlow<Map<String, List<NearbyEmergencyService>>>(
            emptyMap()
        )

    val services: StateFlow<Map<String, List<NearbyEmergencyService>>> =
        _services

    private val _location =
        MutableStateFlow<Pair<Double, Double>?>(null)

    val location: StateFlow<Pair<Double, Double>?> =
        _location

    fun loadNearestServices() {

        viewModelScope.launch {

            val currentLocation =
                locationHelper.getCurrentLocation()

            _location.value =
                currentLocation

            if (currentLocation != null) {

                _services.value =
                    repository.getAllEmergencyServices(
                        userLat = currentLocation.first,
                        userLon = currentLocation.second
                    )
            }
        }
    }
}