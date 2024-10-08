package com.example.istjobportal.utils

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class ProfileViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    val profile = mutableStateOf<ProfileData?>(null)

    fun fetchProfile(userId: String) {
        db.collection("alumniProfiles").document(userId).get().addOnSuccessListener { document ->
            profile.value = document.toObject(ProfileData::class.java)
        }
    }

    fun createOrUpdateProfile(userId: String, profileData: ProfileData) {
        db.collection("alumniProfiles").document(userId).set(profileData)
    }
}
