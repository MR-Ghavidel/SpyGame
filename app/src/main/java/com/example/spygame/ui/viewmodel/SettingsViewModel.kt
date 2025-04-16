package com.example.spygame.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.spygame.data.pereferences.GamePreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val gamePreferences: GamePreferences
) : ViewModel() {

    var numSpies by mutableIntStateOf(gamePreferences.numSpies)
        private set

    var gameTime by mutableIntStateOf(gamePreferences.gameTime)
        private set

    var numPlayers by mutableIntStateOf(gamePreferences.numPlayers)
        private set

    fun updateNumSpies(value: Int) {
        numSpies = value
        gamePreferences.numSpies = value
    }

    fun updateGameTime(value: Int) {
        gameTime = value
        gamePreferences.gameTime = value
    }

    fun updateNumPlayers(value: Int) {
        numPlayers = value
        gamePreferences.numPlayers = value
    }
}

