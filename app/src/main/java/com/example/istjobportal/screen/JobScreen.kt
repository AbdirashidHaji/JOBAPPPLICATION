package com.example.istjobportal.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun JobScreen(navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    val jobs = remember { mutableStateListOf<Map<String, Any>>() }

    // Load jobs from Firestore
    LaunchedEffect(Unit) {
        db.collection("jobListings").get().addOnSuccessListener { result ->
            jobs.clear()
            for (document in result) {
                jobs.add(document.data + ("id" to document.id))
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Job Listings",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (jobs.isEmpty()) {
            Text(
                text = "No job listings available",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else {
            LazyColumn {
                items(jobs) { job ->
                    JobItem(
                        jobTitle = job["jobTitle"] as? String ?: "Unknown",
                        jobDescription = job["jobDescription"] as? String ?: "No description",
                        jobLocation = job["jobLocation"] as? String ?: "Unknown location",
                        onClick = {
                            // Navigate to a detailed job page if necessary
                            // navController.navigate(Screens.JobDetailScreen.route + "/${job["id"]}")
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun JobItem(
    jobTitle: String,
    jobDescription: String,
    jobLocation: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(8.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = jobTitle, style = MaterialTheme.typography.titleSmall)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = jobDescription, maxLines = 2, overflow = TextOverflow.Ellipsis)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Location: $jobLocation", style = MaterialTheme.typography.bodySmall)
        }
    }
}
