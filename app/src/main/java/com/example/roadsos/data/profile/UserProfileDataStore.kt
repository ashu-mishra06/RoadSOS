package com.example.roadsos.data.profile

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.userProfileDataStore by preferencesDataStore(
    name = "user_profile"
)

class UserProfileDataStore(
    private val context: Context
) {
    companion object {

        private val NAME =
            stringPreferencesKey("name")

        private val BLOOD_GROUP =
            stringPreferencesKey("blood_group")

        private val EMERGENCY_CONTACT =
            stringPreferencesKey("emergency_contact")

        private val EMERGENCY_CONTACTS =
            stringPreferencesKey("emergency_contacts")

        private val WEIGHT =
            stringPreferencesKey("weight")

        private val HEIGHT =
            stringPreferencesKey("height")

        private val AGE =
            stringPreferencesKey("age")

        private val IS_PROFILE_COMPLETED =
            booleanPreferencesKey("is_profile_completed")

        private const val CONTACT_SEPARATOR =
            "||"
    }

    val userProfile: Flow<UserProfile> =
        context.userProfileDataStore.data.map { preferences ->

            val primaryContact =
                preferences[EMERGENCY_CONTACT] ?: ""

            val contactsString =
                preferences[EMERGENCY_CONTACTS] ?: ""

            val savedContacts =
                decodeContacts(contactsString)

            UserProfile(
                name = preferences[NAME] ?: "",
                bloodGroup = preferences[BLOOD_GROUP] ?: "",
                emergencyContact = primaryContact,
                emergencyContacts = savedContacts,
                weight = preferences[WEIGHT] ?: "",
                height = preferences[HEIGHT] ?: "",
                age = preferences[AGE] ?: "",
                isProfileCompleted = preferences[IS_PROFILE_COMPLETED] ?: false
            )
        }

    suspend fun saveUserProfile(
        userProfile: UserProfile
    ) {
        val cleanPrimaryContact =
            userProfile.emergencyContact.trim()

        val cleanContactList =
            userProfile
                .getAllEmergencyContacts()
                .distinct()

        context.userProfileDataStore.edit { preferences ->

            preferences[NAME] =
                userProfile.name.trim()

            preferences[BLOOD_GROUP] =
                userProfile.bloodGroup.trim()

            preferences[EMERGENCY_CONTACT] =
                cleanPrimaryContact

            preferences[EMERGENCY_CONTACTS] =
                encodeContacts(cleanContactList)

            preferences[WEIGHT] =
                userProfile.weight.trim()

            preferences[HEIGHT] =
                userProfile.height.trim()

            preferences[AGE] =
                userProfile.age.trim()

            preferences[IS_PROFILE_COMPLETED] =
                userProfile.isProfileCompleted
        }
    }

    suspend fun clearUserProfile() {
        context.userProfileDataStore.edit { preferences ->
            preferences.clear()
        }
    }

    private fun encodeContacts(
        contacts: List<String>
    ): String {
        return contacts
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .distinct()
            .joinToString(CONTACT_SEPARATOR)
    }

    private fun decodeContacts(
        contactsString: String
    ): List<String> {
        if (contactsString.isBlank()) {
            return emptyList()
        }

        return contactsString
            .split(CONTACT_SEPARATOR)
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .distinct()
    }
}