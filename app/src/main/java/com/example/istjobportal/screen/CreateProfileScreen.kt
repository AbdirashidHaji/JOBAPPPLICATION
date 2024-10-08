package com.example.istjobportal.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.istjobportal.nav.Screens
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun CreateProfileScreen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = bio,
            onValueChange = { bio = it },
            label = { Text("Bio") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val profile = hashMapOf(
                    "name" to name,
                    "bio" to bio,
                    "userId" to auth.currentUser?.uid
                )
                db.collection("alumniProfiles").document(auth.currentUser!!.uid).set(profile)
                    .addOnSuccessListener {
                        Toast.makeText(
                            navController.context,
                            "Profile created successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        navController.navigate(Screens.ViewProfileScreen.route)
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(
                            navController.context,
                            "Failed to create profile: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create Profile")
        }
    }
}
