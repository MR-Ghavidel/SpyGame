package com.example.spygame.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.res.painterResource

@Composable
fun InvertibleImage(
    imageRes: Int,
    modifier: Modifier = Modifier
) {
    //val isLightTheme = !isSystemInDarkTheme()
    val isLightTheme = false
    val colorMatrix = if (isLightTheme) {
        ColorMatrix(floatArrayOf(
            -1f,  0f,  0f,  0f,  255f,
            0f, -1f,  0f,  0f,  255f,
            0f,  0f, -1f,  0f,  255f,
            0f,  0f,  0f,  1f,    0f
        ))
    } else {
        ColorMatrix()
    }
    Image(
        modifier = modifier,
        painter = painterResource(id = imageRes),
        contentDescription = "Spy Icon",
        colorFilter = ColorFilter.colorMatrix(colorMatrix)
    )
}