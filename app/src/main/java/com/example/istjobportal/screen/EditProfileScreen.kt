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
import androidx.compose.runtime.LaunchedEffect
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
fun EditProfileScreen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var skills by remember { mutableStateOf("") }
    var linkedin by remember { mutableStateOf("") }
    var courses by remember { mutableStateOf("") }
    var graduationYear by remember { mutableStateOf("") }
    var currentJob by remember { mutableStateOf("") }
    var experiences by remember { mutableStateOf("") }

    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    LaunchedEffect(Unit) {
        // Fetch existing profile data
        val userId = auth.currentUser?.uid ?: return@LaunchedEffect
        db.collection("alumniProfiles").document(userId).get()
            .addOnSuccessListener { document ->
                document?.let {
                    name = it.getString("name") ?: ""
                    bio = it.getString("bio") ?: ""
                    phone = it.getString("phone") ?: ""
                    location = it.getString("location") ?: ""
                    skills = (it.get("skills") as? List<String>)?.joinToString(", ") ?: ""
                    linkedin = it.getString("linkedin") ?: ""
                    courses = (it.get("courses") as? List<String>)?.joinToString(", ") ?: ""
                    graduationYear = it.getString("graduationYear") ?: ""
                    currentJob = it.getString("currentJob") ?: ""
                    experiences = it.getString("experiences") ?: ""
                }
            }
    }

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

        TextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("Location") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = skills,
            onValueChange = { skills = it },
            label = { Text("Skills (comma separated)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = linkedin,
            onValueChange = { linkedin = it },
            label = { Text("LinkedIn") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = courses,
            onValueChange = { courses = it },
            label = { Text("Courses (comma separated)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = graduationYear,
            onValueChange = { graduationYear = it },
            label = { Text("Graduation Year") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = currentJob,
            onValueChange = { currentJob = it },
            label = { Text("Current Job") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = experiences,
            onValueChange = { experiences = it },
            label = { Text("Experiences") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onClick@{
                val userId = auth.currentUser?.uid ?: return@onClick
                val profile = hashMapOf(
                    "name" to name,
                    "bio" to bio,
                    "phone" to phone,
                    "location" to location,
                    "skills" to skills.split(",").map { it.trim() },  // Converting comma-separated string to a list
                    "linkedin" to linkedin,
                    "courses" to courses.split(",").map { it.trim() },  // Converting comma-separated string to a list
                    "graduationYear" to graduationYear,
                    "currentJob" to currentJob,
                    "experiences" to experiences
                )

                db.collection("alumniProfiles").document(userId).set(profile)
                    .addOnSuccessListener {
                        Toast.makeText(navController.context, "Profile updated", Toast.LENGTH_SHORT).show()
                        navController.navigate(Screens.ViewProfileScreen.route)
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(navController.context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Changes")
        }
    }
}
