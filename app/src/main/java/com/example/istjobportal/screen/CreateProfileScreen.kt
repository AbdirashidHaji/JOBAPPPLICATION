package com.example.istjobportal.screen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import android.net.Uri
import android.util.Log
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.istjobportal.utils.AlumniProfileData
import com.example.istjobportal.utils.DegreeChoice
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProfileScreen(navController: NavController) {
    var fullName by remember { mutableStateOf(TextFieldValue()) }
    var email by remember { mutableStateOf(TextFieldValue()) }
    var degree by remember { mutableStateOf(DegreeChoice.Degree_In_Software_Engineering) }
    var graduationYear by remember { mutableStateOf(TextFieldValue()) }
    var extraCourse by remember { mutableStateOf(TextFieldValue()) }
    var profilePhotoUri by remember { mutableStateOf<Uri?>(null) }
    var currentJob by remember { mutableStateOf(TextFieldValue()) }
    var currentEmployee by remember { mutableStateOf(TextFieldValue()) }
    var location by remember { mutableStateOf(TextFieldValue()) }
    var phone by remember { mutableStateOf(TextFieldValue()) }
    var linkedIn by remember { mutableStateOf(TextFieldValue()) }
    var customDegree by remember { mutableStateOf(TextFieldValue()) }
    var skills by remember { mutableStateOf(TextFieldValue()) }

    // State for the dropdown menu
    var expanded by remember { mutableStateOf(false) }
    val degreeOptions = DegreeChoice.values().map { it.name }

    val firestore = Firebase.firestore
    val auth = FirebaseAuth.getInstance()

    // Function to save the profile data to Firestore
    fun saveProfile() {
        if (fullName.text.isEmpty() || email.text.isEmpty()) {
            // Show error message if required fields are empty
            return
        }

        val profileData = AlumniProfileData(
            profileID = auth.currentUser?.uid ?: "",
            fullName = fullName.text,
            email = email.text,
            degree = degree,
            graduationYear = graduationYear.text,
            extraCourse = extraCourse.text,
            profilePhotoUri = profilePhotoUri?.toString(),
            currentJob = currentJob.text,
            currentEmployee = currentEmployee.text,
            location = location.text,
            phone = phone.text,
            linkedIn = linkedIn.text,
            customDegree = customDegree.text,
            skills = skills.text.split(",").map { it.trim() } // Trim whitespace for skills
        )

        firestore.collection("alumniProfiles")
            .document(profileData.profileID)
            .set(profileData)
            .addOnSuccessListener {
                // Handle success (e.g., navigate to another screen)
                navController.navigate("next_screen_route") // Change this to your next screen
            }
            .addOnFailureListener { e ->
                // Handle failure (e.g., show an error message)
                Log.e("CreateProfile", "Error saving profile", e)
                // Show a Snackbar or Toast to inform the user
            }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text(
            text = "Create Profile",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // All the TextField components...

        Button(
            onClick = { saveProfile() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create Profile")
        }
    }
}
