package com.example.firstproject.ui.theme

import android.graphics.BitmapFactory
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.firstproject.viewmodel.BookingState
import com.example.firstproject.viewmodel.BookingViewModel

@Composable
fun ContactContent(onBookingSuccess: () -> Unit = {}) {
    val context = LocalContext.current
    val bookingViewModel: BookingViewModel = viewModel()
    val bookingState by bookingViewModel.bookingState

    var fullName by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(bookingState) {
        if (bookingState is BookingState.Success) {
            onBookingSuccess()
            bookingViewModel.resetState()
            fullName = ""
            dob = ""
            email = ""
            location = ""
            message = ""
        }
    }

    val imageBitmap = remember {
        try {
            val inputStream = context.assets.open("images/image6.jpg")
            BitmapFactory.decodeStream(inputStream).asImageBitmap()
        } catch (e: Exception) {
            null
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color.Transparent
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(60.dp))
                Text(
                    text = "Get in Touch",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Light,
                    color = DeepSlate,
                    letterSpacing = 2.sp
                )
                Text(
                    text = "Concierge & Booking Inquiries",
                    fontSize = 14.sp,
                    color = BrushedGold,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(32.dp))
            }

            // Hero Image Section
            item {
                imageBitmap?.let {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .shadow(12.dp, RoundedCornerShape(16.dp))
                            .clip(RoundedCornerShape(16.dp))
                    ) {
                        Image(
                            bitmap = it,
                            contentDescription = "Contact LUX",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }

            item {
                // Main Glassmorphism Container
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(8.dp, RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    border = BorderStroke(0.5.dp, SoftStone)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        
                        // TOP SECTION: INFO PANEL
                        Column {
                            Text(
                                text = "OUR CHANNELS",
                                fontSize = 11.sp,
                                color = MutedEarth,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 2.sp
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Our concierge team is available for bespoke booking requests and specialized beauty inquiries.",
                                fontSize = 14.sp,
                                color = DeepSlate.copy(alpha = 0.7f),
                                lineHeight = 20.sp
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            
                            ContactDetailRow(Icons.Default.Email, "concierge@luxsalon.com", BrushedGold)
                            Spacer(modifier = Modifier.height(12.dp))
                            ContactDetailRow(Icons.Default.Phone, "+1 (555) LUX-GLOW", Plum)
                            Spacer(modifier = Modifier.height(12.dp))
                            ContactDetailRow(Icons.Default.LocationOn, "123 Elegance Blvd, Suite 7", BrushedGold)
                        }

                        Spacer(modifier = Modifier.height(32.dp))
                        HorizontalDivider(color = SoftStone)
                        Spacer(modifier = Modifier.height(32.dp))

                        // BOTTOM SECTION: FORM
                        Text(
                            text = "BOOK A SESSION",
                            fontSize = 11.sp,
                            color = MutedEarth,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        )
                        Spacer(modifier = Modifier.height(24.dp))

                        ContactTextField(label = "Full Name", value = fullName, onValueChange = { fullName = it }, icon = Icons.Default.Person, accentColor = BrushedGold)
                        Spacer(modifier = Modifier.height(16.dp))
                        ContactTextField(label = "Date of Birth", value = dob, onValueChange = { dob = it }, icon = Icons.Default.DateRange, accentColor = Plum)
                        Spacer(modifier = Modifier.height(16.dp))
                        ContactTextField(label = "Email Address", value = email, onValueChange = { email = it }, icon = Icons.Default.Email, accentColor = BrushedGold)
                        Spacer(modifier = Modifier.height(16.dp))
                        ContactTextField(label = "Preferred Branch", value = location, onValueChange = { location = it }, icon = Icons.Default.LocationOn, accentColor = Plum)
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        OutlinedTextField(
                            value = message,
                            onValueChange = { message = it },
                            modifier = Modifier.fillMaxWidth().height(120.dp),
                            label = { Text("Special Requests", color = MutedEarth, fontSize = 12.sp) },
                            leadingIcon = { Icon(Icons.AutoMirrored.Filled.Message, contentDescription = null, tint = BrushedGold) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = BrushedGold,
                                unfocusedBorderColor = SoftStone,
                                focusedTextColor = DeepSlate,
                                unfocusedTextColor = DeepSlate
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        if (bookingState is BookingState.Error) {
                            Text(
                                text = (bookingState as BookingState.Error).message,
                                color = Color.Red,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                        }

                        // Submit Button
                        Button(
                            onClick = { 
                                if (fullName.isNotBlank() && email.isNotBlank() && dob.isNotBlank()) {
                                    bookingViewModel.bookSession(
                                        serviceName = "Booking Inquiry",
                                        date = dob,
                                        time = "TBD"
                                    )
                                }
                            },
                            enabled = bookingState !is BookingState.Loading,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = BrushedGold),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            if (bookingState is BookingState.Loading) {
                                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                            } else {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = "REQUEST BOOKING",
                                        fontWeight = FontWeight.SemiBold,
                                        letterSpacing = 1.sp,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
fun ContactDetailRow(icon: ImageVector, text: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(color.copy(alpha = 0.1f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = text, color = DeepSlate, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactTextField(
    label: String,
    value: String, 
    onValueChange: (String) -> Unit, 
    icon: ImageVector,
    accentColor: Color
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label, color = MutedEarth, fontSize = 12.sp) },
        leadingIcon = { Icon(icon, contentDescription = null, tint = accentColor) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = accentColor,
            unfocusedBorderColor = SoftStone,
            cursorColor = accentColor,
            focusedTextColor = DeepSlate,
            unfocusedTextColor = DeepSlate
        ),
        shape = RoundedCornerShape(12.dp),
        singleLine = true
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFF9F8F6)
@Composable
fun ContactPreview() {
    ContactContent()
}
