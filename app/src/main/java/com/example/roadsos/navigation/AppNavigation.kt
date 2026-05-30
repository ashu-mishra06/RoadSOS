package com.example.roadsos.navigation

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.roadsos.ui.screens.home.HomeScreen
import com.example.roadsos.ui.screens.map.MapScreen
import com.example.roadsos.ui.screens.profile.ProfileSetupScreen
import com.example.roadsos.ui.screens.profile.ProfileViewScreen
import com.example.roadsos.ui.screens.settings.SettingsScreen
import com.example.roadsos.viewmodel.UserProfileViewModel

@Composable
fun AppNavigation() {

    val navController =
        rememberNavController()

    val userProfileViewModel: UserProfileViewModel =
        viewModel()

    val userProfile by
    userProfileViewModel.userProfile.collectAsState()

    val startDestination =
        if (userProfile.isProfileCompleted) {
            "home"
        } else {
            "profile_setup"
        }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        composable("profile_setup") {

            ProfileSetupScreen(
                onProfileSaved = {
                    navController.navigate("home") {
                        popUpTo("profile_setup") {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable("home") {

            HomeScreen(
                navController = navController
            )
        }

        composable("map") {

            MapScreen(
                navController = navController
            )
        }

        composable("settings") {

            SettingsScreen(
                navController = navController
            )
        }

        composable("profile_view") {

            ProfileViewScreen(
                navController = navController
            )
        }
    }
}