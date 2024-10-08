package com.example.istjobportal.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.istjobportal.screen.AddJobScreen
import com.example.istjobportal.screen.CreateProfileScreen
import com.example.istjobportal.screen.LoginScreen
import com.example.istjobportal.screen.SignupScreen
import com.example.istjobportal.screen.DashboardScreen
import com.example.istjobportal.screen.ForgotPasswordScreen
import com.example.istjobportal.screen.JobScreen
import com.example.istjobportal.screen.ProfileScreen
import com.example.istjobportal.utils.SharedViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    sharedViewModel: SharedViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screens.SignupScreen.route
    ) {

        composable(

            route = Screens.LoginScreen.route
        ){
            LoginScreen(navController = navController)


        }

        composable(
            route = Screens.SignupScreen.route
        ) {
            SignupScreen(navController = navController)
        }
        //forgot password
        composable(
            route = Screens.ForgotPasswordScreen.route
        ){
            ForgotPasswordScreen(navController = navController)
        }
        
        //profile
        composable(
            route = Screens.ProfileScreen.route
        ){
            ProfileScreen(navController = navController)
        }

//        dashboard
        composable(
            route = Screens.DashboardScreen.route
        ){
            DashboardScreen(navController = navController)
        }

        //create profile
        composable(
            route = Screens.CreateProfileScreen.route
        ){
            CreateProfileScreen(navController = navController)
        }
        // add  Job screen
//        composable(
//            route = Screens.AddJobScreen.route
//        ) {
//            AddJobScreen(
//                navController = navController,
//                sharedViewModel = sharedViewModel
//            )
//        }
    }
}