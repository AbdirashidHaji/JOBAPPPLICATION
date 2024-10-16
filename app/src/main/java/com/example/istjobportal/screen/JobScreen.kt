package com.example.istjobportal.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.istjobportal.nav.Screens
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun JobScreen(navController: NavController) {
    val context = LocalContext.current
    val jobs = remember { mutableStateListOf<Map<String, Any>>() } // Mutable list to hold jobs
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
                    JobItem(job = job) {
                        // Navigate to application screen
                        navController.navigate("${Screens.ApplicationScreen.route}/${job["id"]}")
                    }
                }
            }
        }
    }
}

@Composable
fun JobItem(job: Map<String, Any>, onApplyClick: () -> Unit) {
    Card(modifier = Modifier.padding(8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = job["title"] as String, style = MaterialTheme.typography.titleMedium)
            Text(text = "Company: ${job["company"]}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Description: ${job["description"]}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Start Date: ${job["startDate"]}", style = MaterialTheme.typography.titleSmall)
            Text(text = "Expiry Date: ${job["expiryDate"]}", style = MaterialTheme.typography.titleSmall)
            Text(text = "Vacancies: ${job["vacancies"]}", style = MaterialTheme.typography.titleMedium)

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = onApplyClick) {
                Text("Apply")
            }
        }
    }
}

