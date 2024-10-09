package com.example.istjobportal.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.istjobportal.nav.Screens
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun AddJobScreen(navController: NavController) {
    var jobTitle by remember { mutableStateOf("") }
    var jobDescription by remember { mutableStateOf("") }
    var jobLocation by remember { mutableStateOf("") }
    var salary by remember { mutableStateOf("") }
    var experience by remember { mutableStateOf("") }
    var educationLevel by remember { mutableStateOf("") }
    var company by remember { mutableStateOf("") }
    var jobType by remember { mutableStateOf("") }
    var deadlineDate by remember { mutableStateOf("") }
    var dateAdded by remember { mutableStateOf("") }

    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = jobTitle,
            onValueChange = { jobTitle = it },
            label = { Text("Job Title") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = jobDescription,
            onValueChange = { jobDescription = it },
            label = { Text("Job Description") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = jobLocation,
            onValueChange = { jobLocation = it },
            label = { Text("Job Location") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = salary,
            onValueChange = { salary = it },
            label = { Text("Salary") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = experience,
            onValueChange = { experience = it },
            label = { Text("Experience (in years)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = educationLevel,
            onValueChange = { educationLevel = it },
            label = { Text("Education Level") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = company,
            onValueChange = { company = it },
            label = { Text("Company Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = jobType,
            onValueChange = { jobType = it },
            label = { Text("Job Type (e.g. Full-time, Part-time)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = deadlineDate,
            onValueChange = { deadlineDate = it },
            label = { Text("Deadline Date") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = dateAdded,
            onValueChange = { dateAdded = it },
            label = { Text("Date Added") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val jobDetails = hashMapOf(
                    "jobTitle" to jobTitle,
                    "jobDescription" to jobDescription,
                    "jobLocation" to jobLocation,
                    "salary" to salary,
                    "experience" to experience,
                    "educationLevel" to educationLevel,
                    "company" to company,
                    "jobType" to jobType,
                    "deadlineDate" to deadlineDate,
                    "dateAdded" to dateAdded,
                    "postedBy" to auth.currentUser?.uid
                )

                db.collection("jobListings").add(jobDetails)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Job posted successfully", Toast.LENGTH_SHORT).show()
                        navController.navigate(Screens.DashboardScreen.route +"/admin")
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(context, "Failed to post job: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Post Job")
        }
    }
}
