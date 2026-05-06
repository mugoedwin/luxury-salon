package com.example.firstproject.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage

data class ServiceCategory(val title: String, val imagePath: String, val routeId: String)

@Composable
fun ServicesPage(navController: NavController) {
    val plumDeep = Color(0xFF3B091F)
    val roseMaroon = Color(0xFF5D1024)
    val deepCharcoal = Color(0xFF121212)
    val champagneGold = Color(0xFFF1D38E)

    val categories = listOf(
        ServiceCategory("HAIR", "file:///android_asset/images/Hair.jpg", "hair"),
        ServiceCategory("FACIAL", "file:///android_asset/images/Facial.png", "facial"),
        ServiceCategory("SPA", "file:///android_asset/images/Spa.jpg", "spa"),
        ServiceCategory("GLOOM", "file:///android_asset/images/image4.jpg", "gloom"),
        ServiceCategory("GLAM", "file:///android_asset/images/classic fade.jpg", "glam")
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(plumDeep, roseMaroon, deepCharcoal)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "BESPOKE SELECTIONS",
                color = champagneGold,
                style = MaterialTheme.typography.labelLarge,
                letterSpacing = 5.sp,
                fontWeight = FontWeight.Light,
                modifier = Modifier.padding(top = 32.dp, bottom = 40.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(categories) { category ->
                    BespokeIconCard(category) {
                        navController.navigate("service_detail/${category.routeId}")
                    }
                }
            }
        }
    }
}

@Composable
fun BespokeIconCard(category: ServiceCategory, onClick: () -> Unit) {
    val champagneGold = Color(0xFFF1D38E)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, champagneGold.copy(alpha = 0.4f)),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Box {
            AsyncImage(
                model = category.imagePath,
                contentDescription = category.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            // Dark overlay for readability
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
            )
            Text(
                text = category.title,
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.align(Alignment.Center),
                letterSpacing = 2.sp
            )
        }
    }
}
