package com.example.firstproject.ui.screens

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.firstproject.viewmodel.PaymentViewModel
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun InfoLabel(label: String, value: String) {
    Column {
        Text(text = label, style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFFFFD700).copy(alpha = 0.7f)))
        Text(text = value, style = MaterialTheme.typography.bodyMedium.copy(color = Color.White))
    }
}

@Composable
fun BookingCard(booking: Booking) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2D1B19)),
        border = BorderStroke(1.dp, Color(0xFFFFD700).copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Display the image
                AsyncImage(
                    model = booking.imageUrl,
                    contentDescription = "Service Image",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = booking.serviceName.uppercase(),
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = Color(0xFFFFD700),
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        )
                    )
                    Surface(
                        color = if (booking.status == "Confirmed") Color(0xFF4CAF50).copy(alpha = 0.2f) else Color.Gray.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = booking.status,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall.copy(color = Color.White)
                        )
                    }
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color.White.copy(alpha = 0.1f))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    InfoLabel(label = "DATE", value = booking.appointmentDate)
                    Spacer(modifier = Modifier.height(8.dp))
                    InfoLabel(label = "TIME", value = booking.appointmentTime)
                }
                Column(horizontalAlignment = Alignment.End) {
                    InfoLabel(label = "DURATION", value = booking.duration)
                    Spacer(modifier = Modifier.height(8.dp))
                    InfoLabel(label = "REF CODE", value = booking.refCode)
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text("Total Paid", color = Color.White.copy(alpha = 0.6f), style = MaterialTheme.typography.bodySmall)
                Text(
                    text = "Ksh ${booking.price}",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold
                    )
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingsPage(
    onBackClick: () -> Unit,
    onProceedToPayment: (amount: Int, phoneNumber: String) -> Unit,
    paymentViewModel: PaymentViewModel = viewModel()
) {
    val luxuryGold = Color(0xFFD4AF37)
    val deepMaroon = Color(0xFF1A0A0A)
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()

    var step by remember { mutableIntStateOf(0) }
    var phoneNumber by remember { mutableStateOf("") }
    
    val cartItems = BookingManager.cart
    val paymentState by paymentViewModel.paymentState

    // Mock Booking for Presentation
    val mockBookings = listOf(
        Booking("1", "Royal Braids", "Valued Client", "", "10:30 AM", "12/05/2024", "2h", "4500", "Confirmed", "INV-1021"),
        Booking("2", "Facial Treatment", "Valued Client", "", "02:00 PM", "14/05/2024", "1h", "2500", "Pending", "INV-1022")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (step == 0) "MY BOOKINGS" else "PAYMENT", letterSpacing = 2.sp, fontSize = 16.sp) },
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
                LazyColumn(modifier = Modifier.weight(1f)) {
                    // Show mock bookings if cart is empty, otherwise map from BookingManager
                    items(if (cartItems.isEmpty()) mockBookings else cartItems.map { 
                        Booking(
                            serviceName = it.title,
                            price = it.price,
                            imageUrl = it.imageUrl,
                            duration = it.duration,
                            appointmentDate = "Today",
                            appointmentTime = "Soon",
                            status = "Pending"
                        )
                    }) { booking ->
                        BookingCard(booking)
                    }
                }
                if (cartItems.isNotEmpty()) {
                    Button(onClick = { step = 1 }, modifier = Modifier.fillMaxWidth().padding(top = 16.dp).height(56.dp), colors = ButtonDefaults.buttonColors(containerColor = luxuryGold)) {
                        Text("PROCEED TO PAYMENT", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            } else {
                Text("Enter Phone Number (e.g. 0712345678)", color = luxuryGold)
                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                Button(
                    onClick = { paymentViewModel.onPayClicked(phoneNumber, BookingManager.calculateTotal().toInt()) },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = luxuryGold),
                    enabled = phoneNumber.length >= 9
                ) {
                    if (paymentState is com.example.firstproject.viewmodel.PaymentState.Loading) {
                        CircularProgressIndicator(color = Color.Black)
                    } else {
                        Text("PAY NOW", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
