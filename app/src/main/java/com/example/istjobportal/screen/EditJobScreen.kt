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
fun EditJobScreen(navController: NavHostController) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var company by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf("") }
    var vacancies by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()

    // Fetch the first job details from Firestore when the screen is displayed
    LaunchedEffect(Unit) {
        db.collection("jobs").limit(1).get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val document = querySnapshot.documents[0]
                    title = document.getString("title") ?: ""
                    description = document.getString("description") ?: ""
                    company = document.getString("company") ?: ""
                    startDate = document.getString("startDate") ?: ""
                    expiryDate = document.getString("expiryDate") ?: ""
                    vacancies = document.getLong("vacancies")?.toString() ?: ""
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Failed to load job: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.Gray),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            navController.navigate(Screens.DashboardScreen.route) {
                popUpTo(Screens.DashboardScreen.route) { inclusive = true }
            }
        }) {
            Text("Go Back")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Edit Job", style = MaterialTheme.typography.headlineMedium)

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
                // Update job entry
                val jobUpdates = hashMapOf(
                    "title" to title,
                    "description" to description,
                    "company" to company,
                    "startDate" to startDate,
                    "expiryDate" to expiryDate,
                    "vacancies" to (vacancies.toIntOrNull() ?: 0)
                ) as MutableMap<String, Any>

                // Update the first job in Firestore
                db.collection("jobs").limit(1).get()
                    .addOnSuccessListener { querySnapshot ->
                        if (!querySnapshot.isEmpty) {
                            val documentId = querySnapshot.documents[0].id
                            db.collection("jobs").document(documentId)
                                .update(jobUpdates)
                                .addOnSuccessListener {
                                    Toast.makeText(context, "Job updated successfully", Toast.LENGTH_SHORT).show()
                                    navController.navigate("${Screens.DashboardScreen.route}/admin") {
                                        popUpTo(Screens.DashboardScreen.route) { inclusive = true }
                                    }
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(context, "Failed to update job: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(context, "Failed to load job for update: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                    .addOnCompleteListener {
                        isLoading = false
                    }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            Text("Update Job")
        }
    }
}
