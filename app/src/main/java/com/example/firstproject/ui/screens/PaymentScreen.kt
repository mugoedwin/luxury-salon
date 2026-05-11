package com.example.firstproject.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.firstproject.ui.theme.GoldLuxury
import com.example.firstproject.ui.theme.MaroonPrimary
import com.example.firstproject.viewmodel.PaymentState
import com.example.firstproject.viewmodel.PaymentViewModel

@Composable
fun PaymentScreen(
    serviceName: String = "Service",
    amount: Int = 1,
    imageUrl: String = "file:///android_asset/images/image1.png",
    onPaymentSuccess: () -> Unit,
    viewModel: PaymentViewModel = viewModel()
) {
    val paymentState by viewModel.paymentState
    var phone by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        // 1. Background Image
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // 2. Luxury Scrim
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, MaroonPrimary.copy(alpha = 0.9f)),
                        startY = 300f
                    )
                )
        )

        // 3. Payment Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                text = serviceName.uppercase(),
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = GoldLuxury,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 4.sp
                )
            )
            
            Text(
                text = "Ksh $amount",
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("M-Pesa Phone Number", color = Color.White) },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = GoldLuxury,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.5f)
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { viewModel.onPayClicked(phone, amount) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GoldLuxury),
                shape = RoundedCornerShape(12.dp),
                enabled = paymentState !is PaymentState.Loading
            ) {
                if (paymentState is PaymentState.Loading) {
                    CircularProgressIndicator(color = Color.Black)
                } else {
                    Text("PAY VIA M-PESA", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
            
            Spacer(modifier = Modifier.height(40.dp))
        }
        
        // Handle States
        LaunchedEffect(paymentState) {
            if (paymentState is PaymentState.Success) {
                onPaymentSuccess()
            }
        }
    }
}
