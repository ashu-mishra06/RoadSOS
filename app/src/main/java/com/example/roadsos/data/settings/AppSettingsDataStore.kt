package com.example.roadsos.data.settings

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.appSettingsDataStore by preferencesDataStore(
    name = "app_settings"
)

class AppSettingsDataStore(
    private val context: Context
) {

    companion object {

        private val IS_DARK_MODE =
            booleanPreferencesKey("is_dark_mode")

        private val LANGUAGE_CODE =
            stringPreferencesKey("language_code")

        private val IS_AUTO_EMERGENCY_CALL_ENABLED =
            booleanPreferencesKey("is_auto_emergency_call_enabled")
    }

    val appSettings: Flow<AppSettings> =
        context.appSettingsDataStore.data.map { preferences ->

            AppSettings(
                isDarkMode = preferences[IS_DARK_MODE] ?: true,
                languageCode = preferences[LANGUAGE_CODE] ?: "en",
                isAutoEmergencyCallEnabled =
                    preferences[IS_AUTO_EMERGENCY_CALL_ENABLED] ?: true
            )
        }

    suspend fun setDarkMode(
        isDarkMode: Boolean
    ) {
        context.appSettingsDataStore.edit { preferences ->

            preferences[IS_DARK_MODE] =
                isDarkMode
        }
    }

    suspend fun setLanguage(
        languageCode: String
    ) {
        context.appSettingsDataStore.edit { preferences ->

            preferences[LANGUAGE_CODE] =
                languageCode
        }
    }

    suspend fun setAutoEmergencyCallEnabled(
        isEnabled: Boolean
    ) {
        context.appSettingsDataStore.edit { preferences ->

            preferences[IS_AUTO_EMERGENCY_CALL_ENABLED] =
                isEnabled
        }
    }
}