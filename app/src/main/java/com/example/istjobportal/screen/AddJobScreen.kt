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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.istjobportal.nav.Screens
import com.google.firebase.auth.FirebaseAuth
import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

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

    // for picking the dates
    val calendar = Calendar.getInstance()

    //  Start Date
    val startDatePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            startDate = "$dayOfMonth/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    // Expiry Date
    val expiryDatePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            expiryDate = "$dayOfMonth/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.Gray),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
            onValueChange = { },
            label = { Text("Start Date") },
            modifier = Modifier
                .clickable { startDatePickerDialog.show() }
                .fillMaxWidth(),
            enabled = false
        )

        // Expiry Date Input
        OutlinedTextField(
            value = expiryDate,
            onValueChange = { },
            label = { Text("Expiry Date") },
            modifier = Modifier
                .clickable { expiryDatePickerDialog.show() }
                .fillMaxWidth(),
            enabled = false
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
