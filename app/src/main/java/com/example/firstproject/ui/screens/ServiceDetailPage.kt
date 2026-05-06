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

data class ServiceItem(val name: String, val image: String, val detail: String)

@Composable
fun ServiceDetailPage(serviceId: String, onBack: () -> Unit) {
    val (headerImage, items) = when (serviceId) {
        "hair" -> "file:///android_asset/images/Hair.jpg" to listOf(
            ServiceItem("Champagne Blonde", "file:///android_asset/images/champagne blonde.jpg", "Luxurious blonde treatment"),
            ServiceItem("Signature Bob", "file:///android_asset/images/signature bob.jpg", "Modern sleek cut"),
            ServiceItem("Velvet Curls", "file:///android_asset/images/velvet curls.jpg", "Soft bouncy curls")
        )
        "facial" -> "file:///android_asset/images/Facial.png" to listOf(
            ServiceItem("Deep Cleansing", "file:///android_asset/images/Deep cleansing.jpg", "Purifying facial"),
            ServiceItem("Anti-aging Ritual", "file:///android_asset/images/Anti aging ritual.jpg", "Youth rejuvenation")
        )
        "spa" -> "file:///android_asset/images/Spa.jpg" to listOf(
            ServiceItem("Spa Pedicure", "file:///android_asset/images/spa pedicure.jpg", "Relaxing foot therapy")
        )
        "gloom" -> "file:///android_asset/images/image4.jpg" to listOf(
            ServiceItem("Gala Style", "file:///android_asset/images/gala style.jpg", "Red carpet ready styling"),
            ServiceItem("HD Make Up", "file:///android_asset/images/hd make up.jpg", "Flawless finish"),
            ServiceItem("Bridal Glow", "file:///android_asset/images/bridal glow.jpg", "Pinnacle of elegance")
        )
        "glam" -> "file:///android_asset/images/classic fade.jpg" to listOf(
            ServiceItem("Beard Sculpt", "file:///android_asset/images/Beard sculpt.jpg", "Precision grooming"),
            ServiceItem("Luku", "file:///android_asset/images/images.jpg", "Stylist signature look"),
            ServiceItem("Hot Towel Shave", "file:///android_asset/images/hot towel shave.jpg", "Refreshing traditional shave"),
            ServiceItem("Dans", "file:///android_asset/images/dans.jpg", "Exclusive style")
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
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
            )
        }
        items(items) { item ->
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = item.image,
                    contentDescription = item.name,
                    modifier = Modifier.size(80.dp).clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(item.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(item.detail, color = Color.Gray, fontSize = 14.sp)
                }
            }
        }
    }
}
