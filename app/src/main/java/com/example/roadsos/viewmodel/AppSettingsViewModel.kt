package com.example.roadsos.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.roadsos.data.settings.AppSettings
import com.example.roadsos.data.settings.AppSettingsDataStore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AppSettingsViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val dataStore =
        AppSettingsDataStore(application)

    val appSettings =
        dataStore.appSettings.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AppSettings()
        )

    fun setDarkMode(
        isDarkMode: Boolean
    ) {
        viewModelScope.launch {
            dataStore.setDarkMode(isDarkMode)
        }
    }

    fun setLanguage(
        languageCode: String
    ) {
        viewModelScope.launch {
            dataStore.setLanguage(languageCode)
        }
    }

    fun setAutoEmergencyCallEnabled(
        isEnabled: Boolean
    ) {
        viewModelScope.launch {
            dataStore.setAutoEmergencyCallEnabled(isEnabled)
        }
    }
}