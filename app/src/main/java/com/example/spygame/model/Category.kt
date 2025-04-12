package com.example.spygame.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.spygame.R

enum class Category(private val stringResId: Int) {
    USER_DEFINED(R.string.user_defined),
    PLACE(R.string.place),
    FOOD(R.string.food),
    JOB(R.string.job),
    ANIMAL(R.string.animal);


    @Composable
    fun getDisplayName(): String {
        val context = LocalContext.current
        return context.getString(stringResId)
    }
}