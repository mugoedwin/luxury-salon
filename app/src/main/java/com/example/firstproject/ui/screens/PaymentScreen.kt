package com.example.firstproject.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun PaymentScreen(onPaymentSuccess: () -> Unit) {
    var loading by remember { mutableStateOf(true) }
    val luxuryGold = Color(0xFFD4AF37)
    
    LaunchedEffect(Unit) {
        delay(3000) // Simulate M-Pesa processing time
        loading = false
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black), 
        contentAlignment = Alignment.Center
    ) {
        if (loading) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator(color = luxuryGold)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Initiating M-Pesa STK Push...", color = Color.White)
            }
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(24.dp)) {
                Text("Check your phone for the M-Pesa prompt.", color = Color.White, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = onPaymentSuccess,
                    colors = ButtonDefaults.buttonColors(containerColor = luxuryGold)
                ) {
                    Text("Payment Complete", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
