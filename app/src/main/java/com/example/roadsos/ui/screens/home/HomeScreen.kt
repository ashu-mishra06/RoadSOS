package com.example.roadsos.ui.screens.home

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.roadsos.ui.components.BottomNavBar
import com.example.roadsos.ui.components.EmergencyOverlay
import com.example.roadsos.viewmodel.EmergencyEventManager
import com.example.roadsos.viewmodel.EmergencyViewModel
import com.example.roadsos.viewmodel.LocationViewModel
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    navController: NavController
) {
    val context =
        LocalContext.current

    val locationViewModel: LocationViewModel =
        viewModel()

    val emergencyViewModel: EmergencyViewModel =
        viewModel()

    LaunchedEffect(Unit) {
        locationViewModel.fetchLocation()
    }

    val location by
    locationViewModel.location.collectAsState()

    val emergencyState by
    emergencyViewModel.state.collectAsState()

    val crashEventPending by
    EmergencyEventManager.crashEventPending.collectAsState()

    val allPermissionsGranted =
        remember {
            areCoreRoadSOSPermissionsGranted(context)
        }

    LaunchedEffect(crashEventPending) {

        if (crashEventPending) {

            if (
                !emergencyState.isCrashDetected &&
                !emergencyState.emergencyTriggered
            ) {
                emergencyViewModel.triggerCrash()
            }

            EmergencyEventManager.markCrashEventHandled()
        }
    }

    val isEmergencyMode =
        emergencyState.emergencyTriggered

    var sosPressed by remember {
        mutableStateOf(false)
    }

    val sosScale by animateFloatAsState(
        targetValue = if (sosPressed) 0.95f else 1f,
        animationSpec = spring(),
        label = "sosScale"
    )

    LaunchedEffect(sosPressed) {
        if (sosPressed) {
            delay(120)
            sosPressed = false
        }
    }

    val backgroundBrush =
        if (isEmergencyMode) {
            Brush.verticalGradient(
                listOf(
                    Color(0xFF3B0505),
                    Color(0xFF7F1D1D),
                    Color(0xFF111827)
                )
            )
        } else {
            Brush.verticalGradient(
                listOf(
                    Color(0xFF07111F),
                    Color(0xFF0B1220),
                    Color(0xFF111827)
                )
            )
        }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
            bottomBar = {
                BottomNavBar(navController)
            },
            containerColor = Color.Transparent
        ) { padding ->

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundBrush)
                    .padding(padding)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp)
                        .padding(top = 20.dp)
                        .padding(bottom = 160.dp)
                ) {

                    HomeHeader(
                        isEmergencyMode = isEmergencyMode,
                        isCrashDetected = emergencyState.isCrashDetected
                    )

                    Spacer(modifier = Modifier.height(22.dp))

                    MinimalProtectionStatusCard(
                        isEmergencyMode = isEmergencyMode,
                        isCrashDetected = emergencyState.isCrashDetected,
                        allPermissionsGranted = allPermissionsGranted
                    )

                    Spacer(modifier = Modifier.height(22.dp))

                    MinimalLocationCard(
                        locationText =
                            location?.let {
                                "Lat: ${it.first}\nLon: ${it.second}"
                            } ?: "Fetching GPS location..."
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    CrashStatusCard(
                        isEmergencyMode = isEmergencyMode,
                        isCrashDetected = emergencyState.isCrashDetected
                    )

                    if (isEmergencyMode) {

                        Spacer(modifier = Modifier.height(24.dp))

                        EmergencyActionsCard()
                    }
                }

                FloatingSOSButton(
                    isEmergencyMode = isEmergencyMode,
                    isCrashDetected = emergencyState.isCrashDetected,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 18.dp)
                        .scale(sosScale),
                    onClick = {
                        sosPressed = true

                        when {
                            emergencyState.emergencyTriggered -> {
                                emergencyViewModel.cancelEmergency()
                            }

                            emergencyState.isCrashDetected -> {
                                emergencyViewModel.cancelEmergency()
                            }

                            else -> {
                                emergencyViewModel.triggerCrash()
                            }
                        }
                    }
                )
            }
        }

        if (emergencyState.isCrashDetected) {
            EmergencyOverlay(
                countdown = emergencyState.countdown,
                onCancel = {
                    emergencyViewModel.cancelEmergency()
                }
            )
        }
    }
}

