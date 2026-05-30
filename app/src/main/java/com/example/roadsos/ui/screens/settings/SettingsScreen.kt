package com.example.roadsos.ui.screens.settings

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.LocalPhone
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.roadsos.ui.components.BottomNavBar
import com.example.roadsos.utils.AppText
import com.example.roadsos.utils.roadSOSThemeColors
import com.example.roadsos.viewmodel.AppSettingsViewModel
import com.example.roadsos.viewmodel.UserProfileViewModel

@Composable
fun SettingsScreen(
    navController: NavController
) {
    val context =
        LocalContext.current

    val userProfileViewModel: UserProfileViewModel =
        viewModel()

    val appSettingsViewModel: AppSettingsViewModel =
        viewModel()

    val appSettings by
    appSettingsViewModel.appSettings.collectAsState()

    val themeColors =
        roadSOSThemeColors(appSettings.isDarkMode)

    val topColor by animateColorAsState(
        targetValue = themeColors.backgroundTop,
        animationSpec = tween(260),
        label = "settingsTop"
    )

    val middleColor by animateColorAsState(
        targetValue = themeColors.backgroundMiddle,
        animationSpec = tween(260),
        label = "settingsMiddle"
    )

    val bottomColor by animateColorAsState(
        targetValue = themeColors.backgroundBottom,
        animationSpec = tween(260),
        label = "settingsBottom"
    )

    val cardColor by animateColorAsState(
        targetValue = themeColors.card,
        animationSpec = tween(260),
        label = "settingsCard"
    )

    val textPrimary by animateColorAsState(
        targetValue = themeColors.textPrimary,
        animationSpec = tween(260),
        label = "settingsTextPrimary"
    )

    val textSecondary by animateColorAsState(
        targetValue = themeColors.textSecondary,
        animationSpec = tween(260),
        label = "settingsTextSecondary"
    )

    val permissionStatus =
        remember {
            getRoadSOSPermissionStatus(context)
        }

    var showLogoutDialog by remember {
        mutableStateOf(false)
    }

    var showAutoCallOffWarning by remember {
        mutableStateOf(false)
    }

    val allImportantPermissionsGranted =
        permissionStatus.all { it.isGranted }

    val backgroundBrush =
        Brush.verticalGradient(
            listOf(
                topColor,
                middleColor,
                bottomColor
            )
        )

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
                    .padding(22.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            text = AppText.t(
                                appSettings.languageCode,
                                "settings"
                            ),
                            color = textPrimary,
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = AppText.t(
                                appSettings.languageCode,
                                "manage_safety_profile"
                            ),
                            color = textSecondary,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    AnimatedModeToggle(
                        isDarkMode = appSettings.isDarkMode,
                        onToggle = {
                            appSettingsViewModel.setDarkMode(!appSettings.isDarkMode)
                        }
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                SettingsMainCard(
                    title = AppText.t(
                        appSettings.languageCode,
                        "profile"
                    ),
                    subtitle = "View your medical and emergency information",
                    icon = Icons.Default.Person,
                    iconColor = Color(0xFF38BDF8),
                    mainTextColor = textPrimary,
                    subTextColor = textSecondary,
                    cardColor = cardColor,
                    onClick = {
                        navController.navigate("profile_view")
                    }
                )

                Spacer(modifier = Modifier.height(18.dp))

                AutoEmergencyCallCard(
                    isEnabled = appSettings.isAutoEmergencyCallEnabled,
                    isDarkMode = appSettings.isDarkMode,
                    textPrimary = textPrimary,
                    textSecondary = textSecondary,
                    cardColor = cardColor,
                    onToggle = { newValue ->
                        if (newValue) {
                            appSettingsViewModel.setAutoEmergencyCallEnabled(true)
                        } else {
                            showAutoCallOffWarning = true
                        }
                    }
                )

                Spacer(modifier = Modifier.height(18.dp))

                PermissionManageCard(
                    allGranted = allImportantPermissionsGranted,
                    permissionStatus = permissionStatus,
                    isDarkMode = appSettings.isDarkMode
                )

                Spacer(modifier = Modifier.height(18.dp))

                LogoutCard(
                    title = AppText.t(
                        appSettings.languageCode,
                        "logout"
                    ),
                    isDarkMode = appSettings.isDarkMode,
                    onClick = {
                        showLogoutDialog = true
                    }
                )

                Spacer(modifier = Modifier.height(95.dp))
            }
        }
    }

    if (showAutoCallOffWarning) {

        AlertDialog(
            onDismissRequest = {
                showAutoCallOffWarning = false
            },
            containerColor =
                if (appSettings.isDarkMode) {
                    Color(0xFF111827)
                } else {
                    Color.White
                },
            title = {
                Text(
                    text = "Turn off auto emergency call?",
                    color = textPrimary,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "Warning: RoadSOS will not automatically call 112 after emergency countdown. Emergency SMS will still be sent to your saved contacts.",
                    color = textSecondary
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        appSettingsViewModel.setAutoEmergencyCallEnabled(false)
                        showAutoCallOffWarning = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFDC2626),
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Turn Off",
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        showAutoCallOffWarning = false
                    }
                ) {
                    Text(
                        text = "Keep On",
                        color = textPrimary
                    )
                }
            }
        )
    }

    if (showLogoutDialog) {

        AlertDialog(
            onDismissRequest = {
                showLogoutDialog = false
            },
            containerColor =
                if (appSettings.isDarkMode) {
                    Color(0xFF111827)
                } else {
                    Color.White
                },
            title = {
                Text(
                    text = "Clear RoadSOS Data?",
                    color = textPrimary,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "This will erase your saved profile information from this app. You will need to enter your details again.",
                    color = textSecondary
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        userProfileViewModel.clearProfile()

                        showLogoutDialog = false

                        navController.navigate("profile_setup") {
                            popUpTo("home") {
                                inclusive = true
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFDC2626),
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = AppText.t(
                            appSettings.languageCode,
                            "clear_data"
                        ),
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        showLogoutDialog = false
                    }
                ) {
                    Text(
                        text = AppText.t(
                            appSettings.languageCode,
                            "cancel"
                        ),
                        color = textPrimary
                    )
                }
            }
        )
    }
}

@Composable
private fun AutoEmergencyCallCard(
    isEnabled: Boolean,
    isDarkMode: Boolean,
    textPrimary: Color,
    textSecondary: Color,
    cardColor: Color,
    onToggle: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardColor
        )
    ) {

        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(58.dp)
                    .clip(CircleShape)
                    .background(
                        if (isEnabled) {
                            Color(0xFF22C55E).copy(alpha = 0.18f)
                        } else {
                            Color(0xFFFF1744).copy(alpha = 0.18f)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Default.LocalPhone,
                    contentDescription = null,
                    tint =
                        if (isEnabled) {
                            Color(0xFF22C55E)
                        } else {
                            Color(0xFFFF1744)
                        },
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = "Auto Emergency Call",
                    color = textPrimary,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text =
                        if (isEnabled) {
                            "RoadSOS will automatically call 112 after emergency countdown."
                        } else {
                            "Auto call is OFF. SMS alerts still work."
                        },
                    color =
                        if (isEnabled) {
                            if (isDarkMode) Color(0xFFBBF7D0) else Color(0xFF15803D)
                        } else {
                            if (isDarkMode) Color(0xFFFFCDD2) else Color(0xFFB91C1C)
                        },
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Switch(
                checked = isEnabled,
                onCheckedChange = onToggle,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Color(0xFF22C55E),
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = Color(0xFFDC2626)
                )
            )
        }
    }
}

@Composable
private fun AnimatedModeToggle(
    isDarkMode: Boolean,
    onToggle: () -> Unit
) {
    val trackColor by animateColorAsState(
        targetValue =
            if (isDarkMode) {
                Color(0xFF1E293B)
            } else {
                Color(0xFFE0F2FE)
            },
        animationSpec = tween(220),
        label = "modeTrack"
    )

    val thumbColor by animateColorAsState(
        targetValue =
            if (isDarkMode) {
                Color(0xFF38BDF8)
            } else {
                Color(0xFFF59E0B)
            },
        animationSpec = tween(220),
        label = "modeThumb"
    )

    val textColor by animateColorAsState(
        targetValue =
            if (isDarkMode) {
                Color.White
            } else {
                Color(0xFF075985)
            },
        animationSpec = tween(220),
        label = "modeText"
    )

    val thumbOffset by animateDpAsState(
        targetValue =
            if (isDarkMode) {
                40.dp
            } else {
                4.dp
            },
        animationSpec = tween(220),
        label = "modeThumbOffset"
    )

    Box(
        modifier = Modifier
            .width(78.dp)
            .height(36.dp)
            .clip(RoundedCornerShape(50.dp))
            .background(trackColor)
            .clickable {
                onToggle()
            }
            .padding(4.dp),
        contentAlignment = Alignment.CenterStart
    ) {

        Text(
            text = "Mode",
            color = textColor,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center)
        )

        Box(
            modifier = Modifier
                .offset(x = thumbOffset)
                .size(28.dp)
                .clip(CircleShape)
                .background(thumbColor),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text =
                    if (isDarkMode) {
                        "🌙"
                    } else {
                        "☀"
                    },
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Composable
private fun SettingsMainCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconColor: Color,
    mainTextColor: Color,
    subTextColor: Color,
    cardColor: Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardColor
        )
    ) {

        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(58.dp)
                    .clip(CircleShape)
                    .background(iconColor.copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = title,
                    color = mainTextColor,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = subtitle,
                    color = subTextColor,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = subTextColor
            )
        }
    }
}

