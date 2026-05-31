package com.example.roadsos.utils

object LanguageUtils {

    data class AppLanguage(
        val code: String,
        val displayName: String
    )

    val supportedLanguages =
        listOf(
            AppLanguage("en", "English"),
            AppLanguage("hi", "हिन्दी"),
            AppLanguage("bn", "বাংলা"),
            AppLanguage("ta", "தமிழ்"),
            AppLanguage("te", "తెలుగు"),
            AppLanguage("mr", "मराठी")
        )

    fun getLanguageName(
        code: String
    ): String {
        return supportedLanguages
            .firstOrNull { it.code == code }
            ?.displayName ?: "English"
    }
}