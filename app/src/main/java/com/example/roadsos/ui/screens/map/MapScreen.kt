package com.example.roadsos.ui.screens.map

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
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
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.roadsos.data.local.NearbyEmergencyService
import com.example.roadsos.ui.components.BottomNavBar
import com.example.roadsos.utils.AppText
import com.example.roadsos.utils.CategoryUtils
import com.example.roadsos.utils.MapIntentHelper
import com.example.roadsos.utils.roadSOSThemeColors
import com.example.roadsos.viewmodel.AppSettingsViewModel
import com.example.roadsos.viewmodel.EmergencyServicesViewModel
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

@Composable
fun MapScreen(
    navController: NavController
) {
    val context =
        LocalContext.current

    val servicesViewModel: EmergencyServicesViewModel =
        viewModel()

    val appSettingsViewModel: AppSettingsViewModel =
        viewModel()

    val appSettings by
    appSettingsViewModel.appSettings.collectAsState()

    val language =
        appSettings.languageCode

    val themeColors =
        roadSOSThemeColors(appSettings.isDarkMode)

    val topColor by animateColorAsState(
        targetValue = themeColors.backgroundTop,
        animationSpec = tween(260),
        label = "mapTopColor"
    )

    val middleColor by animateColorAsState(
        targetValue = themeColors.backgroundMiddle,
        animationSpec = tween(260),
        label = "mapMiddleColor"
    )

    val bottomColor by animateColorAsState(
        targetValue = themeColors.backgroundBottom,
        animationSpec = tween(260),
        label = "mapBottomColor"
    )

    val textPrimary by animateColorAsState(
        targetValue = themeColors.textPrimary,
        animationSpec = tween(260),
        label = "mapTextPrimary"
    )

    val textSecondary by animateColorAsState(
        targetValue = themeColors.textSecondary,
        animationSpec = tween(260),
        label = "mapTextSecondary"
    )

    val cardColor by animateColorAsState(
        targetValue = themeColors.card,
        animationSpec = tween(260),
        label = "mapCardColor"
    )

    val strongCardColor by animateColorAsState(
        targetValue = themeColors.cardStrong,
        animationSpec = tween(260),
        label = "mapStrongCardColor"
    )

    val borderColor by animateColorAsState(
        targetValue = themeColors.border,
        animationSpec = tween(260),
        label = "mapBorderColor"
    )

    val backgroundBrush =
        Brush.verticalGradient(
            listOf(
                topColor,
                middleColor,
                bottomColor
            )
        )

    LaunchedEffect(Unit) {
        delay(120)
        servicesViewModel.loadNearestServices()
    }

    val emergencyServices by
    servicesViewModel.services.collectAsState()

    val location by
    servicesViewModel.location.collectAsState()

    val nearestServices =
        emergencyServices
            .values
            .flatten()
            .sortedBy { it.distance }
            .take(10)

    Scaffold(
        bottomBar = {
            BottomNavBar(navController)
        },
        containerColor = Color.Transparent
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundBrush)
                .padding(padding)
                .padding(horizontal = 20.dp)
                .padding(top = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Text(
                text = AppText.t(
                    language,
                    "emergency_map"
                ),
                color = textPrimary,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = AppText.t(
                    language,
                    "map_subtitle"
                ),
                color = textSecondary,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = borderColor,
                        shape = RoundedCornerShape(30.dp)
                    ),
                shape = RoundedCornerShape(30.dp),
                colors = CardDefaults.cardColors(
                    containerColor = cardColor
                )
            ) {

                Column(
                    modifier = Modifier.padding(18.dp)
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF22C55E))
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Text(
                            text = AppText.t(
                                language,
                                "live_emergency_radar"
                            ),
                            color = textPrimary,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = AppText.t(
                            language,
                            "radar_desc"
                        ),
                        color = textSecondary,
                        style = MaterialTheme.typography.bodySmall
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    DynamicEmergencyRadar(
                        services = nearestServices,
                        isDarkMode = appSettings.isDarkMode,
                        language = language,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(310.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    location?.let { currentLocation ->

                        Button(
                            onClick = {
                                MapIntentHelper.openLocation(
                                    context = context,
                                    latitude = currentLocation.first,
                                    longitude = currentLocation.second,
                                    label = "RoadSOS Accident Location"
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF2563EB),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(18.dp)
                        ) {

                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = AppText.t(
                                    language,
                                    "open_accident_location"
                                ),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = AppText.t(
                    language,
                    "nearest_emergency_services"
                ),
                color = textPrimary,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = AppText.t(
                    language,
                    "top_services_desc"
                ),
                color = textSecondary,
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(14.dp))

            if (emergencyServices.isEmpty()) {

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = cardColor
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {

                    Text(
                        text = AppText.t(
                            language,
                            "loading_services"
                        ),
                        color = textSecondary,
                        modifier = Modifier.padding(16.dp)
                    )
                }

            } else {

                emergencyServices.forEach { entry ->

                    val category =
                        entry.key

                    val services =
                        entry.value
                            .sortedBy { it.distance }
                            .take(3)

                    if (services.isNotEmpty()) {

                        Text(
                            text = CategoryUtils.readableName(category),
                            color = Color(0xFF0EA5E9),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleSmall
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        services.forEach { service ->

                            MinimalServiceCard(
                                service = service,
                                language = language,
                                textPrimary = textPrimary,
                                textSecondary = textSecondary,
                                cardColor = strongCardColor,
                                borderColor = borderColor,
                                onMapClick = {
                                    MapIntentHelper.openLocation(
                                        context = context,
                                        latitude = service.latitude,
                                        longitude = service.longitude,
                                        label = service.name
                                    )
                                },
                                onRouteClick = {
                                    MapIntentHelper.openNavigation(
                                        context = context,
                                        latitude = service.latitude,
                                        longitude = service.longitude
                                    )
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
private fun DynamicEmergencyRadar(
    services: List<NearbyEmergencyService>,
    isDarkMode: Boolean,
    language: String,
    modifier: Modifier = Modifier
) {
    val infiniteTransition =
        rememberInfiniteTransition(label = "modernRadarTransition")

    val pulse by
    infiniteTransition.animateFloat(
        initialValue = 0.25f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 2600,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "radarPulse"
    )

    val scannerRotation by
    infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 6200,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "scannerRotation"
    )

    val radarBackground =
        if (isDarkMode) {
            Brush.radialGradient(
                listOf(
                    Color(0xFF123A5A),
                    Color(0xFF071827),
                    Color(0xFF020617)
                )
            )
        } else {
            Brush.radialGradient(
                listOf(
                    Color(0xFFE0F2FE),
                    Color(0xFFF8FAFC),
                    Color(0xFFFFFFFF)
                )
            )
        }

    val gridColor =
        if (isDarkMode) {
            Color(0xFF334155).copy(alpha = 0.35f)
        } else {
            Color(0xFF94A3B8).copy(alpha = 0.30f)
        }

    val ringColor =
        if (isDarkMode) {
            Color(0xFF38BDF8)
        } else {
            Color(0xFF0284C7)
        }

    val centerColor =
        Color(0xFF22C55E)

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(28.dp))
            .background(radarBackground)
            .border(
                width = 1.dp,
                color =
                    if (isDarkMode) {
                        Color.White.copy(alpha = 0.08f)
                    } else {
                        Color(0xFFCBD5E1)
                    },
                shape = RoundedCornerShape(28.dp)
            )
    ) {

        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            val center =
                Offset(
                    x = size.width / 2f,
                    y = size.height / 2f
                )

            val maxRadius =
                min(size.width, size.height) * 0.43f

            var x = 0f

            while (x < size.width) {

                drawLine(
                    color = gridColor,
                    start = Offset(x, 0f),
                    end = Offset(x, size.height),
                    strokeWidth = 1f
                )

                x += size.width / 9f
            }

            var y = 0f

            while (y < size.height) {

                drawLine(
                    color = gridColor,
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = 1f
                )

                y += size.height / 7f
            }

            drawCircle(
                color = ringColor.copy(alpha = 0.08f),
                radius = maxRadius * 1.15f,
                center = center
            )

            drawCircle(
                color = ringColor.copy(alpha = 0.17f * (1f - pulse)),
                radius = maxRadius * pulse,
                center = center
            )

            drawCircle(
                color = ringColor.copy(alpha = 0.30f),
                radius = maxRadius,
                center = center,
                style = Stroke(width = 2.4f)
            )

            drawCircle(
                color = ringColor.copy(alpha = 0.24f),
                radius = maxRadius * 0.72f,
                center = center,
                style = Stroke(width = 2f)
            )

            drawCircle(
                color = ringColor.copy(alpha = 0.20f),
                radius = maxRadius * 0.46f,
                center = center,
                style = Stroke(width = 1.8f)
            )

            drawCircle(
                color = ringColor.copy(alpha = 0.16f),
                radius = maxRadius * 0.24f,
                center = center,
                style = Stroke(width = 1.5f)
            )

            drawLine(
                color = gridColor,
                start = Offset(center.x, center.y - maxRadius),
                end = Offset(center.x, center.y + maxRadius),
                strokeWidth = 2f
            )

            drawLine(
                color = gridColor,
                start = Offset(center.x - maxRadius, center.y),
                end = Offset(center.x + maxRadius, center.y),
                strokeWidth = 2f
            )

            rotate(
                degrees = scannerRotation,
                pivot = center
            ) {

                drawArc(
                    color = ringColor.copy(alpha = 0.16f),
                    startAngle = -22f,
                    sweepAngle = 44f,
                    useCenter = true,
                    topLeft = Offset(
                        center.x - maxRadius,
                        center.y - maxRadius
                    ),
                    size = Size(
                        width = maxRadius * 2f,
                        height = maxRadius * 2f
                    )
                )

                drawLine(
                    color = ringColor.copy(alpha = 0.65f),
                    start = center,
                    end = Offset(
                        x = center.x + maxRadius,
                        y = center.y
                    ),
                    strokeWidth = 3.5f
                )
            }

            drawCircle(
                color = centerColor.copy(alpha = 0.20f),
                radius = 34f + (pulse * 10f),
                center = center
            )

            drawCircle(
                color = centerColor,
                radius = 15f,
                center = center
            )

            drawCircle(
                color = Color.White,
                radius = 5f,
                center = center
            )

            services.forEachIndexed { index, service ->

                val angle =
                    (index * 41.0 + 16.0).toFloat()

                val normalizedDistance =
                    (service.distance / 25.0).coerceIn(0.22, 1.0)

                val radius =
                    (maxRadius * normalizedDistance).toFloat()

                val angleRadians =
                    Math.toRadians(angle.toDouble())

                val point =
                    Offset(
                        x = center.x + cos(angleRadians).toFloat() * radius,
                        y = center.y + sin(angleRadians).toFloat() * radius
                    )

                val dotColor =
                    categoryColor(service.category)

                drawLine(
                    color = dotColor.copy(alpha = 0.26f),
                    start = center,
                    end = point,
                    strokeWidth = 2f
                )

                drawCircle(
                    color = dotColor.copy(alpha = 0.16f),
                    radius = 25f,
                    center = point
                )

                drawCircle(
                    color = dotColor.copy(alpha = 0.42f),
                    radius = 15f,
                    center = point
                )

                drawCircle(
                    color = dotColor,
                    radius = 8.5f,
                    center = point
                )

                drawCircle(
                    color = Color.White.copy(alpha = 0.75f),
                    radius = 2.8f,
                    center = point
                )
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            RadarStatusChip(
                text = "AI RADAR",
                color = Color(0xFF22D3EE),
                isDarkMode = isDarkMode
            )

            RadarStatusChip(
                text = "OFFLINE DB",
                color = Color(0xFF22C55E),
                isDarkMode = isDarkMode
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(14.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    if (isDarkMode) {
                        Color.Black.copy(alpha = 0.32f)
                    } else {
                        Color.White.copy(alpha = 0.70f)
                    }
                )
                .padding(
                    horizontal = 12.dp,
                    vertical = 8.dp
                )
        ) {

            Text(
                text =
                    "${services.size} ${AppText.t(language, "nearby")}",
                color =
                    if (isDarkMode) {
                        Color.White
                    } else {
                        Color(0xFF0F172A)
                    },
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = AppText.t(
                    language,
                    "you"
                ),
                color =
                    if (isDarkMode) {
                        Color.White
                    } else {
                        Color(0xFF0F172A)
                    },
                fontWeight = FontWeight.ExtraBold,
                style = MaterialTheme.typography.titleSmall
            )

            Text(
                text = AppText.t(
                    language,
                    "accident_point"
                ),
                color = Color(0xFF22C55E),
                style = MaterialTheme.typography.labelSmall
            )
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(12.dp)
                .clip(RoundedCornerShape(22.dp))
                .background(
                    if (isDarkMode) {
                        Color.Black.copy(alpha = 0.30f)
                    } else {
                        Color.White.copy(alpha = 0.70f)
                    }
                )
                .padding(
                    horizontal = 12.dp,
                    vertical = 10.dp
                ),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            RadarLegendDot(
                label = AppText.t(
                    language,
                    "trauma"
                ),
                color = Color.Red,
                isDarkMode = isDarkMode
            )

            RadarLegendDot(
                label = AppText.t(
                    language,
                    "police"
                ),
                color = Color(0xFF60A5FA),
                isDarkMode = isDarkMode
            )

            RadarLegendDot(
                label = AppText.t(
                    language,
                    "vehicle"
                ),
                color = Color(0xFF22D3EE),
                isDarkMode = isDarkMode
            )
        }
    }
}

@Composable
private fun RadarStatusChip(
    text: String,
    color: Color,
    isDarkMode: Boolean
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(18.dp))
            .background(
                if (isDarkMode) {
                    Color.Black.copy(alpha = 0.34f)
                } else {
                    Color.White.copy(alpha = 0.72f)
                }
            )
            .padding(
                horizontal = 10.dp,
                vertical = 7.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color)
        )

        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = text,
            color =
                if (isDarkMode) {
                    Color.White
                } else {
                    Color(0xFF0F172A)
                },
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
private fun RadarLegendDot(
    label: String,
    color: Color,
    isDarkMode: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color)
        )

        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = label,
            color =
                if (isDarkMode) {
                    Color.LightGray
                } else {
                    Color(0xFF475569)
                },
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun MinimalServiceCard(
    service: NearbyEmergencyService,
    language: String,
    textPrimary: Color,
    textSecondary: Color,
    cardColor: Color,
    borderColor: Color,
    onMapClick: () -> Unit,
    onRouteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(20.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = cardColor
        ),
        shape = RoundedCornerShape(20.dp)
    ) {

        Column(
            modifier = Modifier.padding(14.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(categoryColor(service.category))
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = service.name,
                    color = textPrimary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text =
                        "${"%.2f".format(service.distance)} ${AppText.t(language, "km_away")}",
                    color = textSecondary,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text =
                        if (service.isVerified == 1) {
                            AppText.t(
                                language,
                                "verified"
                            )
                        } else {
                            AppText.t(
                                language,
                                "unverified"
                            )
                        },
                    color =
                        if (service.isVerified == 1) {
                            Color(0xFF22C55E)
                        } else {
                            Color.Gray
                        },
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                OutlinedButton(
                    onClick = onMapClick,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp)
                ) {

                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = AppText.t(
                            language,
                            "map"
                        )
                    )
                }

                Button(
                    onClick = onRouteClick,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2563EB),
                        contentColor = Color.White
                    )
                ) {

                    Icon(
                        imageVector = Icons.Default.Navigation,
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = AppText.t(
                            language,
                            "route"
                        )
                    )
                }
            }
        }
    }
}

private fun categoryColor(
    category: String
): Color {
    val normalizedCategory =
        category.lowercase()

    return when {
        normalizedCategory.contains("hospital") ||
                normalizedCategory.contains("trauma") ||
                normalizedCategory.contains("medical") ->
            Color(0xFFFF1744)

        normalizedCategory.contains("police") ->
            Color(0xFF60A5FA)

        normalizedCategory.contains("ambulance") ||
                normalizedCategory.contains("emergency") ->
            Color(0xFFE879F9)

        normalizedCategory.contains("tow") ||
                normalizedCategory.contains("repair") ||
                normalizedCategory.contains("vehicle") ||
                normalizedCategory.contains("puncture") ->
            Color(0xFF22D3EE)

        normalizedCategory.contains("fire") ->
            Color(0xFFF97316)

        else ->
            Color(0xFF22C55E)
    }
}