package com.example.istjobportal.utils

import androidx.room.Entity
import androidx.room.PrimaryKey

// Define the degree choices available for alumni
enum class DegreeChoice {
    Degree_In_Software_Engineering,
    Degree_In_Computer_Science,
    Degree_In_Information_Technology,
    Degree_In_Data_Science,
    Degree_In_Cyber_Security,
    Degree_In_Artificial_Intelligence,
    Other // Add an option for alumni who have a different degree
}

// Entity annotation for Room database table
@Entity(tableName = "alumniProfiles")
data class AlumniProfileData(
    @PrimaryKey var profileID: String = "", // Unique identifier for the profile
    var fullName: String = "", // Full name of the alumni
    var email: String = "", // Email address of the alumni
    var degree: DegreeChoice = DegreeChoice.Degree_In_Software_Engineering, // Degree choice
    var graduationYear: String = "", // Year of graduation
    var extraCourse: String = "", // Additional courses taken
    var profilePhotoUri: String? = null, // URI for profile photo
    var currentJob: String = "", // Current job title
    var currentEmployee: String = "", // Current employer
    var location: String = "", // Current location
    var phone: String = "", // Phone number
    var linkedIn: String = "", // LinkedIn profile URL
    var customDegree: String? = null, // Custom degree input for other degrees
    var skills: List<String> = emptyList() // List of skills
)
