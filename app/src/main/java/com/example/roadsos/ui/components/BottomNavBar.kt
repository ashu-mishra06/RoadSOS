package com.example.roadsos.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.roadsos.utils.AppText
import com.example.roadsos.utils.roadSOSThemeColors
import com.example.roadsos.viewmodel.AppSettingsViewModel

@Composable
fun BottomNavBar(
    navController: NavController
) {
    val appSettingsViewModel: AppSettingsViewModel =
        viewModel()

    val appSettings by
    appSettingsViewModel.appSettings.collectAsState()

    val themeColors =
        roadSOSThemeColors(appSettings.isDarkMode)

    val language =
        appSettings.languageCode

    val navBackStackEntry by
    navController.currentBackStackEntryAsState()

    val currentRoute =
        navBackStackEntry?.destination?.route

    val navItems =
        listOf(
            BottomNavItem(
                title = AppText.t(
                    language,
                    "home"
                ),
                route = "home",
                icon = Icons.Default.Home
            ),
            BottomNavItem(
                title = AppText.t(
                    language,
                    "map"
                ),
                route = "map",
                icon = Icons.Default.Map
            ),
            BottomNavItem(
                title = AppText.t(
                    language,
                    "settings"
                ),
                route = "settings",
                icon = Icons.Default.Settings
            )
        )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(horizontal = 18.dp)
            .padding(bottom = 12.dp)
            .navigationBarsPadding(),
        horizontalArrangement = Arrangement.Center
    ) {

        NavigationBar(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(32.dp))
                .background(themeColors.navBar)
                .border(
                    width = 1.dp,
                    color = themeColors.border,
                    shape = RoundedCornerShape(32.dp)
                ),
            containerColor = Color.Transparent,
            tonalElevation = 0.dp
        ) {

            navItems.forEach { item ->

                val selected =
                    currentRoute == item.route

                NavigationBarItem(
                    selected = selected,
                    onClick = {
                        if (currentRoute != item.route) {

                            navController.navigate(item.route) {
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title
                        )
                    },
                    label = {
                        Text(
                            text = item.title,
                            fontWeight =
                                if (selected) {
                                    FontWeight.SemiBold
                                } else {
                                    FontWeight.Normal
                                }
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = themeColors.navSelected,
                        selectedTextColor = themeColors.navSelected,
                        indicatorColor = themeColors.navIndicator,
                        unselectedIconColor = themeColors.navUnselected,
                        unselectedTextColor = themeColors.navUnselected
                    )
                )
            }
        }
    }
}

private data class BottomNavItem(
    val title: String,
    val route: String,
    val icon: ImageVector
)