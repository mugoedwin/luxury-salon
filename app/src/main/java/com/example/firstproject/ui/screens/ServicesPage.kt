package com.example.firstproject.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage

data class ServiceCategory(val title: String, val subtitle: String, val imagePath: String, val routeId: String)

@Composable
fun ServicesPage(navController: NavController) {
    val luxuryGold = Color(0xFFD4AF37)
    val deepCharcoal = Color(0xFF121212)
    val richMaroon = Color(0xFF2D0A0A)

    val categories = listOf(
        ServiceCategory("COUTURE HAIR DESIGN", "Styled by master artisans.", "file:///android_asset/images/Hair.jpg", "hair"),
        ServiceCategory("ROYAL GOLD FACIAL", "24k gold infused rejuvenation.", "file:///android_asset/images/Facial.png", "facial"),
        ServiceCategory("PRIVATE SPA RITUALS", "Absolute confusion and serenity.", "file:///android_asset/images/Spa.jpg", "spa"),
        ServiceCategory("BRIDAL ATELIER", "The pinnacle of wedding elegance.", "file:///android_asset/images/bridal glow.jpg", "gloom"),
        ServiceCategory("MODERN GLAM", "Exquisite styling for you.", "file:///android_asset/images/classic fade.jpg", "glam")
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(richMaroon, deepCharcoal),
                    center = Offset(500f, 0f),
                    radius = 2000f
                )
            )
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    "EXCLUSIVE SERVICES",
                    color = luxuryGold,
                    style = MaterialTheme.typography.labelLarge,
                    letterSpacing = 4.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            items(categories) { category ->
                ServiceCategoryItem(category) {
                    navController.navigate("service_detail/${category.routeId}")
                }
            }
        }
    }
}

@Composable
fun ServiceCategoryItem(category: ServiceCategory, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f)),
        border = BorderStroke(0.5.dp, Color(0xFFD4AF37).copy(alpha = 0.3f))
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = category.imagePath,
                contentDescription = category.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            // Dark overlay for readability
            Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f)))
            
            Column(
                modifier = Modifier.align(Alignment.CenterStart).padding(24.dp)
            ) {
                Text(
                    text = category.title,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = category.subtitle,
                    color = Color(0xFFD4AF37), // Luxury Gold
                    fontSize = 14.sp
                )
            }
        }
    }
}
