package com.example.istjobportal.nav

sealed class Screens(val route: String){
    data object LoginScreen:Screens(route = "login" )
    data object SignupScreen:Screens(route = "Signup")
    data object DashboardScreen:Screens(route = "dashboard_screen")
    data object MainScreen:Screens(route = "main_screen")
    data object AddDataScreen:Screens(route = "add_data_screen")
    data object GetDataScreen:Screens(route = "get_data_screen")
    data object ProfileScreen:Screens(route = "Profile_screen")
    data object ForgotPasswordScreen:Screens(route = "ForgotPassword_screen")
}