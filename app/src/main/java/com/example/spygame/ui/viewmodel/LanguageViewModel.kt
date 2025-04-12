package com.example.spygame.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spygame.model.Languages
import com.example.spygame.utils.SharedPreferencesHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LanguageViewModel @Inject constructor(private val sharedPreferencesHelper: SharedPreferencesHelper) : ViewModel() {

    private val _currentLanguage = MutableStateFlow(Languages.ENGLISH.displayName)
    val currentLanguage: StateFlow<String> get() = _currentLanguage

    init {
        viewModelScope.launch {
            _currentLanguage.value = sharedPreferencesHelper.getLanguage()
        }
    }

    fun setLanguage(language: String) {
        _currentLanguage.value = language
        sharedPreferencesHelper.saveLanguage(language)
    }
}