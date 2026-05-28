package com.example.roadsos.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.roadsos.ui.components.BottomNavBar

@Composable
fun SettingsScreen(navController: NavController) {

    Scaffold(
        bottomBar = {
            BottomNavBar(navController)
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp)
        ) {

            Text(
                text = "Settings",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text("User Details")

            Text("Crash Sharing Preferences")

            Text("Emergency Contacts")
        }
    }
}