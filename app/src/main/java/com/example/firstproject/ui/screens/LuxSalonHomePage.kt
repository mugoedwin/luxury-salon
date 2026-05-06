package com.example.firstproject.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.example.firstproject.navigation.*

@Composable
fun LuxSalonHomePage(navController: NavController) {
    Scaffold(
        bottomBar = { SalonBottomNav(navController, ROUTE_LUX_SALON_HOME) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White)
        ) {
            // Treatment Categories
            item {
                SectionHeader("Bespoke Services")
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp), 
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ServiceIcon("Hair", "file:///android_asset/images/Hair.jpg") { 
                        navController.navigate("service_detail/hair") 
                    }
                    ServiceIcon("Facial", "file:///android_asset/images/Facial.png") { 
                        navController.navigate("service_detail/facial") 
                    }
                    ServiceIcon("Spa", "file:///android_asset/images/Spa.jpg") { 
                        navController.navigate("service_detail/spa") 
                    }
                }
            }
            
            // ... (keep existing specialist/booking sections if desired)
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        title,
        modifier = Modifier.padding(start = 24.dp, top = 32.dp, bottom = 16.dp),
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Color.Black)
    )
}

@Composable
fun ServiceIcon(name: String, imageUrl: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier.size(64.dp).clip(CircleShape).border(1.dp, Color.LightGray, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = name,
                modifier = Modifier.fillMaxSize().clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(Modifier.height(8.dp))
        Text(name, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color.Black)
    }
}

@Composable
fun SalonBottomNav(navController: NavController, currentRoute: String) {
    NavigationBar(containerColor = Color.White) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, null) },
            label = { Text("Home") },
            selected = currentRoute == ROUTE_LUX_SALON_HOME,
            onClick = { navController.navigate(ROUTE_LUX_SALON_HOME) }
        )
        // Add other items...
    }
}
