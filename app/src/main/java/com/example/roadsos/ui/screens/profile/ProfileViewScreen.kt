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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.navigation.NavController
import com.example.roadsos.ui.components.BottomNavBar
import com.example.roadsos.utils.LanguageUtils
import com.example.roadsos.utils.ProfileValidationUtils
import com.example.roadsos.viewmodel.AppSettingsViewModel
import com.example.roadsos.viewmodel.UserProfileViewModel

private enum class EditableProfileField {
    NAME,
    BLOOD_GROUP,
    WEIGHT,
    HEIGHT,
    AGE
}

@Composable
fun ProfileViewScreen(
    navController: NavController
) {
    val userProfileViewModel: UserProfileViewModel =
        viewModel()

    val appSettingsViewModel: AppSettingsViewModel =
        viewModel()

    val userProfile by
    userProfileViewModel.userProfile.collectAsState()

    val appSettings by
    appSettingsViewModel.appSettings.collectAsState()

    var editingField by remember {
        mutableStateOf<EditableProfileField?>(null)
    }

    var editingValue by remember {
        mutableStateOf("")
    }

    var editingError by remember {
        mutableStateOf("")
    }

    var showAddContactDialog by remember {
        mutableStateOf(false)
    }

    var contactBeingEdited by remember {
        mutableStateOf<String?>(null)
    }

    var contactInput by remember {
        mutableStateOf("")
    }

    var contactError by remember {
        mutableStateOf("")
    }

    var contactToDelete by remember {
        mutableStateOf<String?>(null)
    }

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
                    text = "Medical Profile",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Saved emergency information for RoadSOS.",
                    color = Color.LightGray
                )

                Spacer(modifier = Modifier.height(24.dp))

                ProfileHeaderCard(
                    name = userProfile.name,
                    bloodGroup = userProfile.bloodGroup
                )

                Spacer(modifier = Modifier.height(18.dp))

                LanguageChangeCard(
                    selectedLanguageCode = appSettings.languageCode,
                    onLanguageSelected = { languageCode ->
                        appSettingsViewModel.setLanguage(languageCode)
                    }
                )

                Spacer(modifier = Modifier.height(18.dp))

                EmergencyContactsSection(
                    contacts = userProfile.getAllEmergencyContacts(),
                    onAddContact = {
                        contactBeingEdited = null
                        contactInput = ""
                        contactError = ""
                        showAddContactDialog = true
                    },
                    onEditContact = { contact ->
                        contactBeingEdited = contact
                        contactInput = contact
                        contactError = ""
                        showAddContactDialog = true
                    },
                    onDeleteContact = { contact ->
                        contactToDelete = contact
                    }
                )

                Spacer(modifier = Modifier.height(18.dp))

                EditableInfoCard(
                    emoji = "👤",
                    title = "Name",
                    value = userProfile.name,
                    onEdit = {
                        editingField = EditableProfileField.NAME
                        editingValue = userProfile.name
                        editingError = ""
                    }
                )

                EditableInfoCard(
                    emoji = "🩸",
                    title = "Blood Group",
                    value = userProfile.bloodGroup,
                    onEdit = {
                        editingField = EditableProfileField.BLOOD_GROUP
                        editingValue = userProfile.bloodGroup
                        editingError = ""
                    }
                )

                EditableInfoCard(
                    emoji = "⚖️",
                    title = "Weight",
                    value =
                        if (userProfile.weight.isBlank()) {
                            ""
                        } else {
                            "${userProfile.weight} kg"
                        },
                    onEdit = {
                        editingField = EditableProfileField.WEIGHT
                        editingValue = userProfile.weight
                        editingError = ""
                    }
                )

                EditableInfoCard(
                    emoji = "📏",
                    title = "Height",
                    value =
                        if (userProfile.height.isBlank()) {
                            ""
                        } else {
                            "${userProfile.height} cm"
                        },
                    onEdit = {
                        editingField = EditableProfileField.HEIGHT
                        editingValue = userProfile.height
                        editingError = ""
                    }
                )

                EditableInfoCard(
                    emoji = "🎂",
                    title = "Age",
                    value =
                        if (userProfile.age.isBlank()) {
                            ""
                        } else {
                            "${userProfile.age} years"
                        },
                    onEdit = {
                        editingField = EditableProfileField.AGE
                        editingValue = userProfile.age
                        editingError = ""
                    }
                )

                Spacer(modifier = Modifier.height(95.dp))
            }
        }
    }

    if (editingField != null) {

        EditProfileFieldDialog(
            field = editingField!!,
            value = editingValue,
            error = editingError,
            onValueChange = { newValue ->

                editingValue =
                    when (editingField) {

                        EditableProfileField.NAME ->
                            newValue.filter { character ->
                                character.isLetter() || character == ' '
                            }

                        EditableProfileField.WEIGHT,
                        EditableProfileField.HEIGHT,
                        EditableProfileField.AGE ->
                            newValue
                                .filter { character ->
                                    character.isDigit()
                                }
                                .take(3)

                        EditableProfileField.BLOOD_GROUP ->
                            newValue

                        null ->
                            newValue
                    }
            },
            onDismiss = {
                editingField = null
                editingValue = ""
                editingError = ""
            },
            onSave = {

                val error =
                    when (editingField) {

                        EditableProfileField.NAME ->
                            ProfileValidationUtils.validateName(editingValue)

                        EditableProfileField.BLOOD_GROUP ->
                            ProfileValidationUtils.validateBloodGroup(editingValue)

                        EditableProfileField.WEIGHT ->
                            ProfileValidationUtils.validateWeight(editingValue)

                        EditableProfileField.HEIGHT ->
                            ProfileValidationUtils.validateHeight(editingValue)

                        EditableProfileField.AGE ->
                            ProfileValidationUtils.validateAge(editingValue)

                        null ->
                            "Invalid field."
                    }

                if (error != null) {

                    editingError = error

                } else {

                    when (editingField) {

                        EditableProfileField.NAME ->
                            userProfileViewModel.updateName(editingValue)

                        EditableProfileField.BLOOD_GROUP ->
                            userProfileViewModel.updateBloodGroup(editingValue)

                        EditableProfileField.WEIGHT ->
                            userProfileViewModel.updateWeight(editingValue)

                        EditableProfileField.HEIGHT ->
                            userProfileViewModel.updateHeight(editingValue)

                        EditableProfileField.AGE ->
                            userProfileViewModel.updateAge(editingValue)

                        null -> {}
                    }

                    editingField = null
                    editingValue = ""
                    editingError = ""
                }
            }
        )
    }

    if (showAddContactDialog) {

        EmergencyContactDialog(
            title =
                if (contactBeingEdited == null) {
                    "Add Emergency Contact"
                } else {
                    "Edit Emergency Contact"
                },
            value = contactInput,
            error = contactError,
            onValueChange = { input ->
                contactInput =
                    input
                        .filter { it.isDigit() }
                        .take(10)
            },
            onDismiss = {
                showAddContactDialog = false
                contactBeingEdited = null
                contactInput = ""
                contactError = ""
            },
            onSave = {

                val error =
                    ProfileValidationUtils.validateEmergencyContact(contactInput)

                if (error != null) {

                    contactError = error

                } else {

                    if (contactBeingEdited == null) {

                        userProfileViewModel.addEmergencyContact(contactInput)

                    } else {

                        userProfileViewModel.updateEmergencyContactAt(
                            oldContact = contactBeingEdited ?: "",
                            newContact = contactInput
                        )
                    }

                    showAddContactDialog = false
                    contactBeingEdited = null
                    contactInput = ""
                    contactError = ""
                }
            }
        )
    }

    if (contactToDelete != null) {

        AlertDialog(
            onDismissRequest = {
                contactToDelete = null
            },
            containerColor = Color(0xFF111827),
            title = {
                Text(
                    text = "Remove Contact?",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "This contact will no longer receive emergency SMS alerts.",
                    color = Color.LightGray
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        userProfileViewModel.removeEmergencyContact(
                            contactToDelete ?: ""
                        )
                        contactToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFDC2626),
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Remove",
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        contactToDelete = null
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
private fun EmergencyContactsSection(
    contacts: List<String>,
    onAddContact: () -> Unit,
    onEditContact: (String) -> Unit,
    onDeleteContact: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.07f)
        )
    ) {

        Column(
            modifier = Modifier.padding(18.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = "Emergency Contacts",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "SMS alerts will be sent to all saved contacts.",
                        color = Color.LightGray,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                IconButton(
                    onClick = onAddContact
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add contact",
                        tint = Color(0xFF38BDF8)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (contacts.isEmpty()) {

                Text(
                    text = "No emergency contact saved.",
                    color = Color(0xFFFFCDD2),
                    fontWeight = FontWeight.Bold
                )

            } else {

                contacts.forEachIndexed { index, contact ->

                    EmergencyContactRow(
                        index = index + 1,
                        contact = contact,
                        onEdit = {
                            onEditContact(contact)
                        },
                        onDelete = {
                            onDeleteContact(contact)
                        }
                    )

                    if (index != contacts.lastIndex) {
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun EmergencyContactRow(
    index: Int,
    contact: String,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White.copy(alpha = 0.06f))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(Color(0xFFFF1744).copy(alpha = 0.18f)),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = index.toString(),
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = contact,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Text(
                text =
                    if (index == 1) {
                        "Primary contact"
                    } else {
                        "Emergency contact"
                    },
                color = Color.LightGray,
                style = MaterialTheme.typography.bodySmall
            )
        }

        IconButton(
            onClick = onEdit
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit contact",
                tint = Color(0xFF38BDF8)
            )
        }

        IconButton(
            onClick = onDelete
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete contact",
                tint = Color(0xFFFF1744)
            )
        }
    }
}

@Composable
private fun EmergencyContactDialog(
    title: String,
    value: String,
    error: String,
    onValueChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF111827),
        title = {
            Text(
                text = title,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        },
        text = {

            Column {

                OutlinedTextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    label = {
                        Text(text = "Emergency Contact")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    shape = RoundedCornerShape(18.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedLabelColor = Color(0xFF38BDF8),
                        unfocusedLabelColor = Color.LightGray,
                        focusedBorderColor = Color(0xFF38BDF8),
                        unfocusedBorderColor = Color.DarkGray,
                        cursorColor = Color(0xFF38BDF8)
                    )
                )

                if (error.isNotBlank()) {

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = error,
                        color = Color(0xFFFFCDD2),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onSave,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF38BDF8),
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Save",
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss
            ) {
                Text(
                    text = "Cancel",
                    color = Color.White
                )
            }
        }
    )
}

@Composable
private fun ProfileHeaderCard(
    name: String,
    bloodGroup: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.08f)
        )
    ) {

        Row(
            modifier = Modifier.padding(22.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFF1744).copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color(0xFFFF1744),
                    modifier = Modifier.size(42.dp)
                )
            }

            Spacer(modifier = Modifier.width(18.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text =
                        name.ifBlank {
                            "No Name Saved"
                        },
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text =
                        if (bloodGroup.isBlank()) {
                            "Blood group not saved"
                        } else {
                            "Blood Group: $bloodGroup"
                        },
                    color = Color.LightGray
                )
            }
        }
    }
}

