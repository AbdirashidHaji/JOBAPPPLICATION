package com.example.istjobportal.screen

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.example.istjobportal.nav.Screens
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

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

    // Calendar instances for picking the dates
    val calendar = Calendar.getInstance()

    // DatePickerDialog for Start Date
    val startDatePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            startDate = "$dayOfMonth/${month + 1}/$year" // Set selected date
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    // DatePickerDialog for Expiry Date
    val expiryDatePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            expiryDate = "$dayOfMonth/${month + 1}/$year" // Set selected date
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    // Fetch job details by jobId from Firestore when the screen is displayed
    LaunchedEffect(jobId) {
        db.collection("jobs").document(jobId).get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    title = documentSnapshot.getString("title") ?: ""
                    description = documentSnapshot.getString("description") ?: ""
                    company = documentSnapshot.getString("company") ?: ""
                    startDate = documentSnapshot.getString("startDate") ?: ""
                    expiryDate = documentSnapshot.getString("expiryDate") ?: ""
                    vacancies = documentSnapshot.getLong("vacancies")?.toString() ?: ""
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

        // Start Date Input (Click to open DatePickerDialog)
        OutlinedTextField(
            value = startDate,
            onValueChange = { },
            label = { Text("Start Date") },
            modifier = Modifier
                .clickable { startDatePickerDialog.show() }
                .fillMaxWidth(),
            enabled = false // Prevent typing, but allow click to trigger DatePicker
        )

        // Expiry Date Input (Click to open DatePickerDialog)
        OutlinedTextField(
            value = expiryDate,
            onValueChange = { },
            label = { Text("Expiry Date") },
            modifier = Modifier
                .clickable { expiryDatePickerDialog.show() }
                .fillMaxWidth(),
            enabled = false // Prevent typing, but allow click to trigger DatePicker
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

                // Update the job in Firestore by jobId
                db.collection("jobs").document(jobId)
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
