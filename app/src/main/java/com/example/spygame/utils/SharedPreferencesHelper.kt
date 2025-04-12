package com.example.spygame.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.spygame.model.Languages

class SharedPreferencesHelper(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)

    companion object {
        private const val LANGUAGE_KEY = "language"
    }

    fun saveLanguage(language: String) {
        sharedPreferences.edit().putString(LANGUAGE_KEY, language).apply()
    }

    fun getLanguage(): String {
        return sharedPreferences.getString(LANGUAGE_KEY, Languages.ENGLISH.displayName) ?: Languages.ENGLISH.displayName
    }
}