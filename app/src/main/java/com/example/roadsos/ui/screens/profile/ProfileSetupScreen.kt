package com.example.roadsos.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.roadsos.utils.AppText
import com.example.roadsos.utils.LanguageUtils
import com.example.roadsos.utils.ProfileValidationUtils
import com.example.roadsos.viewmodel.AppSettingsViewModel
import com.example.roadsos.viewmodel.UserProfileViewModel

@Composable
fun ProfileSetupScreen(
    onProfileSaved: () -> Unit
) {
    val userProfileViewModel: UserProfileViewModel =
        viewModel()

    val appSettingsViewModel: AppSettingsViewModel =
        viewModel()

    val appSettings by
    appSettingsViewModel.appSettings.collectAsState()

    var name by remember {
        mutableStateOf("")
    }

    var bloodGroup by remember {
        mutableStateOf("")
    }

    var emergencyContact by remember {
        mutableStateOf("")
    }

    var weight by remember {
        mutableStateOf("")
    }

    var height by remember {
        mutableStateOf("")
    }

    var age by remember {
        mutableStateOf("")
    }

    var errorMessage by remember {
        mutableStateOf("")
    }

    val backgroundBrush =
        Brush.verticalGradient(
            listOf(
                Color(0xFF07111F),
                Color(0xFF0B1220),
                Color(0xFF111827)
            )
        )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(22.dp))

            Box(
                modifier = Modifier
                    .size(82.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFF1744).copy(alpha = 0.16f)),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Default.HealthAndSafety,
                    contentDescription = null,
                    tint = Color(0xFFFF1744),
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Setup RoadSOS Profile",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Your medical details help during emergency response.",
                color = Color.LightGray,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(28.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(30.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.07f)
                )
            ) {

                Column(
                    modifier = Modifier.padding(20.dp)
                ) {

                    LanguageDropdownCard(
                        selectedLanguageCode = appSettings.languageCode,
                        onLanguageSelected = { languageCode ->
                            appSettingsViewModel.setLanguage(languageCode)
                        }
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    ModernProfileTextField(
                        value = name,
                        onValueChange = { input ->
                            name =
                                input.filter { character ->
                                    character.isLetter() || character == ' '
                                }
                        },
                        label = "Full Name",
                        placeholder = "Example: Ashutosh Masrayan",
                        keyboardType = KeyboardType.Text
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Blood Group",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    BloodGroupSelector(
                        selectedBloodGroup = bloodGroup,
                        onBloodGroupSelected = {
                            bloodGroup = it
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    ModernProfileTextField(
                        value = emergencyContact,
                        onValueChange = { input ->
                            emergencyContact =
                                input
                                    .filter { character ->
                                        character.isDigit()
                                    }
                                    .take(10)
                        },
                        label = "Emergency Contact",
                        placeholder = "10 digit mobile number",
                        keyboardType = KeyboardType.Number
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    ModernProfileTextField(
                        value = weight,
                        onValueChange = { input ->
                            weight =
                                input
                                    .filter { character ->
                                        character.isDigit()
                                    }
                                    .take(3)
                        },
                        label = "Weight",
                        placeholder = "Example: 65 kg",
                        keyboardType = KeyboardType.Number
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    ModernProfileTextField(
                        value = height,
                        onValueChange = { input ->
                            height =
                                input
                                    .filter { character ->
                                        character.isDigit()
                                    }
                                    .take(3)
                        },
                        label = "Height",
                        placeholder = "Example: 170 cm",
                        keyboardType = KeyboardType.Number
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    ModernProfileTextField(
                        value = age,
                        onValueChange = { input ->
                            age =
                                input
                                    .filter { character ->
                                        character.isDigit()
                                    }
                                    .take(3)
                        },
                        label = "Age",
                        placeholder = "Example: 20",
                        keyboardType = KeyboardType.Number
                    )

                    if (errorMessage.isNotBlank()) {

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = errorMessage,
                            color = Color(0xFFFFCDD2),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {

                            val validationError =
                                ProfileValidationUtils.validateFullProfile(
                                    name = name,
                                    bloodGroup = bloodGroup,
                                    emergencyContact = emergencyContact,
                                    weight = weight,
                                    height = height,
                                    age = age
                                )

                            if (validationError != null) {

                                errorMessage =
                                    validationError

                            } else {

                                userProfileViewModel.saveUserProfile(
                                    name = name,
                                    bloodGroup = bloodGroup,
                                    emergencyContact = emergencyContact,
                                    weight = weight,
                                    height = height,
                                    age = age
                                )

                                onProfileSaved()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(58.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFF1744),
                            contentColor = Color.White
                        )
                    ) {

                        Text(
                            text = AppText.t(
                                appSettings.languageCode,
                                "save_continue"
                            ),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun ModernProfileTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    keyboardType: KeyboardType
) {

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = {
            Text(text = label)
        },
        placeholder = {
            Text(text = placeholder)
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        shape = RoundedCornerShape(18.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedLabelColor = Color(0xFFFF1744),
            unfocusedLabelColor = Color.LightGray,
            focusedBorderColor = Color(0xFFFF1744),
            unfocusedBorderColor = Color.DarkGray,
            cursorColor = Color(0xFFFF1744)
        )
    )
}

@Composable
private fun BloodGroupSelector(
    selectedBloodGroup: String,
    onBloodGroupSelected: (String) -> Unit
) {

    Column {

        val rows =
            ProfileValidationUtils.bloodGroups.chunked(4)

        rows.forEach { rowItems ->

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                rowItems.forEach { bloodGroup ->

                    val selected =
                        selectedBloodGroup == bloodGroup

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                if (selected) {
                                    Color(0xFFFF1744)
                                } else {
                                    Color.White.copy(alpha = 0.08f)
                                }
                            )
                            .border(
                                width = 1.dp,
                                color =
                                    if (selected) {
                                        Color.White
                                    } else {
                                        Color.DarkGray
                                    },
                                shape = RoundedCornerShape(16.dp)
                            )
                            .clickable {
                                onBloodGroupSelected(bloodGroup)
                            },
                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            text = bloodGroup,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
private fun LanguageDropdownCard(
    selectedLanguageCode: String,
    onLanguageSelected: (String) -> Unit
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.08f)
        )
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = "Choose Language",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(10.dp))

            Box {

                OutlinedButton(
                    onClick = {
                        expanded = true
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {

                    Text(
                        text = LanguageUtils.getLanguageName(selectedLanguageCode),
                        color = Color.White
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    }
                ) {

                    LanguageUtils.supportedLanguages.forEach { language ->

                        DropdownMenuItem(
                            text = {
                                Text(text = language.displayName)
                            },
                            onClick = {
                                onLanguageSelected(language.code)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}