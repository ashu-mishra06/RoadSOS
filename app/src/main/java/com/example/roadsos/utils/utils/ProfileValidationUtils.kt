package com.example.roadsos.utils

object ProfileValidationUtils {

    val bloodGroups =
        listOf(
            "A+",
            "A-",
            "B+",
            "B-",
            "AB+",
            "AB-",
            "O+",
            "O-"
        )

    fun validateName(name: String): String? {

        val trimmedName =
            name.trim()

        if (trimmedName.isBlank()) {
            return "Name cannot be empty."
        }

        if (trimmedName.length < 2) {
            return "Name must be at least 2 characters."
        }

        if (trimmedName.length > 50) {
            return "Name is too long."
        }

        val nameRegex =
            Regex("^[a-zA-Z ]+$")

        if (!nameRegex.matches(trimmedName)) {
            return "Name should contain only letters and spaces."
        }

        return null
    }

    fun validateBloodGroup(bloodGroup: String): String? {

        if (bloodGroup.isBlank()) {
            return "Please select blood group."
        }

        if (!bloodGroups.contains(bloodGroup)) {
            return "Please select a valid blood group."
        }

        return null
    }

    fun validateEmergencyContact(contact: String): String? {

        val cleanContact =
            contact.trim()

        if (cleanContact.isBlank()) {
            return "Emergency contact cannot be empty."
        }

        if (!cleanContact.all { it.isDigit() }) {
            return "Emergency contact should contain only numbers."
        }

        if (cleanContact.length != 10) {
            return "Emergency contact must be 10 digits."
        }

        if (cleanContact.all { it == cleanContact.first() }) {
            return "Please enter a valid emergency contact number."
        }

        if (
            cleanContact.first() != '6' &&
            cleanContact.first() != '7' &&
            cleanContact.first() != '8' &&
            cleanContact.first() != '9'
        ) {
            return "Indian mobile number should start with 6, 7, 8, or 9."
        }

        return null
    }

    fun validateAge(age: String): String? {

        val ageNumber =
            age.trim().toIntOrNull()

        if (age.trim().isBlank()) {
            return "Age cannot be empty."
        }

        if (ageNumber == null) {
            return "Age should be a number."
        }

        if (ageNumber !in 1..120) {
            return "Age should be between 1 and 120."
        }

        return null
    }

    fun validateWeight(weight: String): String? {

        val weightNumber =
            weight.trim().toIntOrNull()

        if (weight.trim().isBlank()) {
            return "Weight cannot be empty."
        }

        if (weightNumber == null) {
            return "Weight should be a number in kg."
        }

        if (weightNumber !in 2..300) {
            return "Weight should be between 2 kg and 300 kg."
        }

        return null
    }

    fun validateHeight(height: String): String? {

        val heightNumber =
            height.trim().toIntOrNull()

        if (height.trim().isBlank()) {
            return "Height cannot be empty."
        }

        if (heightNumber == null) {
            return "Height should be a number in cm."
        }

        if (heightNumber !in 30..250) {
            return "Height should be between 30 cm and 250 cm."
        }

        return null
    }

    fun validateFullProfile(
        name: String,
        bloodGroup: String,
        emergencyContact: String,
        weight: String,
        height: String,
        age: String
    ): String? {

        return validateName(name)
            ?: validateBloodGroup(bloodGroup)
            ?: validateEmergencyContact(emergencyContact)
            ?: validateWeight(weight)
            ?: validateHeight(height)
            ?: validateAge(age)
    }
}