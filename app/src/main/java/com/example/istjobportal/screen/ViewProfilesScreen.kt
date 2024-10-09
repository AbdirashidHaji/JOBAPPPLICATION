package com.example.istjobportal.screen

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
            }
            .addOnFailureListener { e ->
                // Handle the failure case here
                Log.e("ViewProfilesScreen", "Error fetching profiles", e)
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Alumni Profiles", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        profiles.forEach { (key, value) ->
            Text(text = "$key: $value")
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
