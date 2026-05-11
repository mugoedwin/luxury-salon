package com.example.firstproject.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firstproject.ui.screens.Booking
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

sealed class BookingState {
    object Idle : BookingState()
    object Loading : BookingState()
    object Success : BookingState()
    data class Error(val message: String) : BookingState()
}

class BookingViewModel : ViewModel() {
    private val mAuth = FirebaseAuth.getInstance()
    private val mFirestore = FirebaseFirestore.getInstance()

    private val _bookingState = mutableStateOf<BookingState>(BookingState.Idle)
    val bookingState: State<BookingState> = _bookingState

    val bookings = mutableStateListOf<Booking>()
    var isLoading = mutableStateOf(false)

    init {
        fetchBookings()
    }

    fun fetchBookings() {
        isLoading.value = true
        mFirestore.collection("bookings")
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    bookings.clear()
                    bookings.addAll(snapshot.documents.map { doc ->
                        Booking(
                            id = doc.id,
                            serviceName = doc.getString("service") ?: "Service",
                            customerName = doc.getString("customerName") ?: "Valued Client",
                            imageUrl = doc.getString("imageUrl") ?: "",
                            appointmentDate = doc.getString("date") ?: "",
                            appointmentTime = doc.getString("time") ?: "",
                            price = doc.get("total")?.toString() ?: "0",
                            status = doc.getString("status") ?: "Pending"
                        )
                    })
                }
                isLoading.value = false
            }
    }

    fun updateBookingStatus(bookingId: String, newStatus: String) {
        viewModelScope.launch {
            mFirestore.collection("bookings").document(bookingId)
                .update("status", newStatus)
                .await()
        }
    }

    fun bookSession(serviceName: String, date: String, time: String) {
        viewModelScope.launch {
            _bookingState.value = BookingState.Loading
            
            try {
                val userId = mAuth.currentUser?.uid ?: throw Exception("Not logged in")
                
                val appointment = hashMapOf(
                    "userId" to userId,
                    "service" to serviceName,
                    "date" to date,
                    "time" to time,
                    "status" to "Pending"
                )

                mFirestore.collection("bookings").add(appointment).await()
                
                _bookingState.value = BookingState.Success
                
            } catch (e: Exception) {
                _bookingState.value = BookingState.Error(e.message ?: "Booking failed")
            }
        }
    }

    fun resetState() {
        _bookingState.value = BookingState.Idle
    }
}
