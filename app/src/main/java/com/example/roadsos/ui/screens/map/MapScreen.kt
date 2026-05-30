package com.example.roadsos.ui.screens.map

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.roadsos.data.local.NearbyEmergencyService
import com.example.roadsos.ui.components.BottomNavBar
import com.example.roadsos.utils.CategoryUtils
import com.example.roadsos.utils.MapIntentHelper
import com.example.roadsos.viewmodel.EmergencyServicesViewModel
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
import androidx.compose.animation.core.LinearEasing
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate

@Composable
fun MapScreen(
    navController: NavController
) {
    val context =
        LocalContext.current

    val servicesViewModel: EmergencyServicesViewModel =
        viewModel()

    LaunchedEffect(Unit) {
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
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFF07111F),
                            Color(0xFF0B1220),
                            Color(0xFF111827)
                        )
                    )
                )
                .padding(padding)
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Text(
                text = "Emergency Map",
                color = Color.White,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Offline service radar with external navigation",
                color = Color.LightGray
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(30.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF111C2E)
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
                                .background(Color.Green)
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Text(
                            text = "Live Emergency Radar",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Center is your accident location. Dots are nearby emergency services.",
                        color = Color.LightGray,
                        style = MaterialTheme.typography.bodySmall
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    DynamicEmergencyRadar(
                        services = nearestServices,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(310.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    location?.let {
                        Button(
                            onClick = {
                                MapIntentHelper.openLocation(
                                    context = context,
                                    latitude = it.first,
                                    longitude = it.second,
                                    label = "RoadSOS Accident Location"
                                )
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF2563EB)
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text("Open Accident Location")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Nearest Emergency Services",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Minimal top 3 services from each category",
                color = Color.Gray,
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(14.dp))

            if (emergencyServices.isEmpty()) {

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF1F2937)
                    ),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Text(
                        text = "Loading emergency services from offline database...",
                        color = Color.LightGray,
                        modifier = Modifier.padding(16.dp)
                    )
                }

            } else {

                emergencyServices.forEach { entry ->

                    val category =
                        entry.key

                    val services =
                        entry.value.take(3)

                    if (services.isNotEmpty()) {

                        Text(
                            text = CategoryUtils.readableName(category),
                            color = Color.Cyan,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        services.forEach { service ->

                            MinimalServiceCard(
                                service = service,
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

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun DynamicEmergencyRadar(
    services: List<NearbyEmergencyService>,
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
                durationMillis = 1700,
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
                durationMillis = 3600,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "scannerRotation"
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(30.dp))
            .background(
                Brush.radialGradient(
                    listOf(
                        Color(0xFF123A5A),
                        Color(0xFF071827),
                        Color(0xFF020617)
                    )
                )
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

            val ringStroke =
                Stroke(
                    width = 2.5f,
                    pathEffect = PathEffect.dashPathEffect(
                        intervals = floatArrayOf(16f, 12f),
                        phase = 0f
                    )
                )

            // Background cyber grid
            val gridColor =
                Color(0xFF1E293B).copy(alpha = 0.32f)

            var x = 0f
            while (x < size.width) {
                drawLine(
                    color = gridColor,
                    start = Offset(x, 0f),
                    end = Offset(x, size.height),
                    strokeWidth = 1.2f
                )
                x += size.width / 9f
            }

            var y = 0f
            while (y < size.height) {
                drawLine(
                    color = gridColor,
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = 1.2f
                )
                y += size.height / 7f
            }

            // Outer emergency glow
            drawCircle(
                color = Color(0xFF0EA5E9).copy(alpha = 0.08f),
                radius = maxRadius * 1.18f,
                center = center
            )

            // Pulse wave
            drawCircle(
                color = Color(0xFF38BDF8).copy(alpha = 0.18f * (1f - pulse)),
                radius = maxRadius * pulse,
                center = center
            )

            // Radar rings
            drawCircle(
                color = Color(0xFF38BDF8).copy(alpha = 0.32f),
                radius = maxRadius,
                center = center,
                style = ringStroke
            )

            drawCircle(
                color = Color(0xFF38BDF8).copy(alpha = 0.24f),
                radius = maxRadius * 0.72f,
                center = center,
                style = Stroke(width = 2.2f)
            )

            drawCircle(
                color = Color(0xFF38BDF8).copy(alpha = 0.22f),
                radius = maxRadius * 0.46f,
                center = center,
                style = Stroke(width = 2f)
            )

            drawCircle(
                color = Color(0xFF38BDF8).copy(alpha = 0.18f),
                radius = maxRadius * 0.24f,
                center = center,
                style = Stroke(width = 1.8f)
            )

            // Cross axis
            drawLine(
                color = Color(0xFF64748B).copy(alpha = 0.32f),
                start = Offset(center.x, center.y - maxRadius),
                end = Offset(center.x, center.y + maxRadius),
                strokeWidth = 2f
            )

            drawLine(
                color = Color(0xFF64748B).copy(alpha = 0.32f),
                start = Offset(center.x - maxRadius, center.y),
                end = Offset(center.x + maxRadius, center.y),
                strokeWidth = 2f
            )

            // Rotating scanner beam
            rotate(
                degrees = scannerRotation,
                pivot = center
            ) {
                drawArc(
                    color = Color(0xFF22D3EE).copy(alpha = 0.22f),
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
                    color = Color(0xFF67E8F9).copy(alpha = 0.75f),
                    start = center,
                    end = Offset(
                        x = center.x + maxRadius,
                        y = center.y
                    ),
                    strokeWidth = 4f
                )
            }

            // User accident location glow
            drawCircle(
                color = Color(0xFF22C55E).copy(alpha = 0.20f),
                radius = 34f + (pulse * 12f),
                center = center
            )

            drawCircle(
                color = Color(0xFF22C55E),
                radius = 15f,
                center = center
            )

            drawCircle(
                color = Color.White,
                radius = 5f,
                center = center
            )

            // Service dots
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
                    color = dotColor.copy(alpha = 0.28f),
                    start = center,
                    end = point,
                    strokeWidth = 2f
                )

                drawCircle(
                    color = dotColor.copy(alpha = 0.18f),
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

        // Top glass status row
        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            RadarStatusChip(
                text = "AI RADAR",
                color = Color(0xFF22D3EE)
            )

            RadarStatusChip(
                text = "OFFLINE DB",
                color = Color(0xFF22C55E)
            )
        }

        // Service count badge
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(14.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.Black.copy(alpha = 0.32f))
                .padding(
                    horizontal = 12.dp,
                    vertical = 8.dp
                )
        ) {
            Text(
                text = "${services.size} nearby",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodySmall
            )
        }

        // Center accident label
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "YOU",
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                style = MaterialTheme.typography.titleSmall
            )

            Text(
                text = "Accident Point",
                color = Color(0xFFBBF7D0),
                style = MaterialTheme.typography.labelSmall
            )
        }

        // Bottom legend
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(12.dp)
                .clip(RoundedCornerShape(22.dp))
                .background(Color.Black.copy(alpha = 0.30f))
                .padding(
                    horizontal = 12.dp,
                    vertical = 10.dp
                ),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadarLegendDot(
                label = "Trauma",
                color = Color.Red
            )

            RadarLegendDot(
                label = "Police",
                color = Color(0xFF60A5FA)
            )

            RadarLegendDot(
                label = "Vehicle",
                color = Color(0xFF22D3EE)
            )
        }
    }
}

@Composable
private fun RadarStatusChip(
    text: String,
    color: Color
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(18.dp))
            .background(Color.Black.copy(alpha = 0.34f))
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
            color = Color.White,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
private fun RadarLegendDot(
    label: String,
    color: Color
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
            color = Color.LightGray,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun MinimalServiceCard(
    service: NearbyEmergencyService,
    onMapClick: () -> Unit,
    onRouteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF111827)
        ),
        shape = RoundedCornerShape(18.dp)
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
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${"%.2f".format(service.distance)} km away",
                    color = Color.LightGray,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text =
                        if (service.isVerified == 1) {
                            "Verified"
                        } else {
                            "Unverified"
                        },
                    color =
                        if (service.isVerified == 1) {
                            Color.Green
                        } else {
                            Color.Gray
                        },
                    style = MaterialTheme.typography.bodySmall
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
                    Text("Map")
                }

                Button(
                    onClick = onRouteClick,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF16A34A)
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Route")
                }
            }
        }
    }
}

private fun categoryColor(
    category: String
): Color {
    return when (category) {
        "trauma_l1" ->
            Color.Red

        "trauma_l2" ->
            Color(0xFFFF9800)

        "police_station" ->
            Color(0xFF60A5FA)

        "district_emergency" ->
            Color(0xFFE879F9)

        "puncture_shop" ->
            Color(0xFFFACC15)

        "vehicle_repair" ->
            Color(0xFF22D3EE)

        "vehicle_showroom" ->
            Color.LightGray

        else ->
            Color.White
    }
}