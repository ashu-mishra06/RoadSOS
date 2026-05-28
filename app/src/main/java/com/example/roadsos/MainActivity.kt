package com.example.roadsos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.roadsos.navigation.AppNavigation
import com.example.roadsos.ui.theme.RoadSOSTheme
import android.Manifest
import androidx.activity.result.contract.ActivityResultContracts
import android.content.Intent
import androidx.core.content.ContextCompat
import com.example.roadsos.services.SensorMonitoringService
class MainActivity : ComponentActivity() {

//    private val requestPermissionLauncher =
//        registerForActivityResult(
//            ActivityResultContracts.RequestPermission()
//        ) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        requestPermissionLauncher.launch(
//            Manifest.permission.ACCESS_FINE_LOCATION
//        )

        if (android.os.Build.VERSION.SDK_INT >=
            android.os.Build.VERSION_CODES.TIRAMISU
        ) {

            requestPermissions(
                arrayOf(
                    android.Manifest.permission.POST_NOTIFICATIONS
                ),
                100
            )
        }

        try {

            val intent = Intent(
                this,
                SensorMonitoringService::class.java
            )

            ContextCompat.startForegroundService(
                this,
                intent
            )

        } catch (e: Exception) {

            e.printStackTrace()
        }



        setContent {
            RoadSOSTheme {
                AppNavigation()
            }
        }
    }
}