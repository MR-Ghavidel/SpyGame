package com.example.spygame.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spygame.data.pereferences.GamePreferences
import com.example.spygame.model.Languages
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LanguageViewModel @Inject constructor(private val gamePreferences: GamePreferences) : ViewModel() {

    private val _currentLanguage = MutableStateFlow(Languages.PERSIAN.displayName)
    val currentLanguage: StateFlow<String> get() = _currentLanguage

    init {
        viewModelScope.launch {
            _currentLanguage.value = gamePreferences.getLanguage()
        }
    }

    fun setLanguage(language: String) {
        _currentLanguage.value = language
        gamePreferences.saveLanguage(language)
    }
}