package com.example.firstproject.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firstproject.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val role: String = "user", val userName: String = "") : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel : ViewModel() {
    private val mAuth = FirebaseAuth.getInstance()
    private val mFirestore = FirebaseFirestore.getInstance()

    private val _authState = mutableStateOf<AuthState>(AuthState.Idle)
    val authState: State<AuthState> = _authState

    fun isUserLoggedIn(): Boolean {
        return mAuth.currentUser != null
    }

    fun getCurrentUserId(): String? {
        return mAuth.currentUser?.uid
    }

    fun registerUser(fullName: String, email: String, password: String, phone: String, dob: String, location: String, role: String = "user") {
        viewModelScope.launch {
            Log.d("AuthViewModel", "registerUser started for email: $email")
            
            if (email.isBlank() || password.isBlank() || fullName.isBlank() || phone.isBlank() || dob.isBlank() || location.isBlank()) {
                _authState.value = AuthState.Error("Please fill in all required fields")
                return@launch
            }

            _authState.value = AuthState.Loading

            try {
                Log.d("AuthViewModel", "Attempting to create user...")
                val authResult = mAuth.createUserWithEmailAndPassword(email, password).await()
                val userId = authResult.user?.uid ?: throw Exception("Failed to get User ID")

                Log.d("AuthViewModel", "Auth success, saving to Firestore...")
                val userData = User(
                    uid = userId,
                    name = fullName,
                    email = email,
                    phone = phone,
                    dob = dob,
                    location = location,
                    role = role
                )

                mFirestore.collection("users").document(userId).set(userData).await()
                
                Log.d("AuthViewModel", "Registration flow complete")
                _authState.value = AuthState.Success(role, fullName)

            } catch (e: Exception) {
                val errorMsg = e.message ?: "An unexpected error occurred"
                Log.e("AuthViewModel", "Registration Error: $errorMsg")
                _authState.value = AuthState.Error(errorMsg)
            }
        }
    }

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            if (email.isBlank() || password.isBlank()) {
                _authState.value = AuthState.Error("Email and password cannot be empty")
                return@launch
            }

            _authState.value = AuthState.Loading

            try {
                mAuth.signInWithEmailAndPassword(email, password).await()
                val userId = mAuth.currentUser?.uid ?: throw Exception("User ID not found")
                fetchUserData(userId)
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Login failed")
            }
        }
    }

    private fun fetchUserData(userId: String) {
        viewModelScope.launch {
            try {
                val document = mFirestore.collection("users").document(userId).get().await()
                if (document != null && document.exists()) {
                    val role = document.getString("role") ?: "user"
                    val name = document.getString("name") ?: "Guest"
                    _authState.value = AuthState.Success(role, name)
                } else {
                    _authState.value = AuthState.Error("User data not found")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Failed to fetch user data")
            }
        }
    }

    fun logout() {
        mAuth.signOut()
        _authState.value = AuthState.Idle
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }

    fun getCurrentUser(onResult: (User?) -> Unit) {
        val userId = mAuth.currentUser?.uid
        if (userId != null) {
            mFirestore.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    val user = document.toObject(User::class.java)
                    onResult(user)
                }.addOnFailureListener {
                    onResult(null)
                }
        } else {
            onResult(null)
        }
    }

    fun updateUser(fullName: String, email: String, phone: String, dob: String, location: String) {
        viewModelScope.launch {
            val userId = mAuth.currentUser?.uid
            if (userId == null) {
                _authState.value = AuthState.Error("User not logged in")
                return@launch
            }

            _authState.value = AuthState.Loading

            val updates = mapOf(
                "name" to fullName,
                "email" to email,
                "phone" to phone,
                "dob" to dob,
                "location" to location
            )

            try {
                mFirestore.collection("users").document(userId).update(updates).await()
                _authState.value = AuthState.Success()
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Update failed")
            }
        }
    }
}
