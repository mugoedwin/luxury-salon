package com.example.firstproject.ui.screens

import androidx.compose.runtime.mutableStateListOf

object BookingManager {
    val cart = mutableStateListOf<BespokeService>()

    fun addToCart(service: BespokeService) {
        cart.add(service)
    }

    fun clearCart() {
        cart.clear()
    }

    fun calculateTotal(): Double {
        return cart.sumOf { it.price.toDoubleOrNull() ?: 0.0 }
    }
}
