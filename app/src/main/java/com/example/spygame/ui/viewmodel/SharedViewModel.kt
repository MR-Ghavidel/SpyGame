package com.example.spygame.ui.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor() : ViewModel() {
    private val _time = MutableStateFlow(5)
    val time: StateFlow<Int> = _time.asStateFlow()

    fun setTime(time: Int) {
        _time.value = time
    }
}