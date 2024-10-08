package com.example.istjobportal.nav

sealed class Screens(val route: String){
    data object LoginScreen:Screens(route = "login" )
    data object SignupScreen:Screens(route = "Signup")
    data object DashboardScreen:Screens(route = "dashboard_screen")
//    data object AddJobScreen:Screens(route = "addJob_screen")
    data object ProfileScreen:Screens(route = "Profile_screen")
    data object ForgotPasswordScreen:Screens(route = "ForgotPassword_screen")
    data object CreateProfileScreen:Screens(route = "CreateProfile_~Screen")

}