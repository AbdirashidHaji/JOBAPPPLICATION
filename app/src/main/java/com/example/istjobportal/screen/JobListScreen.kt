package com.example.istjobportal.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.istjobportal.nav.Screens
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun JobListScreen(navController: NavController) {
    val context = LocalContext.current
    val jobs = remember { mutableStateListOf<Map<String, Any>>() }
    var isLoading by remember { mutableStateOf(true) }

    // Firestore instance
    val db = FirebaseFirestore.getInstance()

    // Fetch jobs from Firestore
    LaunchedEffect(Unit) {
        db.collection("jobs")
            .get()
            .addOnSuccessListener { result ->
                jobs.clear()
                for (document in result) {
                    val jobData = document.data.toMutableMap()
                    jobData["id"] = document.id
                    jobs.add(jobData)
                }
                isLoading = false
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Failed to load jobs: ${e.message}", Toast.LENGTH_SHORT).show()
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
        Text(text = "Available Jobs", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        } else {
            LazyColumn {
                items(jobs) { job ->
                    JobListItem(navController = navController, job = job) { deletedJobId ->
                        // Remove the job from the list after deletion
                        jobs.removeAll { it["id"] == deletedJobId }
                    }
                }
            }
        }
    }
}

@Composable
fun JobListItem(navController: NavController, job: Map<String, Any>, onJobDeleted: (String) -> Unit) {
    val context = LocalContext.current
    var showUpdateDialog by remember { mutableStateOf(false) }
    var showDeleteConfirmationDialog by remember { mutableStateOf(false) }

    Card(modifier = Modifier.padding(8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = job["title"] as String, style = MaterialTheme.typography.titleMedium)
            Text(text = "Company: ${job["company"]}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Description: ${job["description"]}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Start Date: ${job["startDate"]}", style = MaterialTheme.typography.titleSmall)
            Text(text = "Expiry Date: ${job["expiryDate"]}", style = MaterialTheme.typography.titleSmall)
            Text(text = "Vacancies: ${job["vacancies"]}", style = MaterialTheme.typography.titleMedium)

            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                // Delete Button
                Button(
                    onClick = { showDeleteConfirmationDialog = true },
                    modifier = Modifier.weight(1f),
                ) {
                    Text(text = "Delete")
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Update Button
                Button(
                    onClick = {
                        showUpdateDialog = true
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Update")
                }
            }

            // Confirmation dialog for deletion
            if (showDeleteConfirmationDialog) {
                AlertDialog(
                    onDismissRequest = { showDeleteConfirmationDialog = false },
                    title = { Text(text = "Confirm Deletion") },
                    text = { Text("Are you sure you want to delete this job?") },
                    confirmButton = {
                        Button(
                            onClick = {
                                val db = FirebaseFirestore.getInstance()
                                val jobId = job["id"] as String

                                // Delete the job from Firestore
                                db.collection("jobs").document(jobId)
                                    .delete()
                                    .addOnSuccessListener {
                                        Toast.makeText(context, "Job deleted successfully", Toast.LENGTH_SHORT).show()
                                        onJobDeleted(jobId)  // Remove job from the list
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(context, "Failed to delete job: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }

                                showDeleteConfirmationDialog = false // Close confirmation dialog
                            }
                        ) {
                            Text("Yes")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { showDeleteConfirmationDialog = false }) {
                            Text("No")
                        }
                    }
                )
            }

            if (showUpdateDialog) {
                showUpdateDialog(job) {
                    showUpdateDialog = false // Close dialog when done
                }
            }
        }
    }
}

@Composable
fun showUpdateDialog(job: Map<String, Any>, onDismiss: () -> Unit) {
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    val jobId = job["id"] as String

    var title by remember { mutableStateOf(job["title"] as String) }
    var company by remember { mutableStateOf(job["company"] as String) }
    var description by remember { mutableStateOf(job["description"] as String) }

    // Update Dialog
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(text = "Update Job")
        },
        text = {
            Column {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Job Title") }
                )
                TextField(
                    value = company,
                    onValueChange = { company = it },
                    label = { Text("Company") }
                )
                TextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    // Update the job in Firestore
                    val updatedJob = hashMapOf(
                        "title" to title,
                        "company" to company,
                        "description" to description
                    )

                    db.collection("jobs").document(jobId)
                        .update(updatedJob as Map<String, Any>)
                        .addOnSuccessListener {
                            Toast.makeText(context, "Job updated successfully", Toast.LENGTH_SHORT).show()
                            onDismiss()  // Close the dialog
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(context, "Failed to update job: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            ) {
                Text("Update")
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text("Cancel")
            }
        }
    )
}
