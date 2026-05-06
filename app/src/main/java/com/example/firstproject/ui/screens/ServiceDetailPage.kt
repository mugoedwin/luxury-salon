package com.example.firstproject.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

data class ServiceItem(val name: String, val image: String, val detail: String, val price: String, val duration: String)

@Composable
fun ServiceDetailPage(serviceId: String, onBookNow: () -> Unit, onBack: () -> Unit) {
    val goldAccent = Color(0xFFD4AF37)
    val deepMaroon = Color(0xFF4A0404)

    val (headerImage, items) = when (serviceId) {
        "hair" -> "file:///android_asset/images/Hair.jpg" to listOf(
            ServiceItem("Champagne Blonde", "file:///android_asset/images/champagne blonde.jpg", "Luxurious blonde treatment", "Ksh 15,000", "3 hrs"),
            ServiceItem("Signature Bob", "file:///android_asset/images/signature bob.jpg", "Modern sleek cut", "Ksh 5,500", "1.5 hrs"),
            ServiceItem("Velvet Curls", "file:///android_asset/images/velvet curls.jpg", "Soft bouncy curls", "Ksh 4,000", "1 hr"),
            ServiceItem("Shava", "file:///android_asset/images/images.jpg", "Classic hair styling", "Ksh 3,500", "45 mins")
        )
        "facial" -> "file:///android_asset/images/Facial.png" to listOf(
            ServiceItem("Deep Cleansing", "file:///android_asset/images/Deep cleansing.jpg", "Purifying facial", "Ksh 4,500", "60 min"),
            ServiceItem("Anti-aging Ritual", "file:///android_asset/images/Anti aging ritual.jpg", "Youth rejuvenation", "Ksh 6,000", "90 min"),
            ServiceItem("Liks", "file:///android_asset/images/images.jpg", "Special facial treatment", "Ksh 3,000", "45 min"),
            ServiceItem("Ola", "file:///android_asset/images/images.jpg", "Signature glow ritual", "Ksh 3,500", "50 min")
        )
        "spa" -> "file:///android_asset/images/Spa.jpg" to listOf(
            ServiceItem("Spa Pedicure", "file:///android_asset/images/spa pedicure.jpg", "Relaxing foot therapy", "Ksh 2,000", "45 min"),
            ServiceItem("Hot Stone Therapy", "file:///android_asset/images/Hot stone therapy.jpg", "Deep relaxation therapy", "Ksh 5,500", "90 min"),
            ServiceItem("Cat", "file:///android_asset/images/cat.jpg", "Luxurious relaxation treatment", "Ksh 4,000", "60 min"),
            ServiceItem("Deep Tissue Massage", "file:///android_asset/images/deep tissue massage.jpg", "Muscle relief", "Ksh 4,500", "75 min")
        )
        "gloom" -> "file:///android_asset/images/image4.jpg" to listOf(
            ServiceItem("Gala Style", "file:///android_asset/images/gala style.jpg", "Red carpet ready styling", "Ksh 7,000", "120 min"),
            ServiceItem("HD Make Up", "file:///android_asset/images/hd make up.jpg", "Flawless finish", "Ksh 5,000", "90 min"),
            ServiceItem("Bridal Glow", "file:///android_asset/images/bridal glow.jpg", "Pinnacle of elegance", "Ksh 10,000", "150 min")
        )
        "glam" -> "file:///android_asset/images/classic fade.jpg" to listOf(
            ServiceItem("Beard Sculpt", "file:///android_asset/images/Beard sculpt.jpg", "Precision grooming", "Ksh 1,500", "30 min"),
            ServiceItem("Luku", "file:///android_asset/images/images.jpg", "Stylist signature look", "Ksh 2,000", "40 min"),
            ServiceItem("Hot Towel Shave", "file:///android_asset/images/hot towel shave.jpg", "Refreshing traditional shave", "Ksh 1,200", "25 min"),
            ServiceItem("Dans", "file:///android_asset/images/dans.jpg", "Exclusive style", "Ksh 2,500", "50 min")
        )
        else -> "" to emptyList()
    }

    LazyColumn(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F5F2))) {
        item {
            Box {
                AsyncImage(
                    model = headerImage,
                    contentDescription = serviceId,
                    modifier = Modifier.fillMaxWidth().height(300.dp),
                    contentScale = ContentScale.Crop
                )
            }
            Text(
                text = serviceId.uppercase(),
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = deepMaroon,
                    letterSpacing = 2.sp
                )
            )
        }

        items(items) { item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = item.image,
                        contentDescription = item.name,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    Column(modifier = Modifier.weight(1f)) {
                        Text(item.name, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
                        Text(item.detail, color = Color.Gray, fontSize = 13.sp)
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Row {
                            Text(text = item.price, color = goldAccent, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(text = item.duration, color = Color.LightGray, fontSize = 12.sp)
                        }
                    }

                    Button(
                        onClick = onBookNow,
                        colors = ButtonDefaults.buttonColors(containerColor = deepMaroon),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp)
                    ) {
                        Text("Book", fontSize = 12.sp, color = goldAccent)
                    }
                }
            }
        }
    }
}
