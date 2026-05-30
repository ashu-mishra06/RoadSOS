package com.example.roadsos.ui.screens.home

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.animation.animateColorAsState
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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
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
import com.example.roadsos.utils.AppText
import com.example.roadsos.utils.EmergencyActionStatusManager
import com.example.roadsos.utils.EmergencyAlertManager
import com.example.roadsos.utils.EmergencyCallManager
import com.example.roadsos.utils.roadSOSThemeColors
import com.example.roadsos.viewmodel.AppSettingsViewModel
import com.example.roadsos.viewmodel.EmergencyEventManager
import com.example.roadsos.viewmodel.EmergencyViewModel
import com.example.roadsos.viewmodel.LocationViewModel
import com.example.roadsos.viewmodel.UserProfileViewModel
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    navController: NavController,
    emergencyViewModel: EmergencyViewModel
) {
    val context =
        LocalContext.current

    val locationViewModel: LocationViewModel =
        viewModel()

    val appSettingsViewModel: AppSettingsViewModel =
        viewModel()

    val userProfileViewModel: UserProfileViewModel =
        viewModel()

    val appSettings by
    appSettingsViewModel.appSettings.collectAsState()

    val userProfile by
    userProfileViewModel.userProfile.collectAsState()

    val location by
    locationViewModel.location.collectAsState()

    val emergencyState by
    emergencyViewModel.state.collectAsState()

    val crashEventPending by
    EmergencyEventManager.crashEventPending.collectAsState()

    val emergencyActionStatus by
    EmergencyActionStatusManager.status.collectAsState()

    val language =
        appSettings.languageCode

    val themeColors =
        roadSOSThemeColors(appSettings.isDarkMode)

    val isEmergencyMode =
        emergencyState.emergencyTriggered

    val backgroundTop by animateColorAsState(
        targetValue =
            if (isEmergencyMode) {
                if (appSettings.isDarkMode) {
                    Color(0xFF3B0505)
                } else {
                    Color(0xFFFFE4E6)
                }
            } else {
                themeColors.backgroundTop
            },
        animationSpec = tween(260),
        label = "homeBackgroundTop"
    )

    val backgroundMiddle by animateColorAsState(
        targetValue =
            if (isEmergencyMode) {
                if (appSettings.isDarkMode) {
                    Color(0xFF7F1D1D)
                } else {
                    Color(0xFFFFF1F2)
                }
            } else {
                themeColors.backgroundMiddle
            },
        animationSpec = tween(260),
        label = "homeBackgroundMiddle"
    )

    val backgroundBottom by animateColorAsState(
        targetValue =
            if (isEmergencyMode) {
                if (appSettings.isDarkMode) {
                    Color(0xFF111827)
                } else {
                    Color(0xFFFFFFFF)
                }
            } else {
                themeColors.backgroundBottom
            },
        animationSpec = tween(260),
        label = "homeBackgroundBottom"
    )

    val textPrimary by animateColorAsState(
        targetValue = themeColors.textPrimary,
        animationSpec = tween(260),
        label = "homeTextPrimary"
    )

    val textSecondary by animateColorAsState(
        targetValue = themeColors.textSecondary,
        animationSpec = tween(260),
        label = "homeTextSecondary"
    )

    val cardColor by animateColorAsState(
        targetValue = themeColors.card,
        animationSpec = tween(260),
        label = "homeCardColor"
    )

    LaunchedEffect(Unit) {
        locationViewModel.fetchLocation()
    }

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

    var emergencyActionsExecuted by rememberSaveable {
        mutableStateOf(false)
    }

    LaunchedEffect(
        emergencyState.emergencyTriggered,
        userProfile,
        location,
        appSettings.isAutoEmergencyCallEnabled
    ) {

        if (
            emergencyState.emergencyTriggered &&
            !emergencyActionsExecuted
        ) {
            emergencyActionsExecuted = true

            EmergencyActionStatusManager.reset()

            EmergencyAlertManager.sendEmergencySmsToSavedContacts(
                context = context,
                userName = userProfile.name,
                bloodGroup = userProfile.bloodGroup,
                emergencyContacts = userProfile.getAllEmergencyContacts(),
                latitude = location?.first,
                longitude = location?.second
            )

            EmergencyCallManager.callEmergencyServiceIfEnabled(
                context = context,
                isAutoCallEnabled = appSettings.isAutoEmergencyCallEnabled
            )
        }

        if (!emergencyState.emergencyTriggered) {
            emergencyActionsExecuted = false
            EmergencyActionStatusManager.reset()
        }
    }

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
        Brush.verticalGradient(
            listOf(
                backgroundTop,
                backgroundMiddle,
                backgroundBottom
            )
        )

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
                        language = language,
                        isEmergencyMode = isEmergencyMode,
                        isCrashDetected = emergencyState.isCrashDetected,
                        textPrimary = textPrimary,
                        textSecondary = textSecondary
                    )

                    Spacer(modifier = Modifier.height(22.dp))

                    MinimalProtectionStatusCard(
                        language = language,
                        isEmergencyMode = isEmergencyMode,
                        isCrashDetected = emergencyState.isCrashDetected,
                        allPermissionsGranted = allPermissionsGranted,
                        textPrimary = textPrimary,
                        textSecondary = textSecondary,
                        cardColor = cardColor
                    )

                    Spacer(modifier = Modifier.height(22.dp))

                    MinimalLocationCard(
                        language = language,
                        locationText =
                            location?.let {
                                "Lat: ${it.first}\nLon: ${it.second}"
                            } ?: AppText.t(
                                language,
                                "fetching_location"
                            ),
                        textPrimary = textPrimary,
                        textSecondary = textSecondary
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    CrashStatusCard(
                        language = language,
                        isDarkMode = appSettings.isDarkMode,
                        isEmergencyMode = isEmergencyMode,
                        isCrashDetected = emergencyState.isCrashDetected,
                        textPrimary = textPrimary,
                        textSecondary = textSecondary
                    )

                    if (isEmergencyMode) {

                        Spacer(modifier = Modifier.height(24.dp))

                        EmergencyActionsCard(
                            language = language,
                            autoCallEnabled = appSettings.isAutoEmergencyCallEnabled,
                            smsStatus = emergencyActionStatus.smsStatus,
                            callStatus = emergencyActionStatus.callStatus,
                            smsSentCount = emergencyActionStatus.smsSentCount,
                            smsFailedCount = emergencyActionStatus.smsFailedCount
                        )
                    }
                }

                FloatingSOSButton(
                    language = language,
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
    language: String,
    isEmergencyMode: Boolean,
    isCrashDetected: Boolean,
    textPrimary: Color,
    textSecondary: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = AppText.t(
                    language,
                    "app_name"
                ),
                style = MaterialTheme.typography.headlineLarge,
                color = textPrimary,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text =
                    when {
                        isEmergencyMode ->
                            AppText.t(
                                language,
                                "emergency_response_active"
                            )

                        isCrashDetected ->
                            AppText.t(
                                language,
                                "crash_countdown_running"
                            )

                        else ->
                            AppText.t(
                                language,
                                "home_subtitle"
                            )
                    },
                color =
                    if (isEmergencyMode || isCrashDetected) {
                        Color(0xFFFF6B6B)
                    } else {
                        textSecondary
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
    language: String,
    isEmergencyMode: Boolean,
    isCrashDetected: Boolean,
    allPermissionsGranted: Boolean,
    textPrimary: Color,
    textSecondary: Color,
    cardColor: Color
) {
    val statusText =
        when {
            isEmergencyMode ->
                AppText.t(
                    language,
                    "emergency_sos_triggered"
                )

            isCrashDetected ->
                AppText.t(
                    language,
                    "crash_detected"
                )

            allPermissionsGranted ->
                AppText.t(
                    language,
                    "active"
                )

            else ->
                AppText.t(
                    language,
                    "permission_needed"
                )
        }

    val subtitleText =
        when {
            isEmergencyMode ->
                AppText.t(
                    language,
                    "emergency_response_active"
                )

            isCrashDetected ->
                AppText.t(
                    language,
                    "crash_countdown_running"
                )

            allPermissionsGranted ->
                AppText.t(
                    language,
                    "all_systems_working"
                )

            else ->
                AppText.t(
                    language,
                    "open_settings_grant_access"
                )
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
            containerColor = cardColor
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
                    text = AppText.t(
                        language,
                        "protection_status"
                    ),
                    color = textSecondary,
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = statusText,
                    color = textPrimary,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = subtitleText,
                color = textSecondary,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun MinimalLocationCard(
    language: String,
    locationText: String,
    textPrimary: Color,
    textSecondary: Color
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
                text = AppText.t(
                    language,
                    "current_location"
                ),
                color = textPrimary,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = locationText,
                color = textSecondary,
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
    language: String,
    isDarkMode: Boolean,
    isEmergencyMode: Boolean,
    isCrashDetected: Boolean,
    textPrimary: Color,
    textSecondary: Color
) {
    val cardBackground =
        when {
            isEmergencyMode ->
                if (isDarkMode) {
                    Color(0xFF5A1111)
                } else {
                    Color(0xFFFFE4E6)
                }

            isDarkMode ->
                Color(0xFF241A1A)

            else ->
                Color.White.copy(alpha = 0.94f)
        }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = cardBackground
        ),
        shape = RoundedCornerShape(28.dp)
    ) {

        Column(
            modifier = Modifier.padding(22.dp)
        ) {

            Text(
                text = AppText.t(
                    language,
                    "crash_detection"
                ),
                color = textPrimary,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text =
                    when {
                        isEmergencyMode ->
                            AppText.t(
                                language,
                                "emergency_sos_triggered"
                            )

                        isCrashDetected ->
                            AppText.t(
                                language,
                                "crash_detected"
                            )

                        else ->
                            AppText.t(
                                language,
                                "no_crash_detected"
                            )
                    },
                color =
                    when {
                        isEmergencyMode ->
                            Color(0xFFFF1744)

                        isCrashDetected ->
                            Color(0xFFF97316)

                        else ->
                            Color(0xFF22C55E)
                    },
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text =
                    when {
                        isEmergencyMode ->
                            AppText.t(
                                language,
                                "emergency_alert_active"
                            )

                        isCrashDetected ->
                            AppText.t(
                                language,
                                "countdown_warning"
                            )

                        else ->
                            AppText.t(
                                language,
                                "listening_ai"
                            )
                    },
                color = textSecondary
            )
        }
    }
}

@Composable
private fun EmergencyActionsCard(
    language: String,
    autoCallEnabled: Boolean,
    smsStatus: String,
    callStatus: String,
    smsSentCount: Int,
    smsFailedCount: Int
) {
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
                text = AppText.t(
                    language,
                    "emergency_actions_started"
                ),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = AppText.t(
                    language,
                    "emergency_actions_desc"
                ),
                color = Color(0xFFFFCDD2)
            )

            Spacer(modifier = Modifier.height(14.dp))

            EmergencyStatusRow(
                label = "SMS Status",
                value = smsStatus,
                positive = smsSentCount > 0
            )

            Spacer(modifier = Modifier.height(8.dp))

            EmergencyStatusRow(
                label = "SMS Count",
                value = "Sent: $smsSentCount | Failed: $smsFailedCount",
                positive = smsSentCount > 0 && smsFailedCount == 0
            )

            Spacer(modifier = Modifier.height(8.dp))

            EmergencyStatusRow(
                label = "Call Status",
                value = callStatus,
                positive = autoCallEnabled && callStatus.contains(
                    "started",
                    ignoreCase = true
                )
            )
        }
    }
}

@Composable
private fun EmergencyStatusRow(
    label: String,
    value: String,
    positive: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White.copy(alpha = 0.08f))
            .padding(
                horizontal = 12.dp,
                vertical = 10.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(
                    if (positive) {
                        Color(0xFF22C55E)
                    } else {
                        Color(0xFFFFB020)
                    }
                )
        )

        Spacer(modifier = Modifier.width(10.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = label,
                color = Color(0xFFFFCDD2),
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = value,
                color = Color.White,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun FloatingSOSButton(
    language: String,
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
            isEmergencyMode ->
                AppText.t(
                    language,
                    "return_normal"
                )

            isCrashDetected ->
                AppText.t(
                    language,
                    "cancel_countdown"
                )

            else ->
                AppText.t(
                    language,
                    "emergency_sos"
                )
        }

    val subtitleText =
        when {
            isEmergencyMode ->
                AppText.t(
                    language,
                    "tap_reset"
                )

            isCrashDetected ->
                AppText.t(
                    language,
                    "tap_cancel"
                )

            else ->
                AppText.t(
                    language,
                    "roadsos_ready"
                )
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
                cornerRadius = CornerRadius(
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
                                .background(
                                    Color.White.copy(
                                        alpha = glowPulse.coerceIn(
                                            0.75f,
                                            1f
                                        )
                                    )
                                )
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
            Manifest.permission.SEND_SMS,
            Manifest.permission.CALL_PHONE
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