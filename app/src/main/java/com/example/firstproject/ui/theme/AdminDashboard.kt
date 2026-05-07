package com.example.firstproject.ui.theme

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.firstproject.ui.screens.BespokeService
import com.example.firstproject.viewmodel.AdminViewModel
import com.example.firstproject.viewmodel.AuthViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AdminDashboardContent(
    onUnauthorized: () -> Unit = {},
    onLogout: () -> Unit = {},
    authViewModel: AuthViewModel = viewModel(),
    adminViewModel: AdminViewModel = viewModel()
) {
    val luxuryGold = Color(0xFFD4AF37)
    val deepCharcoal = Color(0xFF121212)
    val richMaroon = Color(0xFF2D0A0A)
    
    var showDialog by remember { mutableStateOf(false) }
    var stats by remember { mutableStateOf(mapOf("Hair" to 0, "Facial" to 0, "Spa" to 0, "Gloom" to 0, "Glam" to 0)) }
    var adminName by remember { mutableStateOf("Admin") }
    var isVisible by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        authViewModel.getCurrentUser { user ->
            if (user == null || user.role != "admin") {
                onUnauthorized()
            } else {
                adminName = user.name
                scope.launch {
                    delay(300)
                    isVisible = true
                }
            }
        }
        val db = FirebaseFirestore.getInstance()
        db.collection("enquiries").addSnapshotListener { snapshot, _ ->
            if (snapshot != null) {
                val newStats = mutableMapOf("Hair" to 0, "Facial" to 0, "Spa" to 0, "Gloom" to 0, "Glam" to 0)
                for (doc in snapshot.documents) {
                    val type = doc.getString("serviceType") ?: "Other"
                    if (newStats.containsKey(type)) newStats[type] = (newStats[type] ?: 0) + 1
                }
                stats = newStats
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }, containerColor = luxuryGold) { 
                Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.Black) 
            }
        },
        containerColor = deepCharcoal
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().background(Brush.radialGradient(colors = listOf(richMaroon, deepCharcoal), center = Offset(500f, 500f), radius = 2500f)).padding(padding)) {
            LazyColumn(modifier = Modifier.padding(16.dp)) {
                item {
                    Row(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column {
                            AnimatedVisibility(
                                visible = isVisible,
                                enter = fadeIn(animationSpec = tween(1000)) + slideInVertically(initialOffsetY = { -40 })
                            ) {
                                Column {
                                    Text(
                                        text = "ADMIN ACCESS 🛡️",
                                        fontSize = 10.sp,
                                        color = luxuryGold.copy(alpha = 0.8f),
                                        letterSpacing = 2.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Welcome, $adminName 👋",
                                        fontSize = 24.sp,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                        IconButton(onClick = { authViewModel.logout(); onLogout() }) { Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Logout", tint = luxuryGold) }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("MANAGEMENT OVERVIEW", style = MaterialTheme.typography.labelLarge, color = luxuryGold, letterSpacing = 2.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    LuxuryBarChart(stats = stats)
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }

    if (showDialog) {
        ServiceEditDialog(onDismiss = { showDialog = false }) { 
            adminViewModel.saveService(it)
            showDialog = false 
        }
    }
}

@Composable
fun ServiceEditDialog(onDismiss: () -> Unit, onSave: (BespokeService) -> Unit) {
    val context = LocalContext.current
    var title by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("hair") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> selectedImageUri = uri }
    )
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Bespoke Service") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier.size(100.dp).clip(RoundedCornerShape(8.dp)).background(Color.Gray.copy(alpha = 0.2f)).clickable { 
                        photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) 
                    },
                    contentAlignment = Alignment.Center
                ) {
                    if (selectedImageUri != null) {
                        AsyncImage(model = selectedImageUri, contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                    } else {
                        Icon(Icons.Default.AddPhotoAlternate, "Select Image", tint = Color.White)
                    }
                }
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Title") })
                OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("Price (Ksh)") })
                OutlinedTextField(value = category, onValueChange = { category = it }, label = { Text("Category (hair/facial/spa/gloom/glam)") })
            }
        },
        confirmButton = {
            TextButton(onClick = { 
                if (title.isNotBlank() && price.isNotBlank() && selectedImageUri != null) {
                    onSave(BespokeService(category, title, "30 min", "Luxury service", price, "Best", selectedImageUri.toString()))
                    Toast.makeText(context, "Service saved successfully!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Please fill in all required fields and select an image.", Toast.LENGTH_LONG).show()
                }
            }) { Text("SAVE", color = Color(0xFFD4AF37)) }
        }
    )
}

@Composable
fun LuxuryBarChart(stats: Map<String, Int>) {
    val luxuryGold = Color(0xFFD4AF37)
    val maxVal = (stats.values.maxOrNull() ?: 1).toFloat()
    Column(modifier = Modifier.fillMaxWidth().background(Color(0xFF1E1E1E).copy(alpha = 0.8f), RoundedCornerShape(16.dp)).padding(20.dp)) {
        Text("SERVICE DEMAND (REAL-TIME)", color = luxuryGold, style = MaterialTheme.typography.labelSmall)
        Spacer(modifier = Modifier.height(20.dp))
        Row(modifier = Modifier.fillMaxWidth().height(150.dp), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.Bottom) {
            stats.forEach { (label, value) ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(modifier = Modifier.width(24.dp).height((100 * (value.toFloat() / maxVal)).dp).background(luxuryGold, RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)))
                    Text(label.take(4), color = Color.Gray, fontSize = 10.sp)
                }
            }
        }
    }
}
