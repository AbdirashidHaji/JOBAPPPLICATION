package com.example.istjobportal.screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.istjobportal.nav.Screens
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun ApplicationStatusScreen(navController: NavHostController) {
    val context = LocalContext.current
    val applications = remember { mutableStateListOf<Map<String, Any>>() } // Store application details
    var isLoading by remember { mutableStateOf(true) } // Loading state

    // Firestore instance
    val db = FirebaseFirestore.getInstance()

    // Fetch all applications
    LaunchedEffect(Unit) {
        isLoading = true
        db.collection("applications")
            .get()
            .addOnSuccessListener { result ->
                applications.clear()
                for (document in result) {
                    val applicationData = document.data.toMutableMap()
                    applicationData["id"] = document.id // Add document ID to the application data
                    applications.add(applicationData)
                }
                isLoading = false
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Failed to load applications: ${e.message}", Toast.LENGTH_SHORT).show()
                isLoading = false
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Application Status",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        } else {
            if (applications.isEmpty()) {
                Text(text = "No applications found", style = MaterialTheme.typography.bodyMedium)
            } else {
                LazyColumn {
                    items(applications) { application ->
                        ApplicationStatusItem(application = application)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Button to navigate back to the dashboard
        Button(onClick = { navController.navigate(Screens.DashboardScreen.route + "/alumni") }) {
            Text("Back to Dashboard")
        }
    }
}

@Composable
fun ApplicationStatusItem(application: Map<String, Any>) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Job ID: ${application["jobId"]}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Applicant Name: ${application["applicantName"]}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Email: ${application["applicantEmail"]}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Phone: ${application["applicantPhone"]}", style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(8.dp))

            // Display application status
            val status = application["status"] as? String ?: "Pending"
            Text(text = "Status: $status", style = MaterialTheme.typography.bodyMedium)

            // Display feedback if available
            application["feedback"]?.let {
                Text(text = "Feedback: $it", style = MaterialTheme.typography.bodyMedium)
            } ?: run {
                Text(text = "Feedback: No feedback available", style = MaterialTheme.typography.bodyMedium)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
