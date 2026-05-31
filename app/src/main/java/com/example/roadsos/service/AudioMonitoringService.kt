package com.example.roadsos.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.roadsos.MainActivity
import com.example.roadsos.R
import com.example.roadsos.ml.TensorflowHelper
import com.example.roadsos.utils.AudioRecorderManager
import com.example.roadsos.viewmodel.EmergencyEventManager

class AudioMonitoringService : Service() {

    private lateinit var audioRecorderManager:
            AudioRecorderManager

    private lateinit var tensorflowHelper:
            TensorflowHelper

    private var highScoreCount = 0

    private var emergencyAlreadyTriggered = false

    private val crashThreshold = 0.80f

    private val requiredConsecutiveDetections = 3

    override fun onCreate() {
        super.onCreate()

        createNotificationChannel()

        val notification: Notification =
            NotificationCompat.Builder(
                this,
                "roadsos_channel"
            )
                .setContentTitle("RoadSOS Active")
                .setContentText("Monitoring crash sounds...")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build()

        startForeground(1, notification)

        audioRecorderManager =
            AudioRecorderManager()

        tensorflowHelper =
            TensorflowHelper(this)

        startAudioMonitoring()
    }

    private fun startAudioMonitoring() {

        audioRecorderManager.startRecording { audioData ->

            val score =
                tensorflowHelper.analyzeAudio(audioData)

            Log.d(
                "CRASH_SCORE",
                score.toString()
            )

            handleCrashScore(score)
        }
    }

    private fun handleCrashScore(score: Float) {

        if (emergencyAlreadyTriggered) {
            return
        }

        if (score >= crashThreshold) {

            highScoreCount++

            Log.d(
                "CRASH_FILTER",
                "High score count: $highScoreCount"
            )

        } else {

            if (highScoreCount > 0) {
                highScoreCount--
            }
        }

        if (highScoreCount >= requiredConsecutiveDetections) {

            emergencyAlreadyTriggered = true

            Log.d(
                "ACCIDENT_CONFIRMED",
                "Crash confirmed by model"
            )

            triggerAutomaticEmergency()
        }
    }

    private fun triggerAutomaticEmergency() {

        acquireShortWakeLock()

        EmergencyEventManager.triggerCrashEvent()

        showCrashNotification()

        openMainActivityForEmergency()
    }

    private fun acquireShortWakeLock() {

        try {

            val powerManager =
                getSystemService(Context.POWER_SERVICE)
                        as PowerManager

            val wakeLock =
                powerManager.newWakeLock(
                    PowerManager.PARTIAL_WAKE_LOCK,
                    "RoadSOS:CrashWakeLock"
                )

            wakeLock.acquire(10_000)

            Log.d(
                "WAKE_LOCK",
                "Short wake lock acquired"
            )

        } catch (e: Exception) {

            Log.e(
                "WAKE_LOCK",
                "Wake lock failed: ${e.message}"
            )
        }
    }

    private fun openMainActivityForEmergency() {

        try {

            val intent =
                Intent(
                    this,
                    MainActivity::class.java
                ).apply {
                    flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or
                                Intent.FLAG_ACTIVITY_CLEAR_TOP or
                                Intent.FLAG_ACTIVITY_SINGLE_TOP

                    putExtra(
                        "CRASH_TRIGGERED",
                        true
                    )
                }

            startActivity(intent)

            Log.d(
                "EMERGENCY_UI",
                "MainActivity launch attempted"
            )

        } catch (e: Exception) {

            Log.e(
                "EMERGENCY_UI",
                "Failed to launch activity: ${e.message}"
            )
        }
    }

    private fun showCrashNotification() {

        val intent =
            Intent(
                this,
                MainActivity::class.java
            ).apply {
                flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or
                            Intent.FLAG_ACTIVITY_CLEAR_TOP

                putExtra(
                    "CRASH_TRIGGERED",
                    true
                )
            }

        val pendingIntent =
            PendingIntent.getActivity(
                this,
                200,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or
                        PendingIntent.FLAG_IMMUTABLE
            )

        val notification =
            NotificationCompat.Builder(
                this,
                "roadsos_emergency_channel"
            )
                .setContentTitle("Possible Crash Detected")
                .setContentText("RoadSOS emergency countdown is starting.")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setFullScreenIntent(pendingIntent, true)
                .build()

        val manager =
            getSystemService(
                NotificationManager::class.java
            )

        manager.notify(
            911,
            notification
        )
    }

    override fun onDestroy() {

        super.onDestroy()

        audioRecorderManager.stopRecording()
    }

    override fun onBind(intent: Intent?): IBinder? {

        return null
    }

    private fun createNotificationChannel() {

        if (
            Build.VERSION.SDK_INT >=
            Build.VERSION_CODES.O
        ) {

            val monitoringChannel =
                NotificationChannel(
                    "roadsos_channel",
                    "RoadSOS Monitoring",
                    NotificationManager.IMPORTANCE_LOW
                )

            val emergencyChannel =
                NotificationChannel(
                    "roadsos_emergency_channel",
                    "RoadSOS Emergency Alerts",
                    NotificationManager.IMPORTANCE_HIGH
                )

            emergencyChannel.description =
                "Crash detection emergency alerts"

            val manager =
                getSystemService(
                    NotificationManager::class.java
                )

            manager.createNotificationChannel(
                monitoringChannel
            )

            manager.createNotificationChannel(
                emergencyChannel
            )
        }
    }
}