package com.example.istjobportal.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.istjobportal.nav.Screens
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun ManageJobsScreen(navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    val jobs = remember { mutableStateListOf<Map<String, Any>>() }

    // Load all jobs
    LaunchedEffect(Unit) {
        db.collection("jobListings").get().addOnSuccessListener { result ->
            jobs.clear()
            for (document in result) {
                jobs.add(document.data + ("id" to document.id))
            }
        }
    }

    LazyColumn {
        items(jobs) { job ->
            Row(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = job["jobTitle"] as String)
                    // Display other details
                }
                Button(onClick = { navController.navigate(Screens.EditJobScreen.route + "/${job["id"]}") }) {
                    Text("Edit")
                }
                Button(onClick = { navController.navigate(Screens.DeleteJobScreen.route + "/${job["id"]}") }) {
                    Text("Delete")
                }
            }
        }
    }
}
