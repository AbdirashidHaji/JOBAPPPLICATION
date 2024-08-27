package com.example.istjobportal.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.istjobportal.screen.AddDataScreen
import com.example.istjobportal.screen.LoginScreen
import com.example.istjobportal.screen.SignupScreen
import com.example.istjobportal.screen.DashboardScreen
import com.example.istjobportal.screen.GetDataScreen
import com.example.istjobportal.screen.MainScreen
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

//        dashboard
        composable(
            route = Screens.DashboardScreen.route
        ){
            DashboardScreen(navController = navController)
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