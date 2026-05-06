package com.example.firstproject.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun ContactUsScreen(onBack: () -> Unit = {}) {
    val luxuryGold = Color(0xFFD4AF37)
    val deepCharcoal = Color(0xFF121212)
    
    // Form States
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(deepCharcoal)
    ) {
        // 1. Hero Header
        item {
            Box(modifier = Modifier.fillMaxWidth().height(300.dp)) {
                AsyncImage(
                    model = "file:///android_asset/images/laety.jpg",
                    contentDescription = "Ivonne Orchard Residency",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(modifier = Modifier.fillMaxSize().background(
                    Brush.verticalGradient(listOf(Color.Transparent, deepCharcoal))
                ))
                Column(
                    modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("PRIVATE CONCIERGE", color = luxuryGold, style = MaterialTheme.typography.labelLarge, letterSpacing = 5.sp)
                    Text("Get In Touch", color = Color.White, style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.ExtraBold))
                }
            }
        }

        // 2. Info Cards
        item {
            Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)) {
                ContactInfoCard(
                    title = "OUR RESIDENCE",
                    detail = "Makadara, Nairobi, Kenya", 
                    subDetail = "Available for Private Appointments Only",
                    iconText = "📍",
                    luxuryGold = luxuryGold
                )
                Spacer(modifier = Modifier.height(12.dp))
                ContactInfoCard(
                    title = "DIRECT LINE",
                    detail = "+254 116 029 863", 
                    subDetail = "24/7 Priority Concierge",
                    iconText = "📞",
                    luxuryGold = luxuryGold
                )
            }
        }

        // 3. Enquiry Form
        item {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = "SUBMIT YOUR CREDENTIALS",
                    color = Color.Gray,
                    fontSize = 11.sp,
                    letterSpacing = 2.sp,
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                LuxuryInputField(value = name, onValueChange = { name = it }, label = "Full Name")
                Spacer(modifier = Modifier.height(16.dp))
                
                LuxuryInputField(value = email, onValueChange = { email = it }, label = "Email Address")
                Spacer(modifier = Modifier.height(16.dp))
                
                LuxuryInputField(value = phone, onValueChange = { phone = it }, label = "Phone Number")
                Spacer(modifier = Modifier.height(16.dp))
                
                LuxuryInputField(
                    value = message, 
                    onValueChange = { message = it }, 
                    label = "Enquiry Details", 
                    isLarge = true
                )
                
                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = { /* Save to Firebase logic */ },
                    modifier = Modifier.fillMaxWidth().height(60.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = luxuryGold),
                    shape = RoundedCornerShape(4.dp) 
                ) {
                    Text("SEND MESSAGE", color = Color.Black, fontWeight = FontWeight.ExtraBold, letterSpacing = 2.sp)
                }
                
                Spacer(modifier = Modifier.height(40.dp))
                Text(
                    text = "Ivonne Orchard • Award-Winning Care Since 2009",
                    color = Color.DarkGray,
                    fontSize = 10.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
fun ContactInfoCard(title: String, detail: String, subDetail: String, iconText: String, luxuryGold: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(iconText, fontSize = 22.sp)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(title, color = luxuryGold, fontSize = 9.sp, letterSpacing = 2.sp)
                Text(detail, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Medium)
                Text(subDetail, color = Color.Gray, fontSize = 11.sp)
            }
        }
    }
}

@Composable
fun LuxuryInputField(value: String, onValueChange: (String) -> Unit, label: String, isLarge: Boolean = false) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = Color.Gray, fontSize = 12.sp) },
        modifier = Modifier.fillMaxWidth().height(if (isLarge) 140.dp else 65.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color.White.copy(alpha = 0.1f),
            focusedBorderColor = Color(0xFFD4AF37),
            cursorColor = Color(0xFFD4AF37),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        ),
        shape = RoundedCornerShape(8.dp)
    )
}
