@file:Suppress("DEPRECATION")

package com.example.spygame

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.spygame.model.Languages
import com.example.spygame.navigation.SpyNavigation
import com.example.spygame.ui.theme.EnglishTypography
import com.example.spygame.ui.theme.PersianTypography
import com.example.spygame.ui.theme.SpyGameTheme
import com.example.spygame.ui.viewmodel.LanguageViewModel
import com.example.spygame.ui.viewmodel.SettingsViewModel
import com.example.spygame.ui.viewmodel.WordScreenViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val wordScreenViewModel: WordScreenViewModel by viewModels()
    private val settingsViewModel: SettingsViewModel by viewModels()
    private val languageViewModel: LanguageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            SetAppLanguage(
                wordScreenViewModel = wordScreenViewModel,
                settingsViewModel = settingsViewModel,
                languageViewModel = languageViewModel,
            )

        }
    }
}

@Composable
fun SetAppLanguage(
    wordScreenViewModel: WordScreenViewModel,
    settingsViewModel: SettingsViewModel,
    languageViewModel: LanguageViewModel
) {
    val languages by languageViewModel.currentLanguage.collectAsState()
    val context = LocalContext.current
    val configuration = Configuration(context.resources.configuration)
    ////////////////////////////////////////
/*// بروزرسانی کلمه رندوم بر اساس زبان
    if (languages == Languages.PERSIAN.displayName) {
        wordScreenViewModel.updateRandomWordFa()
    }
    if (languages == Languages.ENGLISH.displayName) {
        wordScreenViewModel.updateRandomWordEn()
    }*/
    ////////////////////////////////////////
    when (languages) {
        Languages.PERSIAN.displayName -> {
            configuration.setLocale(Locale(Languages.PERSIAN.displayName))
        }
        Languages.ENGLISH.displayName -> {
            configuration.setLocale(Locale(Languages.ENGLISH.displayName))
        }
    }

    context.resources.updateConfiguration(configuration, context.resources.displayMetrics)

    val isRtl = languages == Languages.PERSIAN.displayName
    CompositionLocalProvider(LocalLayoutDirection provides if (isRtl) LayoutDirection.Rtl else LayoutDirection.Ltr) {
        SpyGameTheme(
            typography = when (languages) {
                Languages.PERSIAN.displayName -> PersianTypography
                Languages.ENGLISH.displayName -> EnglishTypography
                else -> EnglishTypography
            }
        ) {
            SpyNavigation(
                wordScreenViewModel = wordScreenViewModel,
                settingsViewModel = settingsViewModel,
                languageViewModel = languageViewModel
            )
        }
    }
}