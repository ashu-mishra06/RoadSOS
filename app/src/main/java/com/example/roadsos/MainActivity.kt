package com.example.roadsos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.roadsos.navigation.AppNavigation
import com.example.roadsos.ui.theme.RoadSOSTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RoadSOSTheme {
                AppNavigation()
            }
        }
    }
}