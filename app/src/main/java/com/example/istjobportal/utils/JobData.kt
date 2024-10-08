package com.example.istjobportal.utils

import com.google.firebase.Timestamp

data class JobData(
    var id: String = "",
    var title: String = "",
    var company: String = "",
    var type: String = "",
    var location: String = "",
    var description: String = "",
    var imageUrl: String = "",
    var expiryDate: Timestamp = Timestamp.now(),
    var postedDate: Timestamp = Timestamp.now()
) {
    fun isExpired(): Boolean {
        return expiryDate.toDate().before(Timestamp.now().toDate())
    }

    fun isActive(): Boolean {
        return !isExpired()
    }
}

