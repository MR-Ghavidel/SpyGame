package com.example.spygame.ui.components.picker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun rememberPickerState(min: Int) = remember { PickerState(min) }

class PickerState(min: Int) {
    var selectedItem by mutableStateOf("$min")
}