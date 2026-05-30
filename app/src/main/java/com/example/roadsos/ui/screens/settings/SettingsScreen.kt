package com.example.roadsos.ui.screens.settings

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.roadsos.viewmodel.UserProfileViewModel

@Composable
fun SettingsScreen(
    navController: NavController
) {
    val context =
        LocalContext.current

    val userProfileViewModel: UserProfileViewModel =
        viewModel()

    val permissionStatus =
        remember {
            getRoadSOSPermissionStatus(context)
        }

    var showLogoutDialog by remember {
        mutableStateOf(false)
    }

    val allImportantPermissionsGranted =
        permissionStatus.all { it.isGranted }

    val backgroundBrush =
        Brush.verticalGradient(
            listOf(
                Color(0xFF07111F),
                Color(0xFF0B1220),
                Color(0xFF111827)
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

                Text(
                    text = "Settings",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Manage your RoadSOS safety profile and app access.",
                    color = Color.LightGray
                )

                Spacer(modifier = Modifier.height(28.dp))

                // -----------------------------
                // 1. PROFILE CARD
                // -----------------------------

                SettingsMainCard(
                    title = "Profile",
                    subtitle = "View your medical and emergency information",
                    icon = Icons.Default.Person,
                    iconColor = Color(0xFF38BDF8),
                    onClick = {
                        navController.navigate("profile_view")
                    }
                )

                Spacer(modifier = Modifier.height(18.dp))

                // -----------------------------
                // 2. PERMISSION MANAGE CARD
                // -----------------------------

                PermissionManageCard(
                    allGranted = allImportantPermissionsGranted,
                    permissionStatus = permissionStatus
                )

                Spacer(modifier = Modifier.height(18.dp))

                // -----------------------------
                // 3. LOGOUT CARD
                // -----------------------------

                LogoutCard(
                    onClick = {
                        showLogoutDialog = true
                    }
                )

                Spacer(modifier = Modifier.height(95.dp))
            }
        }
    }

    if (showLogoutDialog) {

        AlertDialog(
            onDismissRequest = {
                showLogoutDialog = false
            },
            containerColor = Color(0xFF111827),
            title = {
                Text(
                    text = "Clear RoadSOS Data?",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "This will erase your saved profile information from this app. You will need to enter your details again.",
                    color = Color.LightGray
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
                        text = "Clear Data",
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
                        text = "Cancel",
                        color = Color.White
                    )
                }
            }
        )
    }
}

@Composable
private fun SettingsMainCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconColor: Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF121C2E)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
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
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = subtitle,
                    color = Color.LightGray,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color.LightGray
            )
        }
    }
}

@Composable
private fun PermissionManageCard(
    allGranted: Boolean,
    permissionStatus: List<PermissionItem>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor =
                if (allGranted) {
                    Color(0xFF102A1C)
                } else {
                    Color(0xFF2A1515)
                }
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
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
                        color = Color.White,
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
                                Color(0xFFBBF7D0)
                            } else {
                                Color(0xFFFFCDD2)
                            },
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            permissionStatus.forEach { item ->

                PermissionRow(
                    title = item.title,
                    isGranted = item.isGranted
                )

                Spacer(modifier = Modifier.height(10.dp))
            }

            if (!allGranted) {

                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF7F1D1D)
                    )
                ) {

                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = null,
                            tint = Color(0xFFFFCDD2)
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Text(
                            text = "Warning: RoadSOS may not function properly without all required permissions.",
                            color = Color(0xFFFFCDD2),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PermissionRow(
    title: String,
    isGranted: Boolean
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
            color = Color.White,
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
                    Color(0xFFBBF7D0)
                } else {
                    Color(0xFFFFCDD2)
                },
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun LogoutCard(
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2A1111)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
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
                    text = "Logout",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = "Erase saved user profile data from RoadSOS",
                    color = Color(0xFFFFCDD2),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color(0xFFFFCDD2)
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
            title = "Phone Call / Emergency Calling",
            isGranted = isPermissionGranted(
                context,
                Manifest.permission.CALL_PHONE
            )
        )
    )

    permissions.add(
        PermissionItem(
            title = "SMS / Emergency Alert Message",
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