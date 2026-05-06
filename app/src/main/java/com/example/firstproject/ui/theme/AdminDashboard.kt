package com.example.firstproject.ui.theme

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.firstproject.ui.screens.BespokeService
import com.example.firstproject.viewmodel.AdminViewModel
import com.example.firstproject.viewmodel.AuthViewModel
import com.google.firebase.firestore.FirebaseFirestore

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

    LaunchedEffect(Unit) {
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
        Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(richMaroon, deepCharcoal))).padding(padding)) {
            LazyColumn(modifier = Modifier.padding(16.dp)) {
                item {
                    Text("MANAGEMENT PORTAL", style = MaterialTheme.typography.headlineMedium, color = Color.White, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(24.dp))
                    LuxuryBarChart(stats)
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("MANAGE SERVICES", color = Color.White, style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }

    if (showDialog) {
        ServiceEditDialog(null, { showDialog = false }) { adminViewModel.saveService(it); showDialog = false }
    }
}

@Composable
fun ServiceEditDialog(service: BespokeService?, onDismiss: () -> Unit, onSave: (BespokeService) -> Unit) {
    var title by remember { mutableStateOf(service?.title ?: "") }
    var price by remember { mutableStateOf(service?.price ?: "") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> selectedImageUri = uri }
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Service") },
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
            }
        },
        confirmButton = {
            TextButton(onClick = { onSave(BespokeService("id", title, "30 min", "Luxury service", price, "Best", selectedImageUri.toString())) }) { Text("SAVE", color = Color(0xFFD4AF37)) }
        }
    )
}

@Composable
fun LuxuryBarChart(stats: Map<String, Int>) {
    val gold = Color(0xFFD4AF37)
    Column(modifier = Modifier.fillMaxWidth().background(Color(0xFF1E1E1E), RoundedCornerShape(16.dp)).padding(20.dp)) {
        Text("SERVICE DEMAND (REAL-TIME)", color = gold, fontSize = 10.sp)
        Spacer(modifier = Modifier.height(20.dp))
        Row(modifier = Modifier.fillMaxWidth().height(150.dp), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.Bottom) {
            stats.forEach { (label, value) ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(modifier = Modifier.width(24.dp).height((100 * (value.toFloat() / (stats.values.maxOrNull()?.toFloat() ?: 1f))).dp).background(gold, RoundedCornerShape(4.dp)))
                    Text(label.take(4), color = Color.Gray, fontSize = 10.sp)
                }
            }
        }
    }
}
