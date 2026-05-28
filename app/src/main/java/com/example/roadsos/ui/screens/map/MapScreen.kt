package com.example.roadsos.ui.screens.map

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.roadsos.ui.components.BottomNavBar

@Composable
fun MapScreen(navController: NavController) {

    Scaffold(
        bottomBar = {
            BottomNavBar(navController)
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            Text("Google Maps will appear here")
        }
    }
}