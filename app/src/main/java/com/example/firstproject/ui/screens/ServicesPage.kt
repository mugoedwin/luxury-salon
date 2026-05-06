package com.example.firstproject.ui.screens

import android.util.Log
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
    val categories = listOf(
        ServiceCategory("COUTURE HAIR DESIGN", "Styled by master artisans.", "file:///android_asset/images/Hair.jpg", "hair"),
        ServiceCategory("ROYAL GOLD FACIAL", "24k gold infused rejuvenation.", "file:///android_asset/images/Facial.png", "facial"),
        ServiceCategory("PRIVATE SPA RITUALS", "Absolute confusion and serenity.", "file:///android_asset/images/Spa.jpg", "spa"),
        ServiceCategory("BRIDAL ATELIER", "The pinnacle of wedding elegance.", "file:///android_asset/images/bridal glow.jpg", "bridal")
    )

    Column(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        Text(
            "SERVICES",
            modifier = Modifier.padding(24.dp),
            style = MaterialTheme.typography.headlineMedium.copy(color = Color.White, fontWeight = FontWeight.Bold)
        )
        
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(categories) { category ->
                ServiceCategoryItem(category) {
                    Log.d("ServicesPage", "Clicked on: ${category.routeId}")
                    navController.navigate("service_detail/${category.routeId}")
                }
            }
        }
    }
}

@Composable
fun ServiceCategoryItem(category: ServiceCategory, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
    ) {
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
