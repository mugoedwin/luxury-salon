package com.example.firstproject.ui.theme

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.firstproject.R

// --- 1. DATA MODEL ---
data class SalonService(
    val id: String,
    val name: String,
    val price: String,
    val icon: Int // Resource ID
)

val luxServices = listOf(
    SalonService("1", "Hair Styling", "From KES 2,500", R.drawable.ic_launcher_foreground), // Replace with your icon resources
    SalonService("2", "Facial Treatment", "From KES 4,000", R.drawable.ic_launcher_foreground),
    SalonService("3", "Manicure & Pedicure", "From KES 3,500", R.drawable.ic_launcher_foreground),
    SalonService("4", "Body Massage", "From KES 5,000", R.drawable.ic_launcher_foreground),
    SalonService("5", "Bespoke Grooming", "From KES 3,000", R.drawable.ic_launcher_foreground),
    SalonService("6", "Spa Package", "From KES 10,000", R.drawable.ic_launcher_foreground)
)

// --- 2. UI IMPLEMENTATION ---
@Composable
fun ServicesContent(onServiceClick: (SalonService) -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        Text(
            text = "OUR SERVICES",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFFD4AF37) // Signature Gold
            ),
            modifier = Modifier.padding(bottom = 8.dp, top = 40.dp)
        )
        
        Text(
            text = "Exquisite treatments tailored for you.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(luxServices) { service ->
                ServiceCard(service = service, onClick = { onServiceClick(service) })
            }
        }
    }
}

@Composable
fun ServiceCard(service: SalonService, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
        border = BorderStroke(1.dp, Color(0xFFD4AF37)) // Gold border
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                painter = painterResource(id = service.icon),
                contentDescription = service.name,
                tint = Color(0xFFD4AF37),
                modifier = Modifier.size(48.dp)
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = service.name,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            
            Text(
                text = service.price,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}
