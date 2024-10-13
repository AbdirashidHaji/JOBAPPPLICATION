package com.example.istjobportal.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.istjobportal.nav.Screens
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun ApplyForJobScreen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var resumeUrl by remember { mutableStateOf("") }
    val db = FirebaseFirestore.getInstance()
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TextField(value = name, onValueChange = { name = it }, label = { Text("Full Name") })
        TextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        TextField(value = resumeUrl, onValueChange = { resumeUrl = it }, label = { Text("Resume URL") })

        Button(onClick = {
            val application = hashMapOf(
                "name" to name,
                "email" to email,
                "resumeUrl" to resumeUrl,
//                "jobId" to jobId,
                "appliedAt" to System.currentTimeMillis()
            )
            db.collection("jobApplications").add(application)
                .addOnSuccessListener {
                    Toast.makeText(context, "Application submitted successfully", Toast.LENGTH_SHORT).show()
                    navController.navigate(Screens.DashboardScreen.route)
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Failed to submit application: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }) {
            Text("Apply for Job")
        }
    }
}
