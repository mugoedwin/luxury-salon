package com.example.firstproject.ui.screens

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingsPage(onBackClick: () -> Unit, onProceedToPayment: () -> Unit) {
    val luxuryGold = Color(0xFFD4AF37)
    val deepMaroon = Color(0xFF1A0A0A)
    val cardBackground = Color(0xFF2A1A1A)
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()

    var step by remember { mutableIntStateOf(0) }
    var selectedDate by remember { mutableStateOf("Select Date") }
    var selectedTime by remember { mutableStateOf("") }
    val timeSlots = listOf("09:00 AM", "10:30 AM", "12:00 PM", "01:30 PM", "03:00 PM", "04:30 PM")
    
    val cartItems = BookingManager.cart

    // DatePicker Dialog setup
    val calendar = Calendar.getInstance()
    val datePickerDialog = android.app.DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            selectedDate = "$dayOfMonth/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    fun submitBooking() {
        val bookingData = hashMapOf(
            "services" to cartItems.map { it.title },
            "total" to BookingManager.calculateTotal(),
            "date" to selectedDate,
            "time" to selectedTime,
            "timestamp" to Timestamp.now(),
            "status" to "Pending"
        )

        db.collection("bookings")
            .add(bookingData)
            .addOnSuccessListener {
                Toast.makeText(context, "Booking successfully saved!", Toast.LENGTH_LONG).show()
                BookingManager.clearCart()
                onProceedToPayment()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (step == 0) "YOUR BOOKINGS" else "SELECT DATE & TIME", letterSpacing = 2.sp, fontSize = 16.sp) },
                navigationIcon = {
                    IconButton(onClick = { if (step == 0) onBackClick() else step = 0 }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = luxuryGold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = deepMaroon, titleContentColor = luxuryGold)
            )
        },
        containerColor = deepMaroon
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            if (step == 0) {
                AsyncImage(model = "file:///android_asset/images/bookings.jpg", contentDescription = "Header", modifier = Modifier.fillMaxWidth().height(150.dp), contentScale = ContentScale.Crop)
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(cartItems) { service ->
                        Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), colors = CardDefaults.cardColors(containerColor = cardBackground)) {
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
                Text("Total: Ksh ${BookingManager.calculateTotal()}", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Button(onClick = { step = 1 }, modifier = Modifier.fillMaxWidth().padding(top = 16.dp).height(56.dp), colors = ButtonDefaults.buttonColors(containerColor = luxuryGold)) {
                    Text("PROCEED TO SELECT DATE/TIME", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            } else {
                AsyncImage(model = "file:///android_asset/images/time.jpg", contentDescription = "Time Selection", modifier = Modifier.fillMaxWidth().height(150.dp), contentScale = ContentScale.Crop)
                Spacer(modifier = Modifier.height(16.dp))

                Text("Tap to Select Date", color = luxuryGold, fontWeight = FontWeight.Bold)
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).clickable { datePickerDialog.show() }, 
                    colors = CardDefaults.cardColors(containerColor = cardBackground),
                    border = BorderStroke(1.dp, luxuryGold)
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
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
                            TimeSlotButton(time, selectedTime == time, { selectedTime = it }, luxuryGold, cardBackground)
                        }
                        if (rowSlots.size == 1) Spacer(modifier = Modifier.weight(1f))
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = { submitBooking() },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = luxuryGold),
                    enabled = selectedTime.isNotEmpty() && selectedDate != "Select Date"
                ) {
                    Text("CONFIRM & PAY VIA M-PESA", color = Color.Black, fontWeight = FontWeight.Bold)
                }
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
            .background(if (isSelected) luxuryGold else cardBackground, RoundedCornerShape(8.dp))
            .border(1.dp, if (isSelected) luxuryGold else luxuryGold.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
            .clickable { onSelect(time) },
        contentAlignment = Alignment.Center
    ) {
        Text(time, color = if (isSelected) Color.Black else Color.White)
    }
}
