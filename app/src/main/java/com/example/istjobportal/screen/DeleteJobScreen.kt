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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.istjobportal.nav.Screens
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun DeleteJobScreen(navController: NavHostController) {
    var jobId by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var isDeleted by remember { mutableStateOf(false) }
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
            navController.navigate("${Screens.DashboardScreen.route}/admin") {
                popUpTo(Screens.DashboardScreen.route) { inclusive = true }
            }
        }) {
            Text("Go Back")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Delete Job", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // Job ID Input
        OutlinedTextField(
            value = jobId,
            onValueChange = { jobId = it },
            label = { Text("Job ID") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        }

        Button(
            onClick = {
                isLoading = true

                // Delete the job from Firestore
                db.collection("jobs").document(jobId)
                    .delete()
                    .addOnSuccessListener {
                        Toast.makeText(context, "Job deleted successfully", Toast.LENGTH_SHORT).show()
                        isDeleted = true
                        jobId = "" // Clear the job ID input
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(context, "Failed to delete job: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                    .addOnCompleteListener {
                        isLoading = false
                    }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            Text("Delete Job")
        }

        // Optional: Show confirmation message after successful deletion
        if (isDeleted) {
            Text(text = "Job deleted!", color = Color.Green, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
