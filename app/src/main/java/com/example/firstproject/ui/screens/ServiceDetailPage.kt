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
import androidx.compose.runtime.Composable
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

data class BespokeService(
    val id: String,
    val title: String,
    val duration: String,
    val description: String,
    val price: String,
    val benefit: String,
    val imagePath: String
)

@Composable
fun ServiceDetailPage(serviceId: String, onBookNow: () -> Unit, onBack: () -> Unit) {
    val champagneGold = Color(0xFFF1D38E)
    val context = LocalContext.current
    
    val (headerImage, services) = when (serviceId) {
        "hair" -> "file:///android_asset/images/Hair.jpg" to listOf(
            BespokeService("1", "Champagne Blonde", "3 hrs", "Luxurious blonde treatment", "15,000", "Deep Hydration", "file:///android_asset/images/champagne blonde.jpg"),
            BespokeService("2", "Signature Bob", "1.5 hrs", "Modern sleek cut", "5,500", "Precision Cut", "file:///android_asset/images/signature bob.jpg"),
            BespokeService("3", "Velvet Curls", "1 hr", "Soft bouncy curls", "4,000", "Volume Boost", "file:///android_asset/images/velvet curls.jpg"),
            BespokeService("4", "Shava", "45 mins", "Classic hair styling", "2,500", "Fresh Look", "file:///android_asset/images/images.jpg")
        )
        "facial" -> "file:///android_asset/images/Facial.png" to listOf(
            BespokeService("5", "Deep Cleansing", "60 min", "Purifying facial", "4,500", "Clear Skin", "file:///android_asset/images/Deep cleansing.jpg"),
            BespokeService("6", "Anti-aging Ritual", "90 min", "Youth rejuvenation", "6,000", "Collagen Boost", "file:///android_asset/images/Anti aging ritual.jpg"),
            BespokeService("7", "Liks", "45 min", "Special facial treatment", "3,000", "Skin Glow", "file:///android_asset/images/images.jpg"),
            BespokeService("8", "Ola", "50 min", "Signature glow ritual", "3,500", "Radiance", "file:///android_asset/images/images.jpg")
        )
        "spa" -> "file:///android_asset/images/Spa.jpg" to listOf(
            BespokeService("9", "Spa Pedicure", "45 min", "Relaxing foot therapy", "2,000", "Smooth Skin", "file:///android_asset/images/spa pedicure.jpg"),
            BespokeService("10", "Hot Stone Therapy", "90 min", "Deep relaxation therapy", "5,500", "Tension Relief", "file:///android_asset/images/Hot stone therapy.jpg"),
            BespokeService("11", "Cat", "60 min", "Luxurious relaxation treatment", "4,000", "Deep Calm", "file:///android_asset/images/cat.jpg"),
            BespokeService("12", "Deep Tissue Massage", "75 min", "Muscle relief", "4,500", "Muscle Recovery", "file:///android_asset/images/deep tissue massage.jpg")
        )
        "gloom" -> "file:///android_asset/images/image4.jpg" to listOf(
            BespokeService("13", "Gala Style", "120 min", "Red carpet ready styling", "7,000", "Glamour", "file:///android_asset/images/gala style.jpg"),
            BespokeService("14", "HD Make Up", "90 min", "Flawless finish", "5,000", "Perfect Glow", "file:///android_asset/images/hd make up.jpg"),
            BespokeService("15", "Bridal Glow", "150 min", "Pinnacle of elegance", "10,000", "Bridal Luxury", "file:///android_asset/images/bridal glow.jpg")
        )
        "glam" -> "file:///android_asset/images/classic fade.jpg" to listOf(
            BespokeService("16", "Beard Sculpt", "30 min", "Precision grooming", "1,500", "Sharp Look", "file:///android_asset/images/Beard sculpt.jpg"),
            BespokeService("17", "Luku", "40 min", "Stylist signature look", "2,000", "Modern Trend", "file:///android_asset/images/images.jpg"),
            BespokeService("18", "Hot Towel Shave", "25 min", "Refreshing traditional shave", "1,200", "Smooth Skin", "file:///android_asset/images/hot towel shave.jpg"),
            BespokeService("19", "Dans", "50 min", "Exclusive style", "2,500", "Custom Look", "file:///android_asset/images/dans.jpg")
        )
        else -> "" to emptyList()
    }

    LazyColumn(modifier = Modifier.fillMaxSize().background(Color.White)) {
        item {
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
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, champagneGold.copy(alpha = 0.3f))
    ) {
        Box {
            AsyncImage(
                model = service.imagePath,
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
