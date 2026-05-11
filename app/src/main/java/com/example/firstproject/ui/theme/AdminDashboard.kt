package com.example.firstproject.ui.theme

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.firstproject.CloudinaryHelper
import com.example.firstproject.model.Service
import com.example.firstproject.ui.screens.Booking
import com.example.firstproject.viewmodel.AdminViewModel
import com.example.firstproject.viewmodel.BookingViewModel

@Composable
fun StatusButton(label: String, color: Color, modifier: Modifier, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        border = BorderStroke(1.dp, color.copy(alpha = 0.5f)),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = color),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(text = label, style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
fun AdminBookingActionCard(booking: Booking, onStatusChange: (String) -> Unit) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1412)),
        border = BorderStroke(1.dp, GoldLuxury.copy(alpha = 0.2f)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = booking.imageUrl.ifEmpty { "https://ui-avatars.com/api/?name=${booking.customerName}" },
                    contentDescription = null,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .border(1.dp, GoldLuxury, CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = booking.serviceName.uppercase(),
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = GoldLuxury,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "Client: ${booking.customerName}",
                        color = Color.White.copy(alpha = 0.7f),
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "📅 ${booking.appointmentDate} at ${booking.appointmentTime}",
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Surface(
                    color = when(booking.status) {
                        "Approved" -> Color(0xFF4CAF50).copy(alpha = 0.2f)
                        "Pending" -> Color(0xFFFFC107).copy(alpha = 0.2f)
                        else -> Color(0xFFF44336).copy(alpha = 0.2f)
                    },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(booking.status, modifier = Modifier.padding(8.dp, 4.dp), color = Color.White, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatusButton("Approve", Color(0xFF4CAF50), Modifier.weight(1f)) { 
                    onStatusChange("Approved")
                    Toast.makeText(context, "Approval email sent!", Toast.LENGTH_SHORT).show()
                }
                StatusButton("Pending", Color(0xFFFFC107), Modifier.weight(1f)) { onStatusChange("Pending") }
                StatusButton("Decline", Color(0xFFF44336), Modifier.weight(1f)) { onStatusChange("Declined") }
            }
        }
    }
}

@Composable
fun PremiumServiceCard(
    service: Service,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                shadowElevation = 4.dp
            ) {
                AsyncImage(
                    model = service.imageUrl,
                    contentDescription = null,
                    modifier = Modifier.size(80.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Column(modifier = Modifier.weight(1f).padding(start = 16.dp)) {
                Text(
                    text = service.name.uppercase(),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaroonPrimary
                )
                Text(
                    text = "Ksh ${service.price}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = GoldLuxury,
                    fontWeight = FontWeight.Medium
                )
                
                Surface(
                    color = MaroonPrimary.copy(alpha = 0.1f),
                    shape = CircleShape,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text(
                        text = service.category,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaroonPrimary
                    )
                }
            }

            Row {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = null, tint = MaroonPrimary)
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red.copy(alpha = 0.6f))
                }
            }
        }
    }
}

@Composable
fun DashboardHeader(title: String, subtitle: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(MaroonPrimary, Color(0xFF5D0000))
                ),
                shape = RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp)
            )
            .padding(horizontal = 24.dp, vertical = 40.dp)
    ) {
        Column {
            Text(
                "Ivonne Orchard",
                color = GoldLuxury,
                style = MaterialTheme.typography.labelLarge,
                letterSpacing = 2.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                title,
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                subtitle,
                color = Color.White.copy(alpha = 0.7f),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun EmptyDashboardState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Default.Inventory, contentDescription = null, modifier = Modifier.size(64.dp), tint = Color.LightGray)
        Text("Your catalog is empty", color = Color.Gray)
    }
}