@Composable
private fun PermissionManageCard(
    allGranted: Boolean,
    permissionStatus: List<PermissionItem>,
    isDarkMode: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor =
                if (allGranted) {
                    if (isDarkMode) Color(0xFF102A1C) else Color(0xFFDCFCE7)
                } else {
                    if (isDarkMode) Color(0xFF2A1515) else Color(0xFFFEE2E2)
                }
        )
    ) {

        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(58.dp)
                        .clip(CircleShape)
                        .background(
                            if (allGranted) {
                                Color(0xFF22C55E).copy(alpha = 0.18f)
                            } else {
                                Color(0xFFFF1744).copy(alpha = 0.18f)
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector =
                            if (allGranted) {
                                Icons.Default.Verified
                            } else {
                                Icons.Default.Error
                            },
                        contentDescription = null,
                        tint =
                            if (allGranted) {
                                Color(0xFF22C55E)
                            } else {
                                Color(0xFFFF1744)
                            },
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = "Settings Manage",
                        color =
                            if (isDarkMode) {
                                Color.White
                            } else {
                                Color(0xFF0F172A)
                            },
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    Text(
                        text =
                            if (allGranted) {
                                "All required permissions are granted"
                            } else {
                                "Some permissions are missing"
                            },
                        color =
                            if (allGranted) {
                                Color(0xFF22C55E)
                            } else {
                                Color(0xFFDC2626)
                            },
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            permissionStatus.forEach { item ->

                PermissionRow(
                    title = item.title,
                    isGranted = item.isGranted,
                    isDarkMode = isDarkMode
                )

                Spacer(modifier = Modifier.height(10.dp))
            }

            if (!allGranted) {

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Warning: RoadSOS may not function properly without all required permissions.",
                    color =
                        if (isDarkMode) {
                            Color(0xFFFFCDD2)
                        } else {
                            Color(0xFFB91C1C)
                        },
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun PermissionRow(
    title: String,
    isGranted: Boolean,
    isDarkMode: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(
                    if (isGranted) {
                        Color(0xFF22C55E)
                    } else {
                        Color(0xFFFF1744)
                    }
                )
        )

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = title,
            color =
                if (isDarkMode) {
                    Color.White
                } else {
                    Color(0xFF0F172A)
                },
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text =
                if (isGranted) {
                    "Granted"
                } else {
                    "Missing"
                },
            color =
                if (isGranted) {
                    Color(0xFF22C55E)
                } else {
                    Color(0xFFDC2626)
                },
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun LogoutCard(
    title: String,
    isDarkMode: Boolean,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor =
                if (isDarkMode) {
                    Color(0xFF2A1111)
                } else {
                    Color(0xFFFEE2E2)
                }
        )
    ) {

        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(58.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFF1744).copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Default.DeleteForever,
                    contentDescription = null,
                    tint = Color(0xFFFF1744),
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = title,
                    color =
                        if (isDarkMode) {
                            Color.White
                        } else {
                            Color(0xFF7F1D1D)
                        },
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = "Erase saved user profile data from RoadSOS",
                    color =
                        if (isDarkMode) {
                            Color(0xFFFFCDD2)
                        } else {
                            Color(0xFF991B1B)
                        },
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint =
                    if (isDarkMode) {
                        Color(0xFFFFCDD2)
                    } else {
                        Color(0xFF991B1B)
                    }
            )
        }
    }
}

private data class PermissionItem(
    val title: String,
    val isGranted: Boolean
)

private fun getRoadSOSPermissionStatus(
    context: Context
): List<PermissionItem> {

    val permissions =
        mutableListOf<PermissionItem>()

    permissions.add(
        PermissionItem(
            title = "Microphone / Crash Sound Detection",
            isGranted = isPermissionGranted(
                context,
                Manifest.permission.RECORD_AUDIO
            )
        )
    )

    permissions.add(
        PermissionItem(
            title = "Fine Location / Accident Location",
            isGranted = isPermissionGranted(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
    )

    permissions.add(
        PermissionItem(
            title = "Coarse Location / Backup Location",
            isGranted = isPermissionGranted(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    )

    permissions.add(
        PermissionItem(
            title = "Phone Call / Auto Emergency Call",
            isGranted = isPermissionGranted(
                context,
                Manifest.permission.CALL_PHONE
            )
        )
    )

    permissions.add(
        PermissionItem(
            title = "SMS / Emergency Alerts",
            isGranted = isPermissionGranted(
                context,
                Manifest.permission.SEND_SMS
            )
        )
    )

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

        permissions.add(
            PermissionItem(
                title = "Notifications / Emergency Alerts",
                isGranted = isPermissionGranted(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                )
            )
        )
    }

    return permissions
}

private fun isPermissionGranted(
    context: Context,
    permission: String
): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        permission
    ) == PackageManager.PERMISSION_GRANTED
}