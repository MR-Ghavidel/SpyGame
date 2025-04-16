package com.example.spygame.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.spygame.ui.screen.MainScreen
import com.example.spygame.ui.screen.RolesScreen
import com.example.spygame.ui.screen.RulesScreen
import com.example.spygame.ui.screen.TimerScreen
import com.example.spygame.ui.screen.WordScreen
import com.example.spygame.ui.viewmodel.LanguageViewModel
import com.example.spygame.ui.viewmodel.SettingsViewModel
import com.example.spygame.ui.viewmodel.WordScreenViewModel


@Composable
fun SpyNavigation(
    wordScreenViewModel: WordScreenViewModel,
    settingsViewModel: SettingsViewModel,
    languageViewModel: LanguageViewModel
) {
    val navController = rememberNavController()
    val layoutDirection = LocalLayoutDirection.current

    NavHost(
        modifier = Modifier.background(color = MaterialTheme.colorScheme.background),
        navController = navController,
        startDestination = SpyScreens.MainScreen.name
    ) {
        animatedComposable(route = SpyScreens.MainScreen.name, layoutDirection = layoutDirection) {
            MainScreen(
                navController = navController,
                wordScreenViewModel = wordScreenViewModel,
                settingsViewModel = settingsViewModel,
                languageViewModel = languageViewModel
            )
        }
        animatedComposable(route = SpyScreens.WordScreen.name, layoutDirection = layoutDirection) {
            WordScreen(
                navController = navController,
                wordScreenViewModel = wordScreenViewModel,
                languageViewModel = languageViewModel
            )
        }
        animatedComposable(
            route = "${SpyScreens.RolesScreen.name}?players={players}&spies={spies}",
            layoutDirection = layoutDirection
        ) { backStackEntry ->
            val players = backStackEntry.arguments?.getString("players")?.toInt() ?: 2
            val spies = backStackEntry.arguments?.getString("spies")?.toInt() ?: 1
            RolesScreen(
                navController = navController,
                wordScreenViewModel = wordScreenViewModel,
                players = players,
                spies = spies,
            )
        }
        animatedComposable(route = SpyScreens.TimerScreen.name, layoutDirection = layoutDirection) {
            TimerScreen(
                navController = navController,
                settingsViewModel = settingsViewModel,
                languageViewModel = languageViewModel
            )
        }
        animatedComposable(route = SpyScreens.RulesScreen.name, layoutDirection = layoutDirection) {
            RulesScreen(
                navController = navController
            )
        }
    }
}


fun NavGraphBuilder.animatedComposable(
    route: String,
    layoutDirection: LayoutDirection,
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    composable(
        route = route,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { if (layoutDirection == LayoutDirection.Ltr) 1000 else -1000 },
                animationSpec = tween(500)
            ) + fadeIn(animationSpec = tween(500))
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { if (layoutDirection == LayoutDirection.Ltr) -1000 else 1000 },
                animationSpec = tween(500)
            ) + fadeOut(animationSpec = tween(500))
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { if (layoutDirection == LayoutDirection.Ltr) -1000 else 1000 },
                animationSpec = tween(500)
            ) + fadeIn(animationSpec = tween(500))
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { if (layoutDirection == LayoutDirection.Ltr) 1000 else -1000 },
                animationSpec = tween(500)
            ) + fadeOut(animationSpec = tween(500))
        },
        content = content
    )
}
