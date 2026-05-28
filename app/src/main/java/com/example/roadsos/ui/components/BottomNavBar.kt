package com.example.roadsos.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

@Composable
fun BottomNavBar(navController: NavController) {

    NavigationBar {

        NavigationBarItem(
            selected = false,
            onClick = {
                navController.navigate("home")
            },
            icon = {
                Icon(
                    Icons.Default.Home,
                    contentDescription = null
                )
            },
            label = {
                Text("Home")
            }
        )

        NavigationBarItem(
            selected = false,
            onClick = {
                navController.navigate("map")
            },
            icon = {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = null
                )
            },
            label = {
                Text("Map")
            }
        )

        NavigationBarItem(
            selected = false,
            onClick = {
                navController.navigate("settings")
            },
            icon = {
                Icon(
                    Icons.Default.Settings,
                    contentDescription = null
                )
            },
            label = {
                Text("Settings")
            }
        )
    }
}