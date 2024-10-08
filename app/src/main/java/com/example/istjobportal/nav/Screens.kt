package com.example.istjobportal.nav

sealed class Screens(val route: String){
    data object LoginScreen:Screens(route = "login" )
    data object SignupScreen:Screens(route = "Signup")
    data object DashboardScreen : Screens("dashboard/{role}")
    data object ForgotPasswordScreen:Screens(route = "ForgotPassword_screen")
    data object CreateProfileScreen:Screens(route = "CreateProfile_Screen")
    data object AddJobScreen : Screens("add_job")
    data object EditJobScreen : Screens("edit_job")
    data object DeleteJobScreen : Screens("delete_job")
    data object ManageJobsScreen : Screens("manage_jobs")
    data object EditProfileScreen : Screens("edit_profile")
    data object ApplyForJobScreen : Screens("apply_job")
    data object ViewProfileScreen : Screens("view_profile")
    data object ViewProfilesScreen : Screens("view_profiles")

}