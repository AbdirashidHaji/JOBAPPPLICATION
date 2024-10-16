package com.example.istjobportal.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.istjobportal.nav.Screens
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun DeleteJobScreen(navController: NavHostController, jobId: String) {
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp).background(Color.Gray), horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = { navController.navigate("${Screens.DashboardScreen.route}/admin") { popUpTo(Screens.DashboardScreen.route) { inclusive = true } } }) {
            Text("Go Back")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Delete Job", style = MaterialTheme.typography.headlineMedium)

        // Confirm deletion
        Button(onClick = {
            db.collection("jobs").document(jobId).delete()
                .addOnSuccessListener {
                    Toast.makeText(context, "Job deleted successfully", Toast.LENGTH_SHORT).show()
                    navController.navigate("${Screens.DashboardScreen.route}/admin") { popUpTo(Screens.DashboardScreen.route) { inclusive = true } }
                }
                .addOnFailureListener { e -> Toast.makeText(context, "Failed to delete job: ${e.message}", Toast.LENGTH_SHORT).show() }
        }) {
            Text("Confirm Delete")
        }
    }
}