@Composable
private fun HomeHeader(
    isEmergencyMode: Boolean,
    isCrashDetected: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = "RoadSOS",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text =
                    when {
                        isEmergencyMode ->
                            "Emergency response active"

                        isCrashDetected ->
                            "Crash countdown running"

                        else ->
                            "Offline AI Crash Detection"
                    },
                color =
                    if (isEmergencyMode || isCrashDetected) {
                        Color(0xFFFFCDD2)
                    } else {
                        Color.LightGray
                    }
            )
        }

        if (isEmergencyMode) {

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .background(Color(0xFFFF1744))
                    .padding(
                        horizontal = 12.dp,
                        vertical = 7.dp
                    )
            ) {

                Text(
                    text = "SOS ACTIVE",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun MinimalProtectionStatusCard(
    isEmergencyMode: Boolean,
    isCrashDetected: Boolean,
    allPermissionsGranted: Boolean
) {
    val statusText =
        when {
            isEmergencyMode -> "Emergency Active"
            isCrashDetected -> "Countdown Active"
            allPermissionsGranted -> "Active"
            else -> "Permission Needed"
        }

    val subtitleText =
        when {
            isEmergencyMode -> "RoadSOS response is running"
            isCrashDetected -> "Safety countdown is in progress"
            allPermissionsGranted -> "All systems working properly"
            else -> "Open settings to grant access"
        }

    val dotColor =
        when {
            isEmergencyMode -> Color(0xFFFF1744)
            isCrashDetected -> Color(0xFFF97316)
            allPermissionsGranted -> Color(0xFF22C55E)
            else -> Color(0xFFFF1744)
        }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.07f)
        ),
        shape = RoundedCornerShape(22.dp)
    ) {

        Row(
            modifier = Modifier.padding(
                horizontal = 18.dp,
                vertical = 15.dp
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(dotColor)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = "Protection Status",
                    color = Color.LightGray,
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = statusText,
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = subtitleText,
                color = Color.LightGray,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun MinimalLocationCard(
    locationText: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(Color(0xFF22C55E).copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = "📍",
                style = MaterialTheme.typography.headlineMedium
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = "Current Location",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = locationText,
                color = Color.LightGray,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = null,
            tint = Color(0xFF22C55E),
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
private fun CrashStatusCard(
    isEmergencyMode: Boolean,
    isCrashDetected: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor =
                if (isEmergencyMode) {
                    Color(0xFF5A1111)
                } else {
                    Color(0xFF241A1A)
                }
        ),
        shape = RoundedCornerShape(28.dp)
    ) {
        Column(
            modifier = Modifier.padding(22.dp)
        ) {

            Text(
                text = "Crash Detection",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = when {
                    isEmergencyMode ->
                        "Emergency SOS Triggered"

                    isCrashDetected ->
                        "Crash Detected"

                    else ->
                        "No crash detected"
                },
                color = when {
                    isEmergencyMode ->
                        Color(0xFFFFCDD2)

                    isCrashDetected ->
                        Color(0xFFF97316)

                    else ->
                        Color.Green
                },
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text =
                    when {
                        isEmergencyMode ->
                            "Emergency alert flow is active. Nearest services can be checked on the map."

                        isCrashDetected ->
                            "Safety countdown is running. Cancel if this is a false alarm."

                        else ->
                            "RoadSOS is listening locally using on-device AI."
                    },
                color = Color.LightGray
            )
        }
    }
}

@Composable
private fun EmergencyActionsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF7F1D1D)
        ),
        shape = RoundedCornerShape(24.dp)
    ) {

        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            Text(
                text = "Emergency Actions Started",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "RoadSOS has prepared your emergency alert, current location and nearest emergency services.",
                color = Color(0xFFFFCDD2)
            )
        }
    }
}

