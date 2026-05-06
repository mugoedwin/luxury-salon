package com.example.firstproject.viewmodel

import androidx.lifecycle.ViewModel
import com.example.firstproject.ui.screens.BespokeService
import com.google.firebase.firestore.FirebaseFirestore

class AdminViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    // CREATE or UPDATE a service
    fun saveService(service: BespokeService) {
        // Using the title as the ID makes it easy to update existing ones
        db.collection("bespoke_services")
            .document(service.title) 
            .set(service) 
    }

    // DELETE a service
    fun deleteService(serviceTitle: String) {
        db.collection("bespoke_services")
            .document(serviceTitle)
            .delete()
    }
}
