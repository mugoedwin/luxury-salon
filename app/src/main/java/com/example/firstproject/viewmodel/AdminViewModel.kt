package com.example.firstproject.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.firstproject.model.Service
import com.google.firebase.firestore.FirebaseFirestore

class AdminViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    
    var services = mutableStateListOf<Service>()
    var isLoading = mutableStateOf(true)

    init { fetchServices() }

    private fun fetchServices() {
        isLoading.value = true
        db.collection("services").addSnapshotListener { snapshot, error ->
            if (error != null) {
                isLoading.value = false
                return@addSnapshotListener
            }
            
            if (snapshot != null) {
                services.clear()
                val fetchedServices = snapshot.documents.map { doc ->
                    val service = doc.toObject(Service::class.java)
                    service?.copy(id = doc.id) ?: Service(id = doc.id)
                }
                services.addAll(fetchedServices)
            }
            isLoading.value = false
        }
    }

    // CREATE or UPDATE a service
    fun saveService(service: Service) {
        val docRef = if (service.id.isNotEmpty()) {
            db.collection("services").document(service.id)
        } else {
            db.collection("services").document()
        }
        
        docRef.set(service.copy(id = docRef.id))
    }

    // DELETE a service
    fun deleteService(serviceId: String) {
        db.collection("services").document(serviceId).delete()
    }
}
