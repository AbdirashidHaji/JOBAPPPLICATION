package com.example.istjobportal.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.istjobportal.R
import com.example.istjobportal.nav.Screens
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun LoginScreen(navController: NavController) {
    // Initialize Firebase Auth
    val auth = FirebaseAuth.getInstance()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var passwordVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Login",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.headlineMedium,
                    fontSize = 24.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Image(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(56.dp)),
                    painter = painterResource(R.drawable.ist_logo),
                    contentDescription = "Login"
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = passwordVisible,
                        onCheckedChange = { passwordVisible = it }
                    )
                    Text(text = if (passwordVisible) "Hide Password" else "Show Password")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Forgot Password?",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        navController.navigate(Screens.ForgotPasswordScreen.route)
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        signIn(email, password, auth, { user ->
                            // Navigate based on the role of the user
                            fetchUserRoleAndNavigate(
                                auth,
                                navController,
                                { role -> navController.navigate(Screens.DashboardScreen.route + "/$role") },
                                { navController.navigate(Screens.CreateProfileScreen.route) }
                            )
                        }, { error ->
                            errorMessage = error
                        })
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Sign In")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { navController.navigate(Screens.SignupScreen.route) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Don't have an account? Sign Up")
                }

                // Display error message if exists
                errorMessage?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

private fun signIn(
    email: String,
    password: String,
    auth: FirebaseAuth,
    onSuccess: (FirebaseUser?) -> Unit,
    onFailure: (String) -> Unit
) {
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                if (user != null && user.isEmailVerified) {
                    onSuccess(user)
                } else {
                    auth.signOut()
                    onFailure("Email not verified. Please verify your email.")
                }
            } else {
                val error = task.exception?.message ?: "Sign in failed"
                onFailure(error)
            }
        }
}

private fun fetchUserRoleAndNavigate(
    auth: FirebaseAuth,
    navController: NavController,
    navigateToDashboard: (String) -> Unit,
    navigateToCreateProfile: () -> Unit
) {
    val uid = auth.currentUser?.uid
    if (uid != null) {
        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(uid).get()
            .addOnSuccessListener { documentSnapshot ->
                val role = documentSnapshot.getString("role") ?: "alumni"
                if (role == "admin") {
                    navigateToDashboard("admin")
                } else {
                    checkIfProfileExists(uid, navController, navigateToDashboard, navigateToCreateProfile)
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(navController.context, "Error fetching user role: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}

private fun checkIfProfileExists(
    uid: String,
    navController: NavController,
    navigateToDashboard: (String) -> Unit,
    navigateToCreateProfile: () -> Unit
) {
    val db = FirebaseFirestore.getInstance()
    db.collection("alumniProfiles").document(uid).get()
        .addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                navigateToDashboard("alumni")
            } else {
                navigateToCreateProfile()
            }
        }
        .addOnFailureListener { e ->
            Toast.makeText(navController.context, "Error checking profile: ${e.message}", Toast.LENGTH_LONG).show()
        }
}
