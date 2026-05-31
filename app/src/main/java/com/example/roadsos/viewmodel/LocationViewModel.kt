package com.example.roadsos.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.roadsos.utils.LocationHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LocationViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val helper =
        LocationHelper(application)

    private val _location =
        MutableStateFlow<Pair<Double, Double>?>(null)

    val location:
            StateFlow<Pair<Double, Double>?> =
        _location

    fun fetchLocation() {

        viewModelScope.launch {

            _location.value =
                helper.getCurrentLocation()
        }
    }
}