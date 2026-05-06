package com.example.firstproject.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingsPage(onBackClick: () -> Unit) {
    val luxuryGold = Color(0xFFD4AF37)
    val deepMaroon = Color(0xFF1A0A0A)
    val cardBackground = Color(0xFF2A1A1A)

    var selectedDate by remember { mutableStateOf("May 15, 2026") }
    var selectedTime by remember { mutableStateOf("") }

    val timeSlots = listOf(
        "09:00 AM", "10:30 AM", "12:00 PM", 
        "01:30 PM", "03:00 PM", "04:30 PM"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("RESERVE SESSION", letterSpacing = 2.sp, fontSize = 16.sp) },
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text("Selected Service", color = Color.Gray, fontSize = 12.sp)
            Text("Signature Hair Styling", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            
            Spacer(modifier = Modifier.height(24.dp))

            Text("Select Date", color = luxuryGold, fontWeight = FontWeight.Bold)
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = cardBackground),
                border = BorderStroke(1.dp, luxuryGold.copy(alpha = 0.3f))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = luxuryGold)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(selectedDate, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Available Slots", color = luxuryGold, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            
            val rows = timeSlots.chunked(2)
            rows.forEach { rowSlots ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    rowSlots.forEach { time ->
                        TimeSlotButton(
                            time = time,
                            isSelected = selectedTime == time,
                            onSelect = { selectedTime = it },
                            luxuryGold = luxuryGold,
                            cardBackground = cardBackground
                        )
                    }
                    if (rowSlots.size == 1) Spacer(modifier = Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total Estimate", color = Color.Gray)
                Text("Ksh 4,500", color = luxuryGold, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = { /* Trigger M-Pesa STK Push logic */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = luxuryGold),
                shape = RoundedCornerShape(8.dp),
                enabled = selectedTime.isNotEmpty()
            ) {
                Text(
                    "CONFIRM & PAY VIA M-PESA", 
                    color = Color.Black, 
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}

@Composable
fun TimeSlotButton(time: String, isSelected: Boolean, onSelect: (String) -> Unit, luxuryGold: Color, cardBackground: Color) {
    Box(
        modifier = Modifier
            .width(150.dp)
            .height(50.dp)
            .background(
                if (isSelected) luxuryGold else cardBackground,
                RoundedCornerShape(8.dp)
            )
            .border(
                1.dp, 
                if (isSelected) luxuryGold else luxuryGold.copy(alpha = 0.2f),
                RoundedCornerShape(8.dp)
            )
            .clickable(onClick = { onSelect(time) }),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = time,
            color = if (isSelected) Color.Black else Color.White,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}
