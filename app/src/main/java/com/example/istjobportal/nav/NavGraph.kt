package com.example.istjobportal.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.istjobportal.screen.AddJobScreen
import com.example.istjobportal.screen.CreateProfileScreen
import com.example.istjobportal.screen.DashboardScreen
import com.example.istjobportal.screen.DeleteJobScreen
import com.example.istjobportal.screen.EditJobScreen
import com.example.istjobportal.screen.EditProfileScreen
import com.example.istjobportal.screen.LoginScreen
import com.example.istjobportal.screen.SignupScreen
import com.example.istjobportal.screen.ForgotPasswordScreen
import com.example.istjobportal.screen.ManageJobsScreen
import com.example.istjobportal.screen.ViewProfileScreen
import com.example.istjobportal.screen.ViewProfilesScreen
import com.example.istjobportal.utils.SharedViewModel

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screens.LoginScreen.route) {

        composable(route = Screens.LoginScreen.route) {
            LoginScreen(navController = navController)
        }

        composable(route = Screens.SignupScreen.route) {
            SignupScreen(navController = navController)
        }

        composable(route = Screens.DashboardScreen.route + "/{role}") { backStackEntry ->
            val role = backStackEntry.arguments?.getString("role") ?: "alumni"
            DashboardScreen(navController = navController, role = role)
        }

       composable(route = Screens.AddJobScreen.route) {
           AddJobScreen(navController = navController)
//           AddJobScreen(navController = navController)
       }

        composable(route = Screens.EditJobScreen.route) {
            EditJobScreen(navController = navController)
        }

        composable(route = Screens.DeleteJobScreen.route) {
            DeleteJobScreen(navController = navController)
       }

        composable(route = Screens.ManageJobsScreen.route) {
           ManageJobsScreen(navController = navController)
       }
//
        composable(route = Screens.CreateProfileScreen.route) {
           CreateProfileScreen(navController = navController)
       }

        composable(route = Screens.ViewProfileScreen.route) {
           ViewProfileScreen(navController = navController)
       }

       composable(route = Screens.EditProfileScreen.route) {
          EditProfileScreen(navController = navController)

       }
//
//        composable(route = Screens.ApplyForJobScreen.route) {
//            ApplyForJobScreen(navController = navController)
//        }
//
        composable(route = Screens.ViewProfilesScreen.route) {
           ViewProfilesScreen(navController = navController)
        }
    }
}
