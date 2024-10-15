package com.example.istjobportal.screen

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.istjobportal.nav.Screens
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.nio.file.WatchEvent

@Composable
fun ApplicationScreen(navController: NavController, jobId: String) {
    var applicantName by remember { mutableStateOf("") }
    var applicantEmail by remember { mutableStateOf("") }
    var applicantPhone by remember { mutableStateOf("") }
    var coverLetter by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var resumeUri by remember { mutableStateOf<Uri?>(null) }
    var coverUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    // Create a launcher for selecting files
    val resumeLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        resumeUri = uri
    }

    val coverLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        coverUri = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Apply for Job", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // Applicant Name Field
        OutlinedTextField(
            value = applicantName,
            onValueChange = { applicantName = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Applicant Email Field
        OutlinedTextField(
            value = applicantEmail,
            onValueChange = { applicantEmail = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Applicant Phone Field
        OutlinedTextField(
            value = applicantPhone,
            onValueChange = { applicantPhone = it },
            label = { Text("Phone") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))



        Spacer(modifier = Modifier.height(16.dp))

        // Resume Upload Button
        Button(onClick = { resumeLauncher.launch("application/pdf") }) {
            Text("Upload Resume (PDF)")
        }

        // Display selected resume file name
        resumeUri?.let {
            Text(text = "Selected Resume: ${it.lastPathSegment}", style = MaterialTheme.typography.bodyMedium)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Cover Letter Upload Button
        Button(onClick = { coverLauncher.launch("application/pdf") }) {
            Text("Upload Cover Letter (PDF)")
        }

        // Display selected cover letter file name
        coverUri?.let {
            Text(text = "Selected Cover Letter: ${it.lastPathSegment}", style = MaterialTheme.typography.bodyMedium)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                isLoading = true

                // Submit application to Firestore
                val application = hashMapOf<String, Any>(
                    "jobId" to jobId,
                    "applicantName" to applicantName,
                    "applicantEmail" to applicantEmail,
                    "applicantPhone" to applicantPhone,
                    "coverLetter" to coverLetter
                )

                val db = FirebaseFirestore.getInstance()

                // Upload files to Firebase Storage
                if (resumeUri != null) {
                    val resumeRef = FirebaseStorage.getInstance().reference.child("resumes/${resumeUri!!.lastPathSegment}")
                    resumeRef.putFile(resumeUri!!)
                        .addOnSuccessListener {
                            // Get the download URL and save it to the application
                            resumeRef.downloadUrl.addOnSuccessListener { resumeDownloadUrl ->
                                application["resumeUrl"] = resumeDownloadUrl.toString()
                                uploadApplication(db, application, navController, context)
                            }
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "Failed to upload resume: ${it.message}", Toast.LENGTH_SHORT).show()
                            isLoading = false
                        }
                } else {
                    // No resume uploaded, just upload the application
                    uploadApplication(db, application, navController, context)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading // Disable button while loading
        ) {
            Text("Submit Application")
        }

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        }
    }
}

private fun uploadApplication(
    db: FirebaseFirestore,
    application: HashMap<String, Any>, // Ensure the type is HashMap<String, Any>
    navController: NavController,
    context: Context
) {
    db.collection("applications")
        .add(application)
        .addOnSuccessListener {
            Toast.makeText(context, "Application submitted successfully", Toast.LENGTH_SHORT).show()
            navController.navigate(Screens.DashboardScreen.route + "/alumni") // Navigate to the dashboard after submission
        }
        .addOnFailureListener { e ->
            Toast.makeText(context, "Failed to submit application: ${e.message}", Toast.LENGTH_SHORT).show()
        }
}
