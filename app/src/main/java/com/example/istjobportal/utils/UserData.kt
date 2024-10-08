package com.example.istjobportal.utils

// UserData class with predefined roles and permissions
class UserData(
    val userId: String = "",
    val email: String = "",
    val role: String = "",
    val permissions: List<String> = listOf()
)

// Possible Roles
object Roles {
    const val ADMIN = "admin"
    const val ALUMNI = "alumni"
    const val GUEST = "guest"
}

// Permissions assigned to roles
object Permissions {
    const val ADD_JOB = "addjob"
    const val MANAGE_JOB = "managejob"
    const val VIEW_JOB = "viewjob"
    const val VIEW_APPLICATIONS = "viewapplications"
    const val DELETE_JOB = "deletejob"
    const val UPDATE_JOB = "updatejob"
    const val VIEW_ALUMNI_PROFILE = "viewalumniprofile"
    const val VIEW_DASHBOARD = "viewdashboard"
}

// Role-to-permission mapping
val rolePermissionsMap: Map<String, List<String>> = mapOf(
    Roles.ADMIN to listOf(
        Permissions.ADD_JOB,
        Permissions.MANAGE_JOB,
        Permissions.VIEW_JOB,
        Permissions.VIEW_APPLICATIONS,
        Permissions.DELETE_JOB,
        Permissions.UPDATE_JOB,
        Permissions.VIEW_ALUMNI_PROFILE,
        Permissions.VIEW_DASHBOARD
    ),
    Roles.ALUMNI to listOf(
        Permissions.VIEW_JOB,
        Permissions.VIEW_ALUMNI_PROFILE,
        Permissions.VIEW_APPLICATIONS
    ),
    Roles.GUEST to listOf(
        Permissions.VIEW_JOB
    )
)
