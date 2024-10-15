package com.example.istjobportal.screen

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.istjobportal.nav.Screens
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun EditProfileScreen(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var skills by remember { mutableStateOf("") }
    var linkedin by remember { mutableStateOf("") }
    var currentJob by remember { mutableStateOf("") }
    var graduationYear by remember { mutableStateOf("") }
    var courses by remember { mutableStateOf("") }
    var experiences by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            db.collection("alumniProfiles").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        name = document.getString("name") ?: ""
                        bio = document.getString("bio") ?: ""
                        phone = document.getString("phone") ?: ""
                        location = document.getString("location") ?: ""
                        skills = document.get("skills")?.let { it as List<String> }?.joinToString(", ") ?: ""
                        linkedin = document.getString("linkedin") ?: ""
                        currentJob = document.getString("currentJob") ?: ""
                        graduationYear = document.getString("graduationYear") ?: ""
                        courses = document.get("courses")?.let { it as List<String> }?.joinToString(", ") ?: ""
                        experiences = document.getString("experiences") ?: ""
                    }
                    isLoading = false
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Error getting document: ${e.message}", Toast.LENGTH_SHORT).show()
                    isLoading = false
                }
        }
    }

    if (isLoading) {
        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Profile Information Section
            item {
                Text("Edit Profile", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(bottom = 16.dp))
            }

            item {
                Card(modifier = Modifier.fillMaxWidth(), ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        TextField(value = name, onValueChange = { name = it }, label = { Text("Name") }, modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(value = bio, onValueChange = { bio = it }, label = { Text("Bio") }, modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone") }, modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(value = location, onValueChange = { location = it }, label = { Text("Location") }, modifier = Modifier.fillMaxWidth())
                    }
                }
            }

            // Skills Section
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Card(modifier = Modifier.fillMaxWidth(), ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        TextField(value = skills, onValueChange = { skills = it }, label = { Text("Skills (comma separated)") }, modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(value = linkedin, onValueChange = { linkedin = it }, label = { Text("LinkedIn Profile") }, modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(value = currentJob, onValueChange = { currentJob = it }, label = { Text("Current Job") }, modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(value = graduationYear, onValueChange = { graduationYear = it }, label = { Text("Graduation Year") }, modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(value = courses, onValueChange = { courses = it }, label = { Text("Courses (comma separated)") }, modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(value = experiences, onValueChange = { experiences = it }, label = { Text("Experiences") }, modifier = Modifier.fillMaxWidth())
                    }
                }
            }

            // Save Changes Button
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        val userId = auth.currentUser?.uid
                        if (userId != null) {
                            val profile = hashMapOf(
                                "name" to name,
                                "bio" to bio,
                                "phone" to phone,
                                "location" to location,
                                "skills" to skills.split(",").map { it.trim() },
                                "linkedin" to linkedin,
                                "currentJob" to currentJob,
                                "graduationYear" to graduationYear,
                                "courses" to courses.split(",").map { it.trim() },
                                "experiences" to experiences
                            )
                            db.collection("alumniProfiles").document(userId).set(profile)
                                .addOnSuccessListener {
                                    Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                                    navController.popBackStack()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(context, "Failed to update profile: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save Changes")
                }
            }
        }
    }
}