@Composable
private fun FloatingSOSButton(
    isEmergencyMode: Boolean,
    isCrashDetected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val infiniteTransition =
        rememberInfiniteTransition(label = "sosButtonAnimation")

    val glowPulse by
    infiniteTransition.animateFloat(
        initialValue = 0.75f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(1100),
            repeatMode = RepeatMode.Reverse
        ),
        label = "sosGlowPulse"
    )

    val ringPulse by
    infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1450),
            repeatMode = RepeatMode.Restart
        ),
        label = "sosRingPulse"
    )

    val buttonText =
        when {
            isEmergencyMode -> "RETURN TO NORMAL"
            isCrashDetected -> "CANCEL COUNTDOWN"
            else -> "EMERGENCY SOS"
        }

    val subtitleText =
        when {
            isEmergencyMode -> "Tap to reset emergency mode"
            isCrashDetected -> "Tap to cancel safety countdown"
            else -> "Hold steady. RoadSOS is ready."
        }

    val mainColor =
        when {
            isEmergencyMode -> Color(0xFF16A34A)
            isCrashDetected -> Color(0xFFF97316)
            else -> Color(0xFFFF1744)
        }

    val secondaryColor =
        when {
            isEmergencyMode -> Color(0xFF22C55E)
            isCrashDetected -> Color(0xFFFFB020)
            else -> Color(0xFFFF6B6B)
        }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(112.dp),
        contentAlignment = Alignment.Center
    ) {

        Canvas(
            modifier = Modifier.matchParentSize()
        ) {
            val center =
                Offset(
                    x = size.width / 2f,
                    y = size.height / 2f
                )

            drawRoundRect(
                brush = Brush.horizontalGradient(
                    listOf(
                        mainColor.copy(alpha = 0.25f * glowPulse),
                        secondaryColor.copy(alpha = 0.38f * glowPulse),
                        mainColor.copy(alpha = 0.25f * glowPulse)
                    )
                ),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(
                    x = 36.dp.toPx(),
                    y = 36.dp.toPx()
                )
            )

            drawCircle(
                color = Color.White.copy(alpha = 0.06f * (1f - ringPulse)),
                radius = 170f * ringPulse,
                center = center,
                style = Stroke(width = 5f)
            )

            drawCircle(
                color = Color.White.copy(alpha = 0.04f * (1f - ringPulse)),
                radius = 230f * ringPulse,
                center = center,
                style = Stroke(width = 4f)
            )
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(104.dp)
                .border(
                    width = 2.dp,
                    color = Color.White.copy(alpha = 0.70f),
                    shape = RoundedCornerShape(34.dp)
                ),
            shape = RoundedCornerShape(34.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 18.dp
            )
        ) {

            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(34.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                ),
                contentPadding = PaddingValues(0.dp)
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    mainColor,
                                    secondaryColor,
                                    mainColor
                                )
                            )
                        )
                        .padding(horizontal = 18.dp),
                    contentAlignment = Alignment.Center
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.18f)),
                            contentAlignment = Alignment.Center
                        ) {

                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(36.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {

                            Text(
                                text = buttonText,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = subtitleText,
                                color = Color.White.copy(alpha = 0.90f),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = glowPulse.coerceIn(0.75f, 1f)))
                        )
                    }
                }
            }
        }
    }
}

private fun areCoreRoadSOSPermissionsGranted(
    context: Context
): Boolean {
    val requiredPermissions =
        mutableListOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.SEND_SMS
        )

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        requiredPermissions.add(
            Manifest.permission.POST_NOTIFICATIONS
        )
    }

    return requiredPermissions.all { permission ->

        ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }
}