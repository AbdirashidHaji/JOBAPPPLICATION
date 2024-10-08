package com.example.istjobportal.screen

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
    var profiles by remember { mutableStateOf<List<Map<String, String>>>(emptyList()) }

    LaunchedEffect(Unit) {
        db.collection("alumniProfiles").get()
            .addOnSuccessListener { documents ->
                profiles = documents.map { document ->
                    mapOf(
                        "name" to document.getString("name")!!,
                        "bio" to document.getString("bio")!!
                    )
                }
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

        profiles.forEach { profile ->
            Text(text = "Name: ${profile["name"]}")
            Text(text = "Bio: ${profile["bio"]}")
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
