package com.example.firstproject.ui.screens

data class Booking(
    val id: String = "",
    val serviceName: String = "",
    val customerName: String = "Valued Client",
    val imageUrl: String = "",
    val appointmentTime: String = "",
    val appointmentDate: String = "",
    val duration: String = "1h",
    val price: String = "0",
    val status: String = "Pending",
    val refCode: String = "INV-0000"
)
