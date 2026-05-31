package com.example.roadsos.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.roadsos.data.profile.UserProfile
import com.example.roadsos.data.profile.UserProfileDataStore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UserProfileViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val dataStore =
        UserProfileDataStore(application)

    val userProfile =
        dataStore.userProfile.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserProfile()
        )

    fun saveUserProfile(
        name: String,
        bloodGroup: String,
        emergencyContact: String,
        weight: String,
        height: String,
        age: String
    ) {
        val cleanContact =
            emergencyContact.trim()

        viewModelScope.launch {

            dataStore.saveUserProfile(
                UserProfile(
                    name = name.trim(),
                    bloodGroup = bloodGroup.trim(),
                    emergencyContact = cleanContact,
                    emergencyContacts =
                        if (cleanContact.isBlank()) {
                            emptyList()
                        } else {
                            listOf(cleanContact)
                        },
                    weight = weight.trim(),
                    height = height.trim(),
                    age = age.trim(),
                    isProfileCompleted = true
                )
            )
        }
    }

    fun updateName(
        newName: String
    ) {
        val currentProfile =
            userProfile.value

        viewModelScope.launch {

            dataStore.saveUserProfile(
                currentProfile.copy(
                    name = newName.trim(),
                    isProfileCompleted = true
                )
            )
        }
    }

    fun updateBloodGroup(
        newBloodGroup: String
    ) {
        val currentProfile =
            userProfile.value

        viewModelScope.launch {

            dataStore.saveUserProfile(
                currentProfile.copy(
                    bloodGroup = newBloodGroup.trim(),
                    isProfileCompleted = true
                )
            )
        }
    }

    fun updateEmergencyContact(
        newContact: String
    ) {
        val cleanContact =
            newContact.trim()

        val currentProfile =
            userProfile.value

        val updatedContacts =
            currentProfile
                .getAllEmergencyContacts()
                .toMutableList()

        if (cleanContact.isNotBlank()) {
            updatedContacts.add(cleanContact)
        }

        viewModelScope.launch {

            dataStore.saveUserProfile(
                currentProfile.copy(
                    emergencyContact = cleanContact,
                    emergencyContacts = updatedContacts.distinct(),
                    isProfileCompleted = true
                )
            )
        }
    }

    fun addEmergencyContact(
        newContact: String
    ) {
        val cleanContact =
            newContact.trim()

        if (cleanContact.isBlank()) {
            return
        }

        val currentProfile =
            userProfile.value

        val updatedContacts =
            currentProfile
                .getAllEmergencyContacts()
                .toMutableList()

        updatedContacts.add(cleanContact)

        viewModelScope.launch {

            dataStore.saveUserProfile(
                currentProfile.copy(
                    emergencyContact =
                        currentProfile.emergencyContact.ifBlank {
                            cleanContact
                        },
                    emergencyContacts = updatedContacts.distinct(),
                    isProfileCompleted = true
                )
            )
        }
    }

    fun removeEmergencyContact(
        contactToRemove: String
    ) {
        val cleanContactToRemove =
            contactToRemove.trim()

        val currentProfile =
            userProfile.value

        val updatedContacts =
            currentProfile
                .getAllEmergencyContacts()
                .filterNot { contact ->
                    contact == cleanContactToRemove
                }

        val newPrimaryContact =
            if (currentProfile.emergencyContact == cleanContactToRemove) {
                updatedContacts.firstOrNull() ?: ""
            } else {
                currentProfile.emergencyContact
            }

        viewModelScope.launch {

            dataStore.saveUserProfile(
                currentProfile.copy(
                    emergencyContact = newPrimaryContact,
                    emergencyContacts = updatedContacts,
                    isProfileCompleted = true
                )
            )
        }
    }

    fun updateEmergencyContactAt(
        oldContact: String,
        newContact: String
    ) {
        val cleanOldContact =
            oldContact.trim()

        val cleanNewContact =
            newContact.trim()

        if (cleanNewContact.isBlank()) {
            return
        }

        val currentProfile =
            userProfile.value

        val updatedContacts =
            currentProfile
                .getAllEmergencyContacts()
                .map { contact ->
                    if (contact == cleanOldContact) {
                        cleanNewContact
                    } else {
                        contact
                    }
                }
                .distinct()

        val newPrimaryContact =
            if (currentProfile.emergencyContact == cleanOldContact) {
                cleanNewContact
            } else {
                currentProfile.emergencyContact
            }

        viewModelScope.launch {

            dataStore.saveUserProfile(
                currentProfile.copy(
                    emergencyContact = newPrimaryContact,
                    emergencyContacts = updatedContacts,
                    isProfileCompleted = true
                )
            )
        }
    }

    fun updateWeight(
        newWeight: String
    ) {
        val currentProfile =
            userProfile.value

        viewModelScope.launch {

            dataStore.saveUserProfile(
                currentProfile.copy(
                    weight = newWeight.trim(),
                    isProfileCompleted = true
                )
            )
        }
    }

    fun updateHeight(
        newHeight: String
    ) {
        val currentProfile =
            userProfile.value

        viewModelScope.launch {

            dataStore.saveUserProfile(
                currentProfile.copy(
                    height = newHeight.trim(),
                    isProfileCompleted = true
                )
            )
        }
    }

    fun updateAge(
        newAge: String
    ) {
        val currentProfile =
            userProfile.value

        viewModelScope.launch {

            dataStore.saveUserProfile(
                currentProfile.copy(
                    age = newAge.trim(),
                    isProfileCompleted = true
                )
            )
        }
    }

    fun clearProfile() {
        viewModelScope.launch {
            dataStore.clearUserProfile()
        }
    }
}