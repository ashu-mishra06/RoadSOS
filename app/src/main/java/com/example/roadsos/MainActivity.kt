package com.example.roadsos

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
//import android.util.Log

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts

import androidx.core.content.ContextCompat

import com.example.roadsos.navigation.AppNavigation
import com.example.roadsos.services.AudioMonitoringService
import com.example.roadsos.ui.theme.RoadSOSTheme
//import com.example.roadsos.utils.AudioRecorderManager

//import com.example.roadsos.ml.TensorflowHelper
import com.example.roadsos.utils.DatabaseTestHelper
import androidx.lifecycle.lifecycleScope
import com.example.roadsos.viewmodel.EmergencyEventManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
class MainActivity : ComponentActivity() {

//    private lateinit var tensorflowHelper:
//            TensorflowHelper
//
//    private lateinit var audioRecorderManager:
//            AudioRecorderManager

    // --------------------------------
    // MULTIPLE PERMISSION LAUNCHER
    // --------------------------------

    private val permissionLauncher =

        registerForActivityResult(

            ActivityResultContracts
                .RequestMultiplePermissions()

        ) { permissions ->

            val audioGranted =

                permissions[
                    Manifest.permission.RECORD_AUDIO
                ] ?: false

            if (audioGranted) {

                startAudioMonitoringService()
            }
        }

    // --------------------------------
    // NOTIFICATION PERMISSION
    // --------------------------------

    private val notificationPermissionLauncher =

        registerForActivityResult(

            ActivityResultContracts
                .RequestPermission()

        ) { }

    private val callPermissionLauncher =

        registerForActivityResult(

            ActivityResultContracts.RequestPermission()

        ) { }

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        window.setBackgroundDrawableResource(android.R.color.transparent)
        window.statusBarColor = android.graphics.Color.rgb(7, 17, 31)
        window.navigationBarColor = android.graphics.Color.rgb(7, 17, 31)

        if (
            intent?.getBooleanExtra(
                "CRASH_TRIGGERED",
                false
            ) == true
        ) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
                setShowWhenLocked(true)
                setTurnScreenOn(true)
            }
        }

        // --------------------------------
        // INIT ML + AUDIO
        // --------------------------------
//
//        tensorflowHelper =
//            TensorflowHelper(this)
//
//        audioRecorderManager =
//            AudioRecorderManager()

        // --------------------------------
        // REQUEST PERMISSIONS
        // --------------------------------

        permissionLauncher.launch(

            arrayOf(

                Manifest.permission.RECORD_AUDIO,

                Manifest.permission.ACCESS_FINE_LOCATION,

                Manifest.permission.ACCESS_COARSE_LOCATION,

                Manifest.permission.SEND_SMS
            )
        )

        callPermissionLauncher.launch(
            Manifest.permission.CALL_PHONE
        )

        // --------------------------------
        // NOTIFICATION PERMISSION
        // --------------------------------

        if (

            Build.VERSION.SDK_INT >=
            Build.VERSION_CODES.TIRAMISU

        ) {

            notificationPermissionLauncher.launch(

                Manifest.permission.POST_NOTIFICATIONS
            )
        }

        // --------------------------------
        // START FOREGROUND SERVICE
        // --------------------------------

//        try {
//
//            val intent = Intent(
//
//                this,
//
//                AudioMonitoringService::class.java
//            )
//
//            ContextCompat.startForegroundService(
//
//                this,
//
//                intent
//            )
//
//        } catch (e: Exception) {
//
//            e.printStackTrace()
//        }

//        DatabaseTestHelper.testEmergencyDatabase(this)

        // --------------------------------
        // UI
        // --------------------------------

        setContent {

            RoadSOSTheme {

                AppNavigation()
            }
        }

        if (
            intent?.getBooleanExtra(
                "CRASH_TRIGGERED",
                false
            ) == true
        ) {

            lifecycleScope.launch {

                delay(700)

                EmergencyEventManager.triggerCrashEvent()
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        setIntent(intent)

        if (
            intent.getBooleanExtra(
                "CRASH_TRIGGERED",
                false
            )
        ) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
                setShowWhenLocked(true)
                setTurnScreenOn(true)
            }

            lifecycleScope.launch {

                delay(500)

                EmergencyEventManager.triggerCrashEvent()
            }
        }
    }

    // --------------------------------
    // AUDIO MONITORING
    // --------------------------------

//    private fun startAudioMonitoring() {
//
//        audioRecorderManager.startRecording {
//
//                audioData ->
//
//            val score =
//
//                tensorflowHelper
//                    .analyzeAudio(audioData)
//
//            if (score > 0.8f) {
//
//                Log.d(
//
//                    "ACCIDENT_CONFIRMED",
//
//                    "Crash detected!"
//                )
//            }
//        }
//    }
//
    private fun startAudioMonitoringService() {

    try {

        val serviceIntent =
            Intent(
                this,
                AudioMonitoringService::class.java
            )

        ContextCompat.startForegroundService(
            this,
            serviceIntent
        )

    } catch (e: Exception) {

        e.printStackTrace()
    }

}

}