package com.example.firstproject.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.firstproject.navigation.*

val LuxuryGold = Color(0xFFD4AF37)
val DeepCharcoal = Color(0xFF121212)
val RichMaroon = Color(0xFF2D0A0A)
val SoftGray = Color(0xFFF5F5F5)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IvonneOrchardHomeScreen(navController: NavController = rememberNavController()) {
    Scaffold(
        bottomBar = { AppBottomNavigation(navController) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(RichMaroon, DeepCharcoal),
                        center = Offset(500f, 500f),
                        radius = 1500f
                    )
                )
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 20.dp)
            ) {
                // Animated Brand Identity
                FloatingAnimation {
                    Text(
                        text = "Ivonne Orchard",
                        modifier = Modifier.padding(24.dp),
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    )
                }
                
                Text(
                    text = "EST. 2009 • NAIROBI",
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 0.dp),
                    color = LuxuryGold,
                    letterSpacing = 4.sp,
                    style = MaterialTheme.typography.labelLarge
                )

                Spacer(modifier = Modifier.height(24.dp))
                
                AsyncImage(model = "file:///android_asset/images/image1.png", contentDescription = "Hero", modifier = Modifier.fillMaxWidth().height(200.dp).padding(horizontal = 16.dp).clip(RoundedCornerShape(16.dp)), contentScale = ContentScale.Crop)

                SectionTitle("Bespoke Services")
                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    ServiceIcon(Icons.Default.ContentCut, "Hair", onClick = { navController.navigate("service_detail/hair") })
                    ServiceIcon(Icons.Default.Face, "Facial", onClick = { navController.navigate("service_detail/facial") })
                    ServiceIcon(Icons.Default.AutoAwesome, "Spa", onClick = { navController.navigate("service_detail/spa") })
                    ServiceIcon(Icons.Default.Celebration, "Gloom", onClick = { navController.navigate("service_detail/gloom") })
                    ServiceIcon(Icons.Default.Star, "Glam", onClick = { navController.navigate("service_detail/glam") })
                }

                Spacer(modifier = Modifier.height(32.dp))
                SectionTitle("Our Specialists")
                LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(listOf(Specialist("Elena Rose", "Master Stylist", "Elena.jpg"), Specialist("Marcus V.", "Skin Expert", "Marcus.jpg"), Specialist("Sophia L.", "Color Specialist", "Sofia.jpg"))) { person -> SpecialistItem(person) }
                }

                Spacer(modifier = Modifier.height(32.dp))
                SectionTitle("Our Craft")
                LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(listOf("Silk wrap.jpg", "signature bob.jpg", "Gel Sculpt.jpg", "Beard sculpt.jpg")) { img ->
                        AsyncImage(model = "file:///android_asset/images/$img", contentDescription = "Craft", modifier = Modifier.size(160.dp).clip(RoundedCornerShape(16.dp)), contentScale = ContentScale.Crop)
                    }
                }
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
fun FloatingAnimation(content: @Composable () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "floating")
    val dy by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 15f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "yOffset"
    )

    Box(modifier = Modifier.graphicsLayer { translationY = dy }) { content() }
}

@Composable
fun SectionTitle(title: String) {
    Text(title, modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = LuxuryGold))
}

@Composable
fun ServiceIcon(icon: ImageVector, label: String, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { onClick() }) {
        Box(modifier = Modifier.size(60.dp).clip(CircleShape).background(SoftGray), contentAlignment = Alignment.Center) { Icon(icon, contentDescription = null, tint = DeepCharcoal) }
        Text(label, modifier = Modifier.padding(top = 8.dp), fontSize = 12.sp, color = Color.Gray)
    }
}

@Composable
fun SpecialistItem(specialist: Specialist) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        AsyncImage(model = "file:///android_asset/images/${specialist.imagePath}", contentDescription = specialist.name, modifier = Modifier.size(80.dp).clip(CircleShape), contentScale = ContentScale.Crop)
        Text(specialist.name, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp), color = Color.White)
        Text(specialist.role, fontSize = 11.sp, color = Color.Gray)
    }
}

@Composable
fun AppBottomNavigation(navController: NavController) {
    NavigationBar(containerColor = DeepCharcoal) {
        NavigationBarItem(selected = true, onClick = {}, icon = { Icon(Icons.Default.Home, null, tint = LuxuryGold) }, label = { Text("Home", color = LuxuryGold) })
        NavigationBarItem(selected = false, onClick = { navController.navigate(ROUTE_SERVICES) }, icon = { Icon(Icons.Default.ContentCut, null, tint = Color.Gray) }, label = { Text("Services", color = Color.Gray) })
        NavigationBarItem(selected = false, onClick = {}, icon = { Icon(Icons.Default.CalendarMonth, null, tint = Color.Gray) }, label = { Text("Bookings", color = Color.Gray) })
        NavigationBarItem(selected = false, onClick = { navController.navigate(ROUTE_ABOUT_US) }, icon = { Icon(Icons.Default.Info, null, tint = Color.Gray) }, label = { Text("About", color = Color.Gray) })
        NavigationBarItem(selected = false, onClick = { navController.navigate(ROUTE_CONTACT_US) }, icon = { Icon(Icons.Default.Call, null, tint = Color.Gray) }, label = { Text("Contact", color = Color.Gray) })
    }
}

data class Specialist(val name: String, val role: String, val imagePath: String)
