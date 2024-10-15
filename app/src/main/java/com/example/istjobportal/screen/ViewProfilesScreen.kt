package com.example.istjobportal.screen

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun ViewProfilesScreen(navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    var profiles by remember { mutableStateOf<List<Pair<String, String>>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        db.collection("alumniProfiles").get()
            .addOnSuccessListener { documents ->
                profiles = documents.flatMap { document ->
                    listOf(
                        Pair("Name", document.getString("name") ?: "No Name"),
                        Pair("Bio", document.getString("bio") ?: "No Bio"),
                        Pair("Phone", document.getString("phone") ?: "No Phone"),
                        Pair("Location", document.getString("location") ?: "No Location"),
                        Pair("Skills", (document.get("skills") as? List<*>)?.joinToString(", ") ?: "No Skills"),
                        Pair("LinkedIn", document.getString("linkedin") ?: "No LinkedIn"),
                        Pair("Courses", (document.get("courses") as? List<*>)?.joinToString(", ") ?: "No Courses"),
                        Pair("Graduation Year", document.getString("graduationYear") ?: "No Graduation Year"),
                        Pair("Current Job", document.getString("currentJob") ?: "No Current Job"),
                        Pair("Experiences", (document.get("experiences") as? List<*>)?.joinToString(", ") ?: "No Experiences")
                    )
                }
                isLoading = false // Set loading to false after data is fetched
            }
            .addOnFailureListener { e ->
                // Handle the failure case here
                Log.e("ViewProfilesScreen", "Error fetching profiles", e)
                isLoading = false // Set loading to false on error
            }
    }

    if (isLoading) {
        // Show a loading indicator while fetching profiles
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            item {
                Text(text = "Alumni Profiles", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(bottom = 16.dp))
            }

            items(profiles) { profile ->
                val (key, value) = profile // Destructure the pair here
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "$key: $value", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }
}
