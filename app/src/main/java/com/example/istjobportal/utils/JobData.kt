package com.example.istjobportal.utils

data class JobData(
    val jobId: String = "",
    val jobTitle: String = "",
    val jobDescription: String = "",
    val jobRequirements: String = "",
    val createdBy: String = "",
    val applicants: List<String> = listOf()  // Contains UIDs of applicants
)
