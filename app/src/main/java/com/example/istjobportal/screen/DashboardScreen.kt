package com.example.istjobportal.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.DrawerValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.istjobportal.R
import com.example.istjobportal.nav.Screens
import kotlinx.coroutines.launch
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavController) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(
                        text = "Menu",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Navigation options with icons
                    DrawerMenuItem(
                        text = "Home",
                        icon = Icons.Filled.Home
                    ) {
                        scope.launch { drawerState.close() }
//                        navController.navigate(Screens.MainScreen.route)
                    }
                    DrawerMenuItem(
                        text = "Profile",
                        icon = Icons.Filled.Person
                    ) {
                        scope.launch { drawerState.close() }
                        navController.navigate(Screens.ProfileScreen.route)
                    }
                    DrawerMenuItem(
                        text = "Settings",
                        icon = Icons.Filled.Settings
                    ) {
                        scope.launch { drawerState.close() }
                        // Add navigation logic for Settings screen
                    }
                    DrawerMenuItem(
                        text = "Jobs",
                        icon = Icons.Filled.Add
                    ) {
                        scope.launch { drawerState.close() }
//                        navController.navigate(Screens.AddJobScreen.route)
                    }
                    DrawerMenuItem(
                        text = "Logout",
                        icon = Icons.Filled.ExitToApp
                    ) {
                        scope.launch { drawerState.close() }
                        signOutAndNavigate(navController)
                    }
                }
            }
        },
        content = {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("JOB PORTAL") },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Default.Menu, contentDescription = "Hamburger Menu")
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            titleContentColor = Color.White,
                            navigationIconContentColor = Color.White
                        )
                    )
                }
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    // IST logo
                    Image(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape),
                        painter = painterResource(R.drawable.ist_logo),
                        contentDescription = "IST Logo"
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    // Cards layout with text and links
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        DashboardCard(
                            title = "Today's Jobs",
                            content = "30 Jobs Added",
                            onClick = { navController.navigate(Screens.ProfileScreen.route) }
                        )
                        DashboardCard(
                            title = "Recent Applications",
                            content = "15 Applications Received Today",
                            onClick = { navController.navigate(Screens.ProfileScreen.route) }
                        )
                        DashboardCard(
                            title = "Pending Approvals",
                            content = "8 Job Applications Pending Approval",
                            onClick = { navController.navigate(Screens.ProfileScreen.route) }
                        )

                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    // Navigate back to main screen
//                    Button(
//                        onClick = { navController.navigate(Screens.MainScreen.route) },
//                        modifier = Modifier.fillMaxWidth()
//                    ) {
//                        Text(text = "Go Back")
//                    }
                }
            }
        }
    )
}

@Composable
fun DrawerMenuItem(text: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
        Text(text = text)
    }
}

@Composable
fun DashboardCard(title: String, content: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ist_logo), // Replace with relevant image if needed
                contentDescription = title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .padding(bottom = 8.dp)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

private fun signOutAndNavigate(navController: NavController) {
    FirebaseAuth.getInstance().signOut()
    navController.navigate(Screens.LoginScreen.route) {
        popUpTo(Screens.LoginScreen.route) { inclusive = true}
    }
}
