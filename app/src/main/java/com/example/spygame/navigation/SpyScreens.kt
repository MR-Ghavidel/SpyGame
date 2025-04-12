package com.example.spygame.navigation

enum class SpyScreens {
    MainScreen,
    RolesScreen,
    TimerScreen,
    RulesScreen,
    WordScreen;

    companion object {
        fun fromRoute(route: String?): SpyScreens = when (route?.substringBefore("/")) {
            MainScreen.name -> MainScreen
            WordScreen.name -> WordScreen
            RolesScreen.name -> RolesScreen
            TimerScreen.name -> TimerScreen
            RulesScreen.name -> RulesScreen
            null -> MainScreen
            else -> throw IllegalArgumentException("Route $route is not recognized")
        }
    }
}