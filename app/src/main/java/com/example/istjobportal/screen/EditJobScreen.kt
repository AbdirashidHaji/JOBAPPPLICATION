package com.example.istjobportal.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.istjobportal.nav.Screens
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun EditJobScreen(navController: NavHostController, jobId: String) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var company by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf("") }
    var vacancies by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()

    // Fetch job data from Firestore
    LaunchedEffect(jobId) {
        db.collection("jobs").document(jobId).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    title = document.getString("title") ?: ""
                    description = document.getString("description") ?: ""
                    company = document.getString("company") ?: ""
                    startDate = document.getString("startDate") ?: ""
                    expiryDate = document.getString("expiryDate") ?: ""
                    vacancies = document.getLong("vacancies")?.toString() ?: ""
                }
            }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp).background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { navController.navigate("${Screens.DashboardScreen.route}/admin") { popUpTo(Screens.DashboardScreen.route) { inclusive = true } } }) {
            Text("Go Back")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Edit Job", style = MaterialTheme.typography.headlineMedium)

        // Job Title Input
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Job Title") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Job Description Input
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Job Description") },
            modifier = Modifier.height(100.dp).fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Company Input
        OutlinedTextField(
            value = company,
            onValueChange = { company = it },
            label = { Text("Company") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Start Date Input
        OutlinedTextField(
            value = startDate,
            onValueChange = { startDate = it },
            label = { Text("Start Date (e.g., 01/01/2024)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Expiry Date Input
        OutlinedTextField(
            value = expiryDate,
            onValueChange = { expiryDate = it },
            label = { Text("Expiry Date (e.g., 01/31/2024)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Vacancies Input
        OutlinedTextField(
            value = vacancies,
            onValueChange = { vacancies = it },
            label = { Text("Number of Vacancies") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Show CircularProgressIndicator when loading
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        }

        Button(
            onClick = {
                isLoading = true
                val jobUpdates = hashMapOf(
                    "title" to title,
                    "description" to description,
                    "company" to company,
                    "startDate" to startDate,
                    "expiryDate" to expiryDate,
                    "vacancies" to (vacancies.toIntOrNull() ?: 0)
                )
                db.collection("jobs").document(jobId)
                    .set(jobUpdates)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Job updated successfully", Toast.LENGTH_SHORT).show()
                        navController.navigate("${Screens.DashboardScreen.route}/admin") { popUpTo(Screens.DashboardScreen.route) { inclusive = true } }
                    }
                    .addOnFailureListener { e -> Toast.makeText(context, "Failed to update job: ${e.message}", Toast.LENGTH_SHORT).show() }
                    .addOnCompleteListener { isLoading = false }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading // Disable button while loading
        ) {
            Text("Update Job")
        }
    }
}
