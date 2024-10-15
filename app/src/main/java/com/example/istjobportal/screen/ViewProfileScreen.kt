package com.example.istjobportal.screen

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun ViewProfileScreen(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val context = LocalContext.current
    var profile by remember { mutableStateOf<Map<String, Any>?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            db.collection("alumniProfiles").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        profile = document.data
                    } else {
                        Toast.makeText(context, "No such document", Toast.LENGTH_SHORT).show()
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
        profile?.let {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Profile Photo Section
                item {
                    val imageUrl = it["profilePhoto"] as? String
                    imageUrl?.let { url ->
                        Image(
                            painter = rememberImagePainter(url),
                            contentDescription = "Profile Photo",
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Information Section
                item {
                    Text("Profile Information", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(bottom = 8.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
//                        elevation = 4.dp,
                        //backgroundColor = MaterialTheme.colorScheme.surface
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Name: ${it["name"]}", style = MaterialTheme.typography.titleMedium)
                            Text("Bio: ${it["bio"]}", style = MaterialTheme.typography.titleMedium)
                            Text("Phone: ${it["phone"]}", style = MaterialTheme.typography.titleMedium)
                            Text("Location: ${it["location"]}", style = MaterialTheme.typography.titleMedium)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Skills Section
                item {
                    Text("Skills", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(bottom = 8.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        //elevation = 4.dp,
                        //backgroundColor = MaterialTheme.colorScheme.surface
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Skills: ${it["skills"]}", style = MaterialTheme.typography.titleMedium)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // LinkedIn and Current Job Section
                item {
                    Text("Professional Information", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(bottom = 8.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                       // elevation = 4.dp,
                        //backgroundColor = MaterialTheme.colorScheme.surface
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("LinkedIn: ${it["linkedin"]}", style = MaterialTheme.typography.titleMedium)
                            Text("Current Job: ${it["currentJob"]}", style = MaterialTheme.typography.titleMedium)
                            Text("Graduation Year: ${it["graduationYear"]}", style = MaterialTheme.typography.titleMedium)
                            Text("Courses: ${it["courses"]}", style = MaterialTheme.typography.titleMedium)
                            Text("Experiences: ${it["experiences"]}", style = MaterialTheme.typography.titleMedium)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Edit Profile Button
                item {
                    Button(
                        onClick = { navController.navigate("editProfile") },
//                        modifier = Modifier.align(Alignment.CenterHorizontally)
//                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("Edit Profile")
                    }
                }
            }
        } ?: run {
            Text("Profile not found", style = MaterialTheme.typography.titleMedium)
        }
    }
}
