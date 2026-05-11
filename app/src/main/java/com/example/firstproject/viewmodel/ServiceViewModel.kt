package com.example.firstproject.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.firstproject.model.Service
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ServiceViewModel : ViewModel() {
    private val db = Firebase.firestore
    
    // This list will update automatically when Firestore changes
    var services = mutableStateListOf<Service>()
        private set

    init {
        fetchServices()
    }

    private fun fetchServices() {
        db.collection("services")
            .addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener
                
                if (snapshot != null) {
                    val list = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(Service::class.java)?.copy(id = doc.id)
                    }
                    services.clear()
                    services.addAll(list)
                }
            }
    }
}
