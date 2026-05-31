package com.example.roadsos.data.profile

data class UserProfile(
    val name: String = "",
    val bloodGroup: String = "",
    val emergencyContact: String = "",
    val emergencyContacts: List<String> = emptyList(),
    val weight: String = "",
    val height: String = "",
    val age: String = "",
    val isProfileCompleted: Boolean = false
) {
    fun getAllEmergencyContacts(): List<String> {
        val contacts =
            mutableListOf<String>()

        if (emergencyContact.isNotBlank()) {
            contacts.add(emergencyContact.trim())
        }

        emergencyContacts.forEach { contact ->
            if (contact.isNotBlank()) {
                contacts.add(contact.trim())
            }
        }

        return contacts
            .distinct()
    }
}