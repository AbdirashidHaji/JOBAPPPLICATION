package com.example.istjobportal.screen

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.istjobportal.nav.Screens
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

@Composable
fun CreateProfileScreen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var profilePhotoUri by remember { mutableStateOf<Uri?>(null) }
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
    val storageRef = FirebaseStorage.getInstance().reference

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            profilePhotoUri = uri
        }
    )

    val context = LocalContext.current
    val scrollState = rememberScrollState()
    var step by remember { mutableIntStateOf(1) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (step) {
            1 -> {
                Text("Step 1: Basic Information", style = MaterialTheme.typography.titleSmall)
                Spacer(modifier = Modifier.height(16.dp))

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
                    onClick = { step = 2 },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Next")
                }
            }

            2 -> {
                Text("Step 2: Contact Information", style = MaterialTheme.typography.titleSmall)
                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
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

                Button(
                    onClick = { step = 3 },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Next")
                }
            }

            3 -> {
                Text("Step 3: Professional Information", style = MaterialTheme.typography.titleSmall)
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
                    label = { Text("LinkedIn Profile") },
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

                Button(
                    onClick = { step = 4 },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Next")
                }
            }

            4 -> {
                Text("Step 4: Education & Experience", style = MaterialTheme.typography.titleSmall)
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
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = experiences,
                    onValueChange = { experiences = it },
                    label = { Text("Experiences") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { step = 5 },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Next")
                }
            }

            5 -> {
                Text("Step 5: Upload Profile Photo", style = MaterialTheme.typography.titleSmall)
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { launcher.launch("image/*") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Select Profile Photo")
                }

                profilePhotoUri?.let {
                    Spacer(modifier = Modifier.height(16.dp))
                    Image(
                        painter = rememberAsyncImagePainter(it),
                        contentDescription = "Profile Photo",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color.Gray, CircleShape)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        profilePhotoUri?.let { uri ->
                            val photoRef = storageRef.child("profilePhotos/${auth.currentUser!!.uid}.jpg")
                            val uploadTask = photoRef.putFile(uri)

                            uploadTask.continueWithTask { task ->
                                if (!task.isSuccessful) {
                                    task.exception?.let { throw it }
                                }
                                photoRef.downloadUrl
                            }.addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val downloadUri = task.result
                                    val profile = hashMapOf(
                                        "name" to name,
                                        "bio" to bio,
                                        "profilePhotoUri" to downloadUri.toString(),
                                        "phone" to phone,
                                        "location" to location,
                                        "skills" to skills.split(",").map { it.trim() },
                                        "linkedin" to linkedin,
                                        "courses" to courses.split(",").map { it.trim() },
                                        "graduationYear" to graduationYear,
                                        "currentJob" to currentJob,
                                        "experiences" to experiences,
                                        "userId" to auth.currentUser?.uid
                                    )
                                    db.collection("alumniProfiles").document(auth.currentUser!!.uid).set(profile)
                                        .addOnSuccessListener {
                                            Toast.makeText(context, "Profile created successfully", Toast.LENGTH_SHORT).show()
                                            // Navigate to the dashboard screen after successful profile creation
                                            navController.navigate(Screens.DashboardScreen.route + "/alumni")

                                        }
                                        .addOnFailureListener { e ->
                                            Toast.makeText(context, "Failed to create profile: ${e.message}", Toast.LENGTH_SHORT).show()
                                        }
                                } else {
                                    Toast.makeText(context, "Failed to upload image", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Create Profile")
                }
            }
        }
    }
}
