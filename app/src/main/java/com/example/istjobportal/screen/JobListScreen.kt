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
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import android.util.Log
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

@Composable
fun JobListScreen(navController: NavController) {
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    var jobList by remember { mutableStateOf(listOf<Map<String, Any>>()) }

    // Fetch jobs from Firestore
    LaunchedEffect(Unit) {
        db.collection("jobs")
            .get()
            .addOnSuccessListener { result ->
                jobList = result.documents.mapNotNull { document ->
                    val data = document.data ?: return@mapNotNull null
                    data + ("id" to document.id)  // Add the document ID to the data
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Failed to fetch jobs: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    // LazyColumn to display list of jobs
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        items(jobList) { job ->
            JobListItem(navController, job) { deletedJobId ->
                jobList = jobList.filterNot { it["id"] == deletedJobId }
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
            Text(text = job["title"] as String, style = MaterialTheme.typography.headlineMedium)
            Text(text = "Company: ${job["company"]}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Description: ${job["description"]}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Start Date: ${job["startDate"]}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Expiry Date: ${job["expiryDate"]}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Vacancies: ${job["vacancies"]}", style = MaterialTheme.typography.bodyMedium)

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
                    onClick = { showUpdateDialog = true },
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

            // Update Dialog
            if (showUpdateDialog) {
                ShowUpdateDialog(job) {
                    showUpdateDialog = false // Close dialog when done
                }
            }
        }
    }
}

@Composable
fun ShowUpdateDialog(job: Map<String, Any>, onDismiss: () -> Unit) {
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    val jobId = job["id"] as String

    // Mutable states for all job fields
    var title by remember { mutableStateOf(job["title"] as String) }
    var company by remember { mutableStateOf(job["company"] as String) }
    var description by remember { mutableStateOf(job["description"] as String) }
    var startDate by remember { mutableStateOf(job["startDate"] as String) }
    var expiryDate by remember { mutableStateOf(job["expiryDate"] as String) }
    var vacancies by remember { mutableStateOf(job["vacancies"].toString()) }

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
                TextField(
                    value = startDate,
                    onValueChange = { startDate = it },
                    label = { Text("Start Date (YYYY-MM-DD)") }
                )
                TextField(
                    value = expiryDate,
                    onValueChange = { expiryDate = it },
                    label = { Text("Expiry Date (YYYY-MM-DD)") }
                )
                TextField(
                    value = vacancies,
                    onValueChange = { vacancies = it },
                    label = { Text("Vacancies") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    // Ensure the vacancies field is valid
                    val vacanciesInt = vacancies.toIntOrNull()
                    if (vacanciesInt == null) {
                        Toast.makeText(context, "Please enter a valid number of vacancies", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    // Update the job in Firestore
                    val updatedJob = hashMapOf(
                        "title" to title,
                        "company" to company,
                        "description" to description,
                        "startDate" to startDate,
                        "expiryDate" to expiryDate,
                        "vacancies" to vacanciesInt
                    )

                    Log.d("JobUpdate", "Updating job with ID: $jobId and data: $updatedJob")

                    db.collection("jobs").document(jobId)
                        .set(updatedJob as Map<String, Any>, SetOptions.merge())
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
