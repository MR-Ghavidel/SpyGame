package com.example.spygame.ui.theme

import android.os.Build
import androidx.compose.material3.Typography
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    secondary = DarkSecondary,
    background = DarkBackground,
    surface = DarkSecondary,
    onPrimary = DarkText,
    onSecondary = DarkText,
    onBackground = DarkText,
    onSurface = DarkText,
    tertiary = DarkGray,
    onTertiary = DarkText
/*    primary = Color.Black,
    onPrimary = Color.White,
    secondary = Color.DarkGray,
    onSecondary = Color.White,
    tertiary = Color.Gray,
    onTertiary = Color.White,
    errorContainer = Color.Red,*/

)

private val LightColorScheme = lightColorScheme(
    primary = LightPrimary,
    secondary = LightSecondary,
    background = LightBackground,
    surface = LightSecondary,
    onPrimary = LightText,
    onSecondary = LightText,
    onBackground = LightText,
    onSurface = LightText,
    tertiary = LightGray,
    onTertiary = LightText
/*    primary = Color.Black,
    onPrimary = Color.White,
    secondary = Color.DarkGray,
    onSecondary = Color.White,
    tertiary = Color.Gray,
    onTertiary = Color.White,
    errorContainer = Color.Red*/
)

@Composable
fun SpyGameTheme(
    darkTheme: Boolean = true,//isSystemInDarkTheme(),
    typography: Typography,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        content = content
    )
}