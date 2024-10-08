package com.example.istjobportal.utils

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class JobViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    val jobs = mutableStateListOf<JobData>()

    init {
        fetchJobs()
    }

    private fun fetchJobs() {
        db.collection("jobs").get().addOnSuccessListener { result ->
            jobs.clear()
            for (document in result) {
                val job = document.toObject(JobData::class.java)
                jobs.add(job)
            }
        }
    }

    fun addJob(job: JobData) {
        db.collection("jobs").add(job).addOnSuccessListener {
            fetchJobs()
        }
    }

    fun updateJob(jobId: String, job: JobData) {
        db.collection("jobs").document(jobId).set(job).addOnSuccessListener {
            fetchJobs()
        }
    }

    fun deleteJob(jobId: String) {
        db.collection("jobs").document(jobId).delete().addOnSuccessListener {
            fetchJobs()
        }
    }

    fun applyJob(jobId: String, userId: String) {
        db.collection("jobs").document(jobId).update("applicants", FieldValue.arrayUnion(userId))
    }
}
