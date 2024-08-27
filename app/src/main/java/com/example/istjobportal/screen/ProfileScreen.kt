package com.example.istjobportal.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun ProfileScreen(navController: NavController) {
    var name by remember { mutableStateOf("John Doe") }
    var email by remember { mutableStateOf("john.doe@example.com") }
    var bio by remember { mutableStateOf("This is a brief bio about John.") }
    var isEditing by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile Image
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
                .clickable { /* Handle image change */ }
        ) {
            // Placeholder for profile image
        }

        Spacer(modifier = Modifier.height(16.dp))

        // User Information
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isEditing) {
                BasicTextField(
                    value = TextFieldValue(name),
                    onValueChange = { name = it.text },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp),
                    singleLine = true
                )
            } else {
                Text(
                    text = name,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (isEditing) {
                BasicTextField(
                    value = TextFieldValue(email),
                    onValueChange = { email = it.text },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp),
                    singleLine = true
                )
            } else {
                Text(
                    text = email,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (isEditing) {
                BasicTextField(
                    value = TextFieldValue(bio),
                    onValueChange = { bio = it.text },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp)
                )
            } else {
                Text(
                    text = bio,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Edit/Save Button
        Button(
            onClick = {
                if (isEditing) {
                    // Save changes
                }
                // Toggle edit mode
                isEditing = !isEditing
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = if (isEditing) "Save" else "Edit")
        }
    }
}
