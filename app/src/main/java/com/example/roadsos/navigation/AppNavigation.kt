package com.example.roadsos.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.roadsos.ui.screens.home.HomeScreen
import com.example.roadsos.ui.screens.map.MapScreen
import com.example.roadsos.ui.screens.profile.ProfileSetupScreen
import com.example.roadsos.ui.screens.profile.ProfileViewScreen
import com.example.roadsos.ui.screens.settings.SettingsScreen
import com.example.roadsos.utils.roadSOSThemeColors
import com.example.roadsos.viewmodel.AppSettingsViewModel
import com.example.roadsos.viewmodel.EmergencyViewModel
import com.example.roadsos.viewmodel.UserProfileViewModel

@Composable
fun AppNavigation() {

    val navController =
        rememberNavController()

    val userProfileViewModel: UserProfileViewModel =
        viewModel()

    val appSettingsViewModel: AppSettingsViewModel =
        viewModel()

    /*
     IMPORTANT:
     EmergencyViewModel is created here, not inside HomeScreen.
     This keeps emergency state alive while moving Home <-> Map <-> Settings.
    */
    val emergencyViewModel: EmergencyViewModel =
        viewModel()

    val userProfile by
    userProfileViewModel.userProfile.collectAsState()

    val appSettings by
    appSettingsViewModel.appSettings.collectAsState()

    val themeColors =
        roadSOSThemeColors(appSettings.isDarkMode)

    val startDestination =
        if (userProfile.isProfileCompleted) {
            RoadSOSRoutes.HOME
        } else {
            RoadSOSRoutes.PROFILE_SETUP
        }

    val appBackground =
        Brush.verticalGradient(
            listOf(
                themeColors.backgroundTop,
                themeColors.backgroundMiddle,
                themeColors.backgroundBottom
            )
        )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(appBackground)
    ) {

        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier
                .fillMaxSize()
                .background(appBackground)
        ) {

            composable(
                route = RoadSOSRoutes.PROFILE_SETUP
            ) {

                ProfileSetupScreen(
                    onProfileSaved = {
                        navController.navigate(RoadSOSRoutes.HOME) {
                            popUpTo(RoadSOSRoutes.PROFILE_SETUP) {
                                inclusive = true
                            }

                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(
                route = RoadSOSRoutes.HOME
            ) {

                HomeScreen(
                    navController = navController,
                    emergencyViewModel = emergencyViewModel
                )
            }

            composable(
                route = RoadSOSRoutes.MAP
            ) {

                MapScreen(
                    navController = navController
                )
            }

            composable(
                route = RoadSOSRoutes.SETTINGS
            ) {

                SettingsScreen(
                    navController = navController
                )
            }

            composable(
                route = RoadSOSRoutes.PROFILE_VIEW
            ) {

                ProfileViewScreen(
                    navController = navController
                )
            }
        }
    }
}

object RoadSOSRoutes {

    const val PROFILE_SETUP =
        "profile_setup"

    const val HOME =
        "home"

    const val MAP =
        "map"

    const val SETTINGS =
        "settings"

    const val PROFILE_VIEW =
        "profile_view"
}