package com.example.roadsos.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import com.example.roadsos.ui.screens.home.HomeScreen
import com.example.roadsos.ui.screens.map.MapScreen
import com.example.roadsos.ui.screens.settings.SettingsScreen
import com.example.roadsos.ui.screens.onboarding.PermissionScreen

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "permission"
    ) {

        composable("permission") {
            PermissionScreen(navController)
        }

        composable("home") {
            HomeScreen(navController)
        }

        composable("map") {
            MapScreen(navController)
        }

        composable("settings") {
            SettingsScreen(navController)
        }
    }
}