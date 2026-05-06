package com.example.firstproject.model

data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val dob: String = "",
    val location: String = "",
    val role: String = "user"
)
