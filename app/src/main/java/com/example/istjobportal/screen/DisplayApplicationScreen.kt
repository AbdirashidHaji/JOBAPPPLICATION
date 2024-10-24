package com.example.istjobportal.screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.istjobportal.nav.Screens
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun DisplayApplicationScreen(navController: NavHostController) {
    val context = LocalContext.current
    val applications = remember { mutableStateListOf<Map<String, Any>>() } // Mutable list to hold applications
    var isLoading by remember { mutableStateOf(true) }

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
                    applicationData["id"] = document.id
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
            text = "All Applications",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        } else {
            LazyColumn {
                items(applications) { application ->
                    ApplicationItem(application = application) { action, feedback ->
                        // Handle the action (approve/reject) and feedback
                        handleApplicationAction(db, application["id"] as String, action, feedback, context, navController)
                    }
                }
            }
        }
    }
}

@Composable
fun ApplicationItem(application: Map<String, Any>, onActionClick: (String, String?) -> Unit) {
    var feedback by remember { mutableStateOf("") }
    var showFeedbackInput by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { /* Handle item click if necessary */ }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Applicant Name: ${application["applicantName"]}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Email: ${application["applicantEmail"]}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Phone: ${application["applicantPhone"]}", style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "Cover Letter:", style = MaterialTheme.typography.bodyMedium)
            Text(text = application["coverLetter"] as String, style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(8.dp))

            // Resume and Cover Letter URLs
            application["resumeUrl"]?.let {
                Text(text = "Resume: $it", style = MaterialTheme.typography.bodyMedium)
            }

            application["coverUrl"]?.let {
                Text(text = "Cover Letter: $it", style = MaterialTheme.typography.bodyMedium)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = { onActionClick("approve", null) }) {
                    Text("Approve")
                }

                Button(onClick = {
                    showFeedbackInput = !showFeedbackInput
                    if (showFeedbackInput) feedback = ""
                }) {
                    Text("Reject")
                }

                if (showFeedbackInput) {
                    OutlinedTextField(
                        value = feedback,
                        onValueChange = { feedback = it },
                        label = { Text("Feedback") },
                        modifier = Modifier.weight(1f)
                    )
                    Button(onClick = { onActionClick("reject", feedback) }) {
                        Text("Submit Feedback")
                    }
                }
            }
        }
    }
}

private fun handleApplicationAction(
    db: FirebaseFirestore,
    applicationId: String,
    action: String,
    feedback: String?,
    context: Context,
    navController: NavHostController
) {
    when (action) {
        "approve" -> {
            // Update the application status to approved
            db.collection("applications").document(applicationId)
                .update("status", "approved")
                .addOnSuccessListener {
                    Toast.makeText(context, "Application approved successfully", Toast.LENGTH_SHORT).show()
                    // Navigate back to the dashboard
                    navController.navigate("${Screens.DashboardScreen.route}/admin") {
                        popUpTo(Screens.DashboardScreen.route) { inclusive = true }
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Failed to approve application: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
        "reject" -> {
            // Create a MutableMap<String, Any> for the update data
            val updateData: MutableMap<String, Any?> = hashMapOf(
                "status" to "rejected",
                "feedback" to feedback
            )

            db.collection("applications").document(applicationId)
                .update(updateData)
                .addOnSuccessListener {
                    Toast.makeText(context, "Application rejected successfully", Toast.LENGTH_SHORT).show()
                    // Navigate back to the dashboard
                    navController.navigate("${Screens.DashboardScreen.route}/admin") {
                        popUpTo(Screens.DashboardScreen.route) { inclusive = true }
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Failed to reject application: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
