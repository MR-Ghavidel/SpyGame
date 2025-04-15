package com.example.spygame.data.pereferences

import android.content.Context
import com.example.spygame.model.Languages
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GamePreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs = context.getSharedPreferences("spy_game_settings_pref", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_NUM_SPIES = "num_spies"
        private const val KEY_GAME_TIME = "game_time"
        private const val KEY_NUM_PLAYERS = "num_players"
        private const val LANGUAGE_KEY = "language"
    }

    var numSpies: Int
        get() = prefs.getInt(KEY_NUM_SPIES, 1)
        set(value) = prefs.edit().putInt(KEY_NUM_SPIES, value).apply()

    var gameTime: Int
        get() = prefs.getInt(KEY_GAME_TIME, 5) //5 min
        set(value) = prefs.edit().putInt(KEY_GAME_TIME, value).apply()

    var numPlayers: Int
        get() = prefs.getInt(KEY_NUM_PLAYERS, 6)
        set(value) = prefs.edit().putInt(KEY_NUM_PLAYERS, value).apply()

    fun saveLanguage(language: String) {
        prefs.edit().putString(LANGUAGE_KEY, language).apply()
    }

    fun getLanguage(): String {
        return prefs.getString(LANGUAGE_KEY, Languages.PERSIAN.displayName) ?: Languages.PERSIAN.displayName
    }
}
