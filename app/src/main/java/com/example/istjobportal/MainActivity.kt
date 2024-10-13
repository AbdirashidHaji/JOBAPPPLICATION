package com.example.istjobportal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.istjobportal.nav.NavGraph
import com.example.istjobportal.nav.Screens
import com.example.istjobportal.ui.theme.IstJobPortalTheme
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var navController: NavHostController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IstJobPortalTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ){
              navController = rememberNavController()
                    //calling navGraph
                    NavGraph(navController = navController)                }

            }
        }
    }
    @Preview(showBackground = true)
    @Composable
    fun SignupScreenPreview() {
        IstJobPortalTheme {
            Screens.DashboardScreen
        }
    }
}

