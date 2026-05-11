package com.example.firstproject.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Timer
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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.firebase.firestore.FirebaseFirestore

data class BespokeService(
    val category: String = "",
    val title: String = "",
    val duration: String = "",
    val description: String = "",
    val price: String = "",
    val benefit: String = "",
    val imageUrl: String = "" // Updated to match Firestore
)

@Composable
fun ServiceDetailPage(serviceId: String, onBookNow: () -> Unit, onBack: () -> Unit) {
    val champagneGold = Color(0xFFF1D38E)
    val context = LocalContext.current
    var services by remember { mutableStateOf<List<BespokeService>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(serviceId) {
        val db = FirebaseFirestore.getInstance()
        // FIXED: Changed collection from "bespoke_services" to "services"
        db.collection("services")
            .whereEqualTo("category", serviceId.replaceFirstChar { it.uppercase() }) // Capitalize to match Firestore "Spa"
            .get()
            .addOnSuccessListener { snapshot ->
                services = snapshot.toObjects(BespokeService::class.java)
                isLoading = false
            }
            .addOnFailureListener {
                isLoading = false
            }
    }

    LazyColumn(modifier = Modifier.fillMaxSize().background(Color.White)) {
        item {
            val headerImage = when (serviceId.lowercase()) {
                "hair" -> "file:///android_asset/images/Hair.jpg"
                "facial" -> "file:///android_asset/images/Facial.png"
                "spa" -> "file:///android_asset/images/Spa.jpg"
                "gloom" -> "file:///android_asset/images/image4.jpg"
                "glam" -> "file:///android_asset/images/classic fade.jpg"
                else -> ""
            }
            
            AsyncImage(
                model = headerImage,
                contentDescription = serviceId,
                modifier = Modifier.fillMaxWidth().height(250.dp),
                contentScale = ContentScale.Crop
            )
            Text(
                text = serviceId.uppercase(),
                modifier = Modifier.padding(24.dp),
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold)
            )
            Button(
                onClick = onBookNow,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = champagneGold)
            ) {
                Text("PROCEED TO BOOKINGS", color = Color.Black, fontWeight = FontWeight.Bold)
            }
            
            if (isLoading) {
                Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = champagneGold)
                }
            }
        }
        items(services) { service ->
            DetailedServiceCard(service) {
                BookingManager.addToCart(it)
                Toast.makeText(context, "${it.title} added to bookings", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@Composable
fun DetailedServiceCard(service: BespokeService, onAddToCart: (BespokeService) -> Unit) {
    val champagneGold = Color(0xFFF1D38E)
    val context = LocalContext.current
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, champagneGold.copy(alpha = 0.3f))
    ) {
        Box {
            // Using Coil ImageRequest for better URL handling
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(service.imageUrl.replace("http://", "https://"))
                    .crossfade(true)
                    .build(),
                contentDescription = service.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth().height(220.dp)
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.7f))
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = service.title.uppercase(),
                        color = champagneGold,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 2.sp
                    )
                    Text(
                        text = "Ksh ${service.price}",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = service.description,
                    color = Color.LightGray,
                    fontSize = 12.sp,
                    maxLines = 2
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Timer, 
                            contentDescription = null, 
                            tint = champagneGold, 
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = " ${service.duration} • ${service.benefit}",
                            color = champagneGold,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Button(
                        onClick = { onAddToCart(service) },
                        colors = ButtonDefaults.buttonColors(containerColor = champagneGold)
                    ) {
                        Text("ADD", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
