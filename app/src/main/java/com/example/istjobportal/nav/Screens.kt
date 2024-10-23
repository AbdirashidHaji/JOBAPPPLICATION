package com.example.istjobportal.nav

sealed class Screens(val route: String){
    //authentications
    data object LoginScreen:Screens(route = "login" )
    data object SignupScreen:Screens(route = "Signup")
    data object DashboardScreen : Screens("dashboard/{role}")
    data object ForgotPasswordScreen:Screens(route = "ForgotPassword_screen")

    //jobs
    data object AddJobScreen : Screens("add_job")
    data object ApplicationScreen : Screens("Application_Screen")
    data object DisplayApplicationScreen : Screens("Display_Application")
    data object JobScreen : Screens("Job_Screen")
    data object JobListScreen : Screens("JobList_Screen")
    data object ApplicationStatusScreen :Screens("Application_StatusScreen")
    //profiles
    data object EditProfileScreen : Screens("edit_profile/{userId}")
    data object CreateProfileScreen:Screens(route = "CreateProfile_Screen")
    data object ViewProfileScreen : Screens("view_profile/{userId}")
    data object ViewProfilesScreen : Screens("view_profiles")


}