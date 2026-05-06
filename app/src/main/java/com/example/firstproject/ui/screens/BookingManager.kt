package com.example.firstproject.ui.screens

import androidx.compose.runtime.mutableStateListOf

object BookingManager {
    // This list will remember everything the user "books"
    val cart = mutableStateListOf<BespokeService>()
    
    fun addToCart(service: BespokeService) {
        if (!cart.contains(service)) {
            cart.add(service)
        }
    }

    fun clearCart() {
        cart.clear()
    }
    
    fun calculateTotal(): String {
        var total = 0
        cart.forEach { 
            // Remove commas and convert to int
            val price = it.price.replace(",", "").toIntOrNull() ?: 0
            total += price 
        }
        return total.toString()
    }
}
