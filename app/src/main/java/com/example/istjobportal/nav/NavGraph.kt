package com.example.istjobportal.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.istjobportal.screen.AddDataScreen
import com.example.istjobportal.screen.ApplicationScreen
import com.example.istjobportal.screen.LoginScreen
import com.example.istjobportal.screen.SignupScreen
import com.example.istjobportal.screen.DashboardScreen
import com.example.istjobportal.screen.ForgotPasswordScreen
import com.example.istjobportal.screen.GetDataScreen
import com.example.istjobportal.screen.JobScreen
import com.example.istjobportal.screen.MainScreen
import com.example.istjobportal.screen.PendingApprovalsScreen
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
        //PendingApprovalsScreen
        composable(
            route = Screens.PendingApprovalsScreen.route
        ){
            PendingApprovalsScreen(navController = navController)
        }
        //jobs
        composable(
            route = Screens.JobScreen.route
        ){
            JobScreen(navController = navController)
        }
        //application
        composable(
            route = Screens.ApplicationsScreen.route
        ){
            ApplicationScreen(navController = navController)
        }
        // main screen
        composable(
            route = Screens.MainScreen.route
        ) {
            MainScreen(
                navController = navController,

                )
        }
        // get data screen
        composable(
            route = Screens.GetDataScreen.route
        ) {
            GetDataScreen(
                navController = navController,
                sharedViewModel = sharedViewModel
            )
        }
        // add data screen
        composable(
            route = Screens.AddDataScreen.route
        ) {
            AddDataScreen(
                navController = navController,
                sharedViewModel = sharedViewModel
            )
        }
    }
}