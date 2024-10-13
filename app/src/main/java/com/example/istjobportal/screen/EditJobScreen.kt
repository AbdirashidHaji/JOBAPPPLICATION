package com.example.istjobportal.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.istjobportal.nav.Screens
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun EditJobScreen(navController: NavController) {
    var jobTitle by remember { mutableStateOf("") }
    var jobDescription by remember { mutableStateOf("") }
    var jobLocation by remember { mutableStateOf("") }
    var salary by remember { mutableStateOf("") }
    var experience by remember { mutableStateOf("") }
    var educationLevel by remember { mutableStateOf("") }
    var company by remember { mutableStateOf("") }
    var jobType by remember { mutableStateOf("") }
    var deadlineDate by remember { mutableStateOf("") }
    var dateAdded by remember { mutableStateOf("") }

    val db = FirebaseFirestore.getInstance()
    val context = LocalContext.current

    // Load job details
    LaunchedEffect(JobScreen(navController = navController)) {
        db.collection("jobListings").document().get().addOnSuccessListener { document ->
            jobTitle = document.getString("jobTitle") ?: ""
            jobDescription = document.getString("jobDescription") ?: ""
            jobLocation = document.getString("jobLocation") ?: ""
            salary = document.getString("salary") ?: ""
            experience = document.getString("experience") ?: ""
            educationLevel = document.getString("educationLevel") ?: ""
            company = document.getString("company") ?: ""
            jobType = document.getString("jobType") ?: ""
            deadlineDate = document.getString("deadlineDate") ?: ""
            dateAdded = document.getString("dateAdded") ?: ""
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        TextField(value = jobTitle, onValueChange = { jobTitle = it }, label = { Text("Job Title") })
        // Add other fields like jobDescription, salary, etc., as in AddJobScreen

        Button(onClick = {
            val updatedJobDetails = mapOf(
                "jobTitle" to jobTitle,
                "jobDescription" to jobDescription,
                // other fields
            )
            db.collection("jobListings").document().update(updatedJobDetails)
                .addOnSuccessListener {
                    Toast.makeText(context, "Job updated successfully", Toast.LENGTH_SHORT).show()
                    navController.navigate(Screens.ManageJobsScreen.route)
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Failed to update job: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }) {
            Text("Save Changes")
        }
    }
}