@Composable
private fun LanguageChangeCard(
    selectedLanguageCode: String,
    onLanguageSelected: (String) -> Unit
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.07f)
        )
    ) {

        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.08f)),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "🌐",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Spacer(modifier = Modifier.width(15.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = "Language",
                    color = Color.LightGray,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = LanguageUtils.getLanguageName(selectedLanguageCode),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Box {

                TextButton(
                    onClick = {
                        expanded = true
                    }
                ) {

                    Text(
                        text = "Change",
                        color = Color(0xFF38BDF8),
                        fontWeight = FontWeight.Bold
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

@Composable
private fun EditableInfoCard(
    emoji: String,
    title: String,
    value: String,
    onEdit: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 14.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.07f)
        )
    ) {

        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.08f)),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = emoji,
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Spacer(modifier = Modifier.width(15.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = title,
                    color = Color.LightGray,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text =
                        value.ifBlank {
                            "Not saved"
                        },
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            IconButton(
                onClick = onEdit
            ) {

                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit",
                    tint = Color(0xFF38BDF8)
                )
            }
        }
    }
}

@Composable
private fun EditProfileFieldDialog(
    field: EditableProfileField,
    value: String,
    error: String,
    onValueChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF111827),
        title = {

            Text(
                text = "Edit ${fieldTitle(field)}",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        },
        text = {

            Column {

                if (field == EditableProfileField.BLOOD_GROUP) {

                    BloodGroupEditSelector(
                        selectedBloodGroup = value,
                        onBloodGroupSelected = onValueChange
                    )

                } else {

                    OutlinedTextField(
                        value = value,
                        onValueChange = onValueChange,
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        label = {
                            Text(text = fieldTitle(field))
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType =
                                when (field) {

                                    EditableProfileField.NAME ->
                                        KeyboardType.Text

                                    EditableProfileField.WEIGHT,
                                    EditableProfileField.HEIGHT,
                                    EditableProfileField.AGE ->
                                        KeyboardType.Number

                                    EditableProfileField.BLOOD_GROUP ->
                                        KeyboardType.Text
                                }
                        ),
                        shape = RoundedCornerShape(18.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedLabelColor = Color(0xFF38BDF8),
                            unfocusedLabelColor = Color.LightGray,
                            focusedBorderColor = Color(0xFF38BDF8),
                            unfocusedBorderColor = Color.DarkGray,
                            cursorColor = Color(0xFF38BDF8)
                        )
                    )
                }

                if (error.isNotBlank()) {

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = error,
                        color = Color(0xFFFFCDD2),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {

            Button(
                onClick = onSave,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF38BDF8),
                    contentColor = Color.White
                )
            ) {

                Text(
                    text = "Save",
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {

            OutlinedButton(
                onClick = onDismiss
            ) {

                Text(
                    text = "Cancel",
                    color = Color.White
                )
            }
        }
    )
}

@Composable
private fun BloodGroupEditSelector(
    selectedBloodGroup: String,
    onBloodGroupSelected: (String) -> Unit
) {
    Column {

        val rows =
            ProfileValidationUtils.bloodGroups.chunked(4)

        rows.forEach { rowItems ->

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                rowItems.forEach { bloodGroup ->

                    val selected =
                        selectedBloodGroup == bloodGroup

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(42.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                if (selected) {
                                    Color(0xFF38BDF8)
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
                                shape = RoundedCornerShape(14.dp)
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

private fun fieldTitle(
    field: EditableProfileField
): String {
    return when (field) {

        EditableProfileField.NAME ->
            "Name"

        EditableProfileField.BLOOD_GROUP ->
            "Blood Group"

        EditableProfileField.WEIGHT ->
            "Weight"

        EditableProfileField.HEIGHT ->
            "Height"

        EditableProfileField.AGE ->
            "Age"
    }
}