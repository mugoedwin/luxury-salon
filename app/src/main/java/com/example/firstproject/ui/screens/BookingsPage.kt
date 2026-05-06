package com.example.firstproject.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingsPage(onBackClick: () -> Unit) {
    val luxuryGold = Color(0xFFD4AF37)
    val deepMaroon = Color(0xFF1A0A0A)
    val cardBackground = Color(0xFF2A1A1A)

    val cartItems = BookingManager.cart

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("YOUR BOOKINGS", letterSpacing = 2.sp, fontSize = 16.sp) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = luxuryGold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = deepMaroon,
                    titleContentColor = luxuryGold
                )
            )
        },
        containerColor = deepMaroon
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Header Image
            AsyncImage(
                model = "file:///android_asset/images/bookings.jpg",
                contentDescription = "Bookings Header",
                modifier = Modifier.fillMaxWidth().height(200.dp),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(16.dp)) {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(cartItems) { service ->
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                            colors = CardDefaults.cardColors(containerColor = cardBackground)
                        ) {
                            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(service.title, color = Color.White, fontWeight = FontWeight.Bold)
                                    Text("Ksh ${service.price}", color = luxuryGold)
                                }
                                IconButton(onClick = { BookingManager.cart.remove(service) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Remove", tint = Color.Gray)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text("Total: Ksh ${BookingManager.calculateTotal()}", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { /* Handle Payment */ },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = luxuryGold)
                ) {
                    Text("PROCEED TO PAYMENT", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
