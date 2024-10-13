package com.example.istjobportal.screen

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.istjobportal.nav.Screens
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun DeleteJobScreen(navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Are you sure you want to delete this job?", style = MaterialTheme.typography.titleSmall)

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            db.collection("jobListings").document().delete()
                .addOnSuccessListener {
                    Toast.makeText(context, "Job deleted successfully", Toast.LENGTH_SHORT).show()
                    navController.navigate(Screens.ManageJobsScreen.route)
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Failed to delete job: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }) {
            Text("Delete Job")
        }
    }
}
