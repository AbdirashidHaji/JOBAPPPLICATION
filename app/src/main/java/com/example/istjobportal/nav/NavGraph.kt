package com.example.istjobportal.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.istjobportal.screen.AddJobScreen
import com.example.istjobportal.screen.ApplicationScreen
import com.example.istjobportal.screen.ApplicationStatusScreen
import com.example.istjobportal.screen.CreateProfileScreen
import com.example.istjobportal.screen.DashboardScreen
import com.example.istjobportal.screen.JobScreen
import com.example.istjobportal.screen.JobListScreen
import com.example.istjobportal.screen.DeleteJobScreen
import com.example.istjobportal.screen.EditJobScreen
import com.example.istjobportal.screen.DisplayApplicationScreen
import com.example.istjobportal.screen.EditProfileScreen
import com.example.istjobportal.screen.LoginScreen
import com.example.istjobportal.screen.SignupScreen
import com.example.istjobportal.screen.ViewProfileScreen
import com.example.istjobportal.screen.ViewProfilesScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screens.LoginScreen.route
    ) {
        // Login Screen
        composable(route = Screens.LoginScreen.route) {
            LoginScreen(navController = navController)
        }

        // Signup Screen
        composable(route = Screens.SignupScreen.route) {
            SignupScreen(navController = navController)
        }

        // Dashboard Screen with role parameter
        composable(
            route = "${Screens.DashboardScreen.route}/{role}",
            arguments = listOf(navArgument("role") { type = NavType.StringType })
        ) { backStackEntry ->
            val role = backStackEntry.arguments?.getString("role") ?: "alumni"
            DashboardScreen(navController = navController, role = role)
        }

        // Add Job Screen
        composable(route = Screens.AddJobScreen.route) {
            AddJobScreen(navController = navController)
        }

        // Edit Job Screen
        composable(route = Screens.EditJobScreen.route,
            arguments = listOf(navArgument("jobId") {type = NavType.StringType})
            ) {backStackEntry ->
            val jobId = backStackEntry.arguments?.getString("jobId") ?: ""
            EditJobScreen(navController = navController,jobId)
        }

        // Delete Job Screen
//        composable(route = Screens.DeleteJobScreen.route,
//            arguments = listOf(navArgument("jobId") {type = NavType.StringType})
//            ) {backStackEntry ->
//            val jobId = backStackEntry.arguments?.getString("jobId") ?: ""
//            DeleteJobScreen(navController = navController,jobId)
//        }
        composable(route = Screens.DeleteJobScreen.route){
            DeleteJobScreen(navController = navController)
        }
        //job listings alumni
        composable(route = Screens.JobScreen.route){
            JobScreen(navController = navController)
        }
        //job listing for admin
        composable(route = Screens.JobListScreen.route){
            JobListScreen(navController = navController)
        }
        // Create Profile Screen
        composable(route = Screens.CreateProfileScreen.route) {
            CreateProfileScreen(navController = navController)
        }

        // View Profile Screen
        composable(
            route = Screens.ViewProfileScreen.route,
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            ViewProfileScreen(navController = navController)
        }

        // Edit Profile Screen
        composable(
            route = Screens.EditProfileScreen.route,
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            EditProfileScreen(navController = navController )
        }

        // View Profiles Screen
        composable(route = Screens.ViewProfilesScreen.route) {
            ViewProfilesScreen(navController = navController)
        }

        //applications
        composable("${Screens.ApplicationScreen.route}/{jobId}") { backStackEntry ->
            val jobId = backStackEntry.arguments?.getString("jobId") ?: ""
            ApplicationScreen(navController, jobId)
        }
     //display applications

        composable(route = Screens.DisplayApplicationScreen.route){
            DisplayApplicationScreen(navController = navController)
        }
     //applications status ApplicationStatusScreen
        composable(route = Screens.ApplicationStatusScreen.route){
            ApplicationStatusScreen(navController = navController)
        }

    }
}