@Composable
fun ServiceEditDialog(
    onDismiss: () -> Unit,
    onSave: (Service) -> Unit,
    existingService: Service? = null
) {
    val context = LocalContext.current
    var name by remember { mutableStateOf(existingService?.name ?: "") }
    var price by remember { mutableStateOf(existingService?.price ?: "") }
    val categories = listOf("Hair", "Facial", "Spa", "Gloom", "Glam")
    var category by remember { mutableStateOf(existingService?.category ?: categories.first()) }
    var expanded by remember { mutableStateOf(false) }

    var imageUrl by remember { mutableStateOf(existingService?.imageUrl ?: "") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var isUploading by remember { mutableStateOf(false) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> selectedImageUri = uri }
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (existingService == null) "New Service" else "Edit Service") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier.size(100.dp).clip(RoundedCornerShape(8.dp)).background(Color.Gray.copy(alpha = 0.2f)).clickable {
                        photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    },
                    contentAlignment = Alignment.Center
                ) {
                    if (isUploading) {
                        CircularProgressIndicator(color = GoldLuxury)
                    } else if (selectedImageUri != null) {
                        AsyncImage(model = selectedImageUri, contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                    } else if (imageUrl.isNotEmpty()) {
                        AsyncImage(model = imageUrl, contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                    } else {
                        Icon(Icons.Default.AddPhotoAlternate, "Select Image", tint = Color.White)
                    }
                }
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
                OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("Price (Ksh)") })
                Box {
                    OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
                        Text(category)
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        categories.forEach { cat ->
                            DropdownMenuItem(text = { Text(cat) }, onClick = {
                                category = cat
                                expanded = false
                            })
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (name.isNotBlank() && price.isNotBlank()) {
                    if (selectedImageUri != null) {
                        isUploading = true
                        CloudinaryHelper.uploadToCloudinary(
                            context = context,
                            imageUri = selectedImageUri!!,
                            onSuccess = { url ->
                                onSave(Service(existingService?.id ?: "", name, price, category, url))
                                onDismiss()
                            },
                            onError = { isUploading = false }
                        )
                    } else {
                        onSave(Service(existingService?.id ?: "", name, price, category, imageUrl))
                        onDismiss()
                    }
                }
            }) { Text(if (isUploading) "UPLOADING..." else "SAVE", color = GoldLuxury) }
        }
    )
}

@Composable
fun WorldClassAdminDashboard(
    adminViewModel: AdminViewModel = viewModel(),
    bookingViewModel: BookingViewModel = viewModel()
) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedService by remember { mutableStateOf<Service?>(null) }
    var showBookings by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = CreamBackground,
        floatingActionButton = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                FloatingActionButton(onClick = { showBookings = !showBookings }, containerColor = MaroonPrimary, contentColor = Color.White) {
                    Icon(Icons.AutoMirrored.Filled.ListAlt, "Toggle View")
                }
                if (!showBookings) {
                    ExtendedFloatingActionButton(
                        onClick = {
                            selectedService = null
                            showDialog = true
                        },
                        containerColor = GoldLuxury,
                        contentColor = Color.White,
                        icon = { Icon(Icons.Default.Add, "Add") },
                        text = { Text("NEW SERVICE") }
                    )
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            DashboardHeader(
                if (showBookings) "Appointments" else "Inventory Management",
                if (showBookings) "Approve or decline client requests" else "Manage your salon offerings"
            )
            
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(top = 160.dp)
            ) {
                if (showBookings) {
                    LazyColumn(contentPadding = PaddingValues(bottom = 100.dp)) {
                        items(bookingViewModel.bookings) { booking ->
                            AdminBookingActionCard(booking) { newStatus ->
                                bookingViewModel.updateBookingStatus(booking.id, newStatus)
                            }
                        }
                    }
                } else {
                    if (adminViewModel.services.isEmpty()) {
                        EmptyDashboardState()
                    } else {
                        LazyColumn(contentPadding = PaddingValues(bottom = 100.dp)) {
                            items(adminViewModel.services) { service ->
                                PremiumServiceCard(
                                    service = service,
                                    onEdit = {
                                        selectedService = service
                                        showDialog = true
                                    },
                                    onDelete = { adminViewModel.deleteService(service.id) }
                                )
                            }
                        }
                    }
                }
            }
        }

        if (showDialog) {
            ServiceEditDialog(
                onDismiss = { showDialog = false },
                onSave = { service ->
                    adminViewModel.saveService(service)
                    showDialog = false
                },
                existingService = selectedService
            )
        }
    }
}
