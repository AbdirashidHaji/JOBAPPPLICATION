package com.example.istjobportal.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.istjobportal.nav.Screens
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun DashboardScreen(
    navController: NavController,
    role: String
) {
    // Firebase Authentication instance
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    // Content specific to user role
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (role == "admin") "Admin Dashboard" else "Alumni Dashboard",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (role == "admin") {
            // Admin-specific features
            Button(
                onClick = { navController.navigate(Screens.AddJobScreen.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Job Listing")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate(Screens.EditJobScreen.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Edit Job Listing")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate(Screens.DeleteJobScreen.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Delete Job Listing")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate(Screens.ManageJobsScreen.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Manage Jobs")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate(Screens.ViewProfilesScreen.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("View Alumni Profiles")
            }
        } else {
            // Alumni-specific features
            Button(
                onClick = { navController.navigate(Screens.CreateProfileScreen.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Create Profile")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate(Screens.EditProfileScreen.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Edit Profile")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate(Screens.ApplyForJobScreen.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Apply for Job")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate(Screens.ViewProfileScreen.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("View Your Profile")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Logout Button
        Button(
            onClick = {
                auth.signOut()
                navController.navigate(Screens.LoginScreen.route) {
                    popUpTo(Screens.LoginScreen.route) { inclusive = true }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Logout", color = Color.White)
        }
    }
}
