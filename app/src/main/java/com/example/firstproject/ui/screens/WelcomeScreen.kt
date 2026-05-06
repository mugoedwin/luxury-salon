package com.example.firstproject.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WelcomeScreen(
    onNavigateToLogin: (String) -> Unit, // Takes "admin" or "customer"
    onNavigateToRegister: () -> Unit
) {
    val luxuryGold = Color(0xFFD4AF37)
    val deepCharcoal = Color(0xFF1A1A1A)
    
    var showRoleDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(deepCharcoal)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "PREMIUM BEAUTY EXPERIENCE",
            fontSize = 12.sp,
            letterSpacing = 2.sp,
            color = luxuryGold,
            fontWeight = FontWeight.Light
        )
        
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Ivonne Orchard",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Luxury salon services, premium beauty care, and world-class styling since 2009.",
            fontSize = 14.sp,
            color = Color.LightGray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        
        Spacer(modifier = Modifier.height(48.dp))

        // LOGIN BUTTON
        Button(
            onClick = { showRoleDialog = true },
            modifier = Modifier.fillMaxWidth().height(55.dp),
            colors = ButtonDefaults.buttonColors(containerColor = luxuryGold),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Login to Dashboard", color = Color.Black, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // REGISTER BUTTON
        OutlinedButton(
            onClick = onNavigateToRegister,
            modifier = Modifier.fillMaxWidth().height(55.dp),
            border = BorderStroke(1.dp, luxuryGold),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Create Account", color = luxuryGold, fontWeight = FontWeight.Bold)
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text("Glow With Confidence ✨", color = Color.Gray, fontSize = 12.sp)
    }

    // Role Selection Dialog
    if (showRoleDialog) {
        AlertDialog(
            onDismissRequest = { showRoleDialog = false },
            containerColor = Color(0xFF2D2D2D), // Dark dialog for luxury feel
            title = { Text("Access Portal", color = luxuryGold) },
            text = { Text("Are you accessing the Admin suite or the Customer portal?", color = Color.White) },
            confirmButton = {
                TextButton(onClick = { 
                    showRoleDialog = false
                    onNavigateToLogin("admin") 
                }) { Text("Admin", color = luxuryGold) }
            },
            dismissButton = {
                TextButton(onClick = { 
                    showRoleDialog = false
                    onNavigateToLogin("customer") 
                }) { Text("Customer", color = Color.White) }
            }
        )
    }
}
