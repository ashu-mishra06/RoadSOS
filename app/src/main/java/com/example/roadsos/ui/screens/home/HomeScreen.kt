package com.example.roadsos.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.roadsos.ui.components.BottomNavBar

//new import
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import com.example.roadsos.viewmodel.LocationViewModel

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
@Composable
fun HomeScreen(navController: NavController) {

    val viewModel: LocationViewModel = viewModel()

    LaunchedEffect(Unit) {
        viewModel.fetchLocation()
    }

    val location by viewModel.location.collectAsState()

    Scaffold(
        bottomBar = {
            BottomNavBar(navController)
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFF101820),
                            Color(0xFF1B2A41)
                        )
                    )
                )
                .padding(20.dp)
        ) {

            Text(
                text = "RoadSOS",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF243447)
                )
            ) {

                Column(
                    modifier = Modifier.padding(20.dp)
                ) {

                    Text(
                        text = "Protection Status",
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "ACTIVE",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.Green
                    )

                    Text(
                        text =
                            location?.let {
                                "Lat: ${it.first}, Lon: ${it.second}"
                            } ?: "Fetching location...",


                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.Green
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red
                )
            ) {

                Icon(
                    Icons.Default.Warning,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text("Emergency SOS")
            }
        }
    }
}