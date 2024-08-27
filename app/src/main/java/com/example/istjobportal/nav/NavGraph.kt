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
fun NavGraph(navController: NavHostController,
             SharedViewModel: SharedViewModel

){ NavHost(
        navController = navController,
        startDestination = Screens.SignupScreen.route
    ) {
        composable(
            route = Screens.LoginScreen.route
        ){
            LoginScreen(navController = navController)
        }
        //Dashboard
        composable(
            route = Screens.DashboardScreen.route
        ){
            DashboardScreen(navController = navController)
        }
        //MainScreen
        composable(
            route = Screens.MainScreen.route
        ){
            MainScreen(navController = navController)
        }
    // get data screen

    composable(
        route = Screens.GetDataScreen.route
    ){
        GetDataScreen(
            navController = navController,
            SharedViewModel = SharedViewModel
        )
    }
    // add data screen
    composable(
        route = Screens.AddDataScreen.route
    ){
        AddDataScreen(
            navController = navController,
            SharedViewModel = SharedViewModel
            )
      }
    }
}