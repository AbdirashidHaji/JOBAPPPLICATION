package com.example.istjobportal.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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

    // Dashboard content
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Role-based title
        Text(
            text = if (role == "admin") "Admin Dashboard" else "Alumni Dashboard",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Feature cards
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            if (role == "admin") {
                DashboardCard(
                    title = "Add Job Listing",
                    description = "Post new job listings for alumni.",
                    icon = Icons.Default.Add,
                    onClick = { navController.navigate(Screens.AddJobScreen.route) }
                )
                DashboardCard(
                    title = "Edit Job Listing",
                    description = "Modify existing job listings.",
                    icon = Icons.Default.Edit,
                    onClick = { navController.navigate(Screens.EditJobScreen.route) }
                )
                DashboardCard(
                    title = "Delete Job Listing",
                    description = "Remove outdated job listings.",
                    icon = Icons.Default.Delete,
                    onClick = { navController.navigate(Screens.DeleteJobScreen.route) }
                )
                DashboardCard(
                    title = "Manage Jobs",
                    description = "View and manage all jobs.",
                    icon = Icons.Default.List,
                    onClick = { navController.navigate(Screens.ManageJobsScreen.route) }
                )
                DashboardCard(
                    title = "View Alumni Profiles",
                    description = "Browse alumni profiles.",
                    icon = Icons.Default.Person,
                    onClick = { navController.navigate(Screens.ViewProfilesScreen.route) }
                )
            } else {
                DashboardCard(
                    title = "Create Profile",
                    description = "Create your alumni profile.",
                    icon = Icons.Rounded.Person,
                    onClick = { navController.navigate(Screens.CreateProfileScreen.route) }
                )
                DashboardCard(
                    title = "Edit Profile",
                    description = "Update your profile details.",
                    icon = Icons.Default.Edit,
                    onClick = { navController.navigate(Screens.EditProfileScreen.route) }
                )
                DashboardCard(
                    title = "Apply for Job",
                    description = "Search and apply for jobs.",
                    icon = Icons.Rounded.AddCircle,
                    onClick = { navController.navigate(Screens.JobScreen.route) }
                )
                DashboardCard(
                    title = "View Your Profile",
                    description = "Check your current profile.",
                    icon = Icons.Default.Person,
                    onClick = { navController.navigate(Screens.ViewProfileScreen.route) }
                )

            }
        }

        // Spacer between main actions and logout
        Spacer(modifier = Modifier.weight(1f))

        // Logout Button
        LogoutButton(auth, navController)
    }
}

@Composable
fun DashboardCard(
    title: String,
    description: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(40.dp)
            )
        }
    }
}

@Composable
fun LogoutButton(auth: FirebaseAuth, navController: NavController) {
    Button(
        onClick = {
            auth.signOut()
            navController.navigate(Screens.LoginScreen.route) {
                popUpTo(Screens.LoginScreen.route) { inclusive = true }
            }
        },
        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text("Logout", color = Color.White, style = MaterialTheme.typography.bodyLarge)
    }
}
