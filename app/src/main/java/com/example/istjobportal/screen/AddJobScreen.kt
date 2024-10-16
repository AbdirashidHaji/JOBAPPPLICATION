package com.example.istjobportal.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.istjobportal.nav.Screens
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun AddJobScreen(navController: NavHostController) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var company by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf("") }
    var vacancies by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Firestore instance
    val db = FirebaseFirestore.getInstance()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.Gray),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Go Back Button
        Button(onClick = {
            navController.navigate(Screens.DashboardScreen.route) {
                popUpTo(Screens.DashboardScreen.route) { inclusive = true }
            }
        }) {
            Text("Go Back")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Add Job", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // Job Title Input
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Job Title") }
        )

        // Job Description Input
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Job Description") },
            modifier = Modifier.height(100.dp)
        )

        // Company Input
        OutlinedTextField(
            value = company,
            onValueChange = { company = it },
            label = { Text("Company") }
        )

        // Start Date Input
        OutlinedTextField(
            value = startDate,
            onValueChange = { startDate = it },
            label = { Text("Start Date (e.g., 01/01/2024)") }
        )

        // Expiry Date Input
        OutlinedTextField(
            value = expiryDate,
            onValueChange = { expiryDate = it },
            label = { Text("Expiry Date (e.g., 01/31/2024)") }
        )

        // Vacancies Input
        OutlinedTextField(
            value = vacancies,
            onValueChange = { vacancies = it },
            label = { Text("Number of Vacancies") }
        )

        Spacer(modifier = Modifier.height(16.dp))


        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        }

        Button(
            onClick = {
                isLoading = true
                // Create a new job entry
                val job = hashMapOf(
                    "title" to title,
                    "description" to description,
                    "company" to company,
                    "startDate" to startDate,
                    "expiryDate" to expiryDate,
                    "vacancies" to (vacancies.toIntOrNull() ?: 0)
                )

                // Add the job to Firestore
                db.collection("jobs")
                    .add(job)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Job added successfully", Toast.LENGTH_SHORT).show()
                        navController.navigate("${Screens.DashboardScreen.route}/admin") {
                            popUpTo(Screens.DashboardScreen.route) { inclusive = true }
                        }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(context, "Failed to add job: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                    .addOnCompleteListener {
                        isLoading = false
                    }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            Text("Add Job")
        }
    }
}
