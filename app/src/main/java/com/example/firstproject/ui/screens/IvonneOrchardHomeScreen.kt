package com.example.firstproject.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.firstproject.navigation.*
import com.example.firstproject.viewmodel.AuthViewModel
import kotlinx.coroutines.delay

val LuxuryGold = Color(0xFFD4AF37)
val DeepCharcoal = Color(0xFF121212)
val RichMaroon = Color(0xFF2D0A0A)
val SoftGray = Color(0xFFF5F5F5)

data class ServiceItemCategory(val name: String, val imagePath: String, val route: String)
data class Testimonial(val name: String, val text: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IvonneOrchardHomeScreen(
    navController: NavController = rememberNavController(),
    initialUserName: String? = null,
    authViewModel: AuthViewModel = viewModel()
) {
    var userName by remember { mutableStateOf(initialUserName ?: "Guest") }
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (initialUserName == null) {
            authViewModel.getCurrentUser { user ->
                user?.let { userName = it.name }
            }
        }
        delay(300)
        isVisible = true
    }

    val serviceCategories = listOf(
        ServiceItemCategory("Hair", "file:///android_asset/images/Hair.jpg", "service_detail/hair"),
        ServiceItemCategory("Facial", "file:///android_asset/images/Facial.png", "service_detail/facial"),
        ServiceItemCategory("Spa", "file:///android_asset/images/Spa.jpg", "service_detail/spa"),
        ServiceItemCategory("Gloom", "file:///android_asset/images/image4.jpg", "service_detail/gloom"),
        ServiceItemCategory("Glam", "file:///android_asset/images/classic fade.jpg", "service_detail/glam")
    )

    val testimonials = listOf(
        Testimonial("Sarah J.", "The gold standard of luxury in Nairobi."),
        Testimonial("Michael K.", "Ivonne Orchard is a sanctuary for the soul."),
        Testimonial("Elena R.", "Absolutely breathtaking results every single time."),
        Testimonial("Kevin L.", "Professionalism meets unparalleled elegance.")
    )

    val ourCraftImages = listOf(
        "file:///android_asset/images/gala style.jpg",
        "file:///android_asset/images/Gel Sculpt.jpg",
        "file:///android_asset/images/gold leaf oil.jpg",
        "file:///android_asset/images/luku.jpg",
        "file:///android_asset/images/Marcus.jpg",
        "file:///android_asset/images/plumglazepolish.jpg",
        "file:///android_asset/images/radiance serum.jpg",
        "file:///android_asset/images/rose.jpg",
        "file:///android_asset/images/signature bob.jpg",
        "file:///android_asset/images/silk hair mask.jpg"
    )

    Scaffold(bottomBar = { AppBottomNavigation(navController) }) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().background(Brush.radialGradient(colors = listOf(RichMaroon, DeepCharcoal), center = Offset(500f, 500f), radius = 1500f)).padding(paddingValues)) {
            Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(bottom = 20.dp)) {
                
                // Personalized Breathtaking Header
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(animationSpec = tween(1000)) + slideInVertically(initialOffsetY = { -40 })
                ) {
                    Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 32.dp)) {
                        Text(
                            text = "CLIENT PORTAL ✨",
                            fontSize = 10.sp,
                            color = LuxuryGold.copy(alpha = 0.8f),
                            letterSpacing = 2.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Welcome, $userName 👋",
                            fontSize = 32.sp,
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            style = MaterialTheme.typography.displaySmall
                        )
                    }
                }

                FloatingAnimation {
                    Text("Ivonne Orchard", modifier = Modifier.padding(horizontal = 24.dp), style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.ExtraBold, color = Color.White))
                }
                
                Text("EST. 2009 • NAIROBI", modifier = Modifier.padding(horizontal = 24.dp), color = LuxuryGold, letterSpacing = 4.sp, style = MaterialTheme.typography.labelLarge)

                Spacer(modifier = Modifier.height(24.dp))
                
                AsyncImage(model = "file:///android_asset/images/image1.png", contentDescription = "Hero", modifier = Modifier.fillMaxWidth().height(200.dp).padding(horizontal = 16.dp).clip(RoundedCornerShape(16.dp)), contentScale = ContentScale.Crop)

                SectionTitle("Bespoke Services")
                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    serviceCategories.forEach { category -> ServiceImageButton(category) { navController.navigate(category.route) } }
                }

                Spacer(modifier = Modifier.height(32.dp))
                SectionTitle("Our Craft")
                LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(ourCraftImages) { imageUrl ->
                        AsyncImage(model = imageUrl, contentDescription = "Craft", modifier = Modifier.size(120.dp).clip(RoundedCornerShape(8.dp)), contentScale = ContentScale.Crop)
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
                SectionTitle("Client Voices")
                
                val scrollState = rememberLazyListState()
                LaunchedEffect(Unit) {
                    while (true) {
                        scrollState.animateScrollToItem(scrollState.firstVisibleItemIndex + 1)
                        delay(2000)
                    }
                }

                LazyRow(state = scrollState, contentPadding = PaddingValues(horizontal = 24.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(testimonials + testimonials) { t ->
                        Card(modifier = Modifier.width(300.dp), colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f))) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                Text("★★★★★", color = LuxuryGold, fontSize = 12.sp)
                                Text("“${t.text}”", color = Color.White, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(vertical = 8.dp))
                                Text("— ${t.name}", color = LuxuryGold, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(title, modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = LuxuryGold))
}

@Composable
fun ServiceImageButton(category: ServiceItemCategory, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { onClick() }) {
        Box(modifier = Modifier.size(60.dp).clip(CircleShape).background(SoftGray), contentAlignment = Alignment.Center) { 
            AsyncImage(model = category.imagePath, contentDescription = category.name, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
        }
        Text(category.name, modifier = Modifier.padding(top = 8.dp), fontSize = 12.sp, color = Color.Gray)
    }
}

@Composable
fun FloatingAnimation(content: @Composable () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "floating")
    val dy by infiniteTransition.animateFloat(initialValue = 0f, targetValue = 15f, animationSpec = infiniteRepeatable(animation = tween(2000, easing = FastOutSlowInEasing), repeatMode = RepeatMode.Reverse), label = "yOffset")
    Box(modifier = Modifier.graphicsLayer { translationY = dy }) { content() }
}

@Composable
fun AppBottomNavigation(navController: NavController) {
    NavigationBar(containerColor = DeepCharcoal) {
        NavigationBarItem(selected = true, onClick = {}, icon = { Icon(Icons.Default.Home, null, tint = LuxuryGold) }, label = { Text("Home", color = LuxuryGold) })
        NavigationBarItem(selected = false, onClick = { navController.navigate(ROUTE_SERVICES) }, icon = { Icon(Icons.Default.ContentCut, null, tint = Color.Gray) }, label = { Text("Services", color = Color.Gray) })
        NavigationBarItem(selected = false, onClick = { navController.navigate("bookings") }, icon = { Icon(Icons.Default.CalendarMonth, null, tint = Color.Gray) }, label = { Text("Bookings", color = Color.Gray) })
        NavigationBarItem(selected = false, onClick = { navController.navigate(ROUTE_ABOUT_US) }, icon = { Icon(Icons.Default.Info, null, tint = Color.Gray) }, label = { Text("About", color = Color.Gray) })
        NavigationBarItem(selected = false, onClick = { navController.navigate(ROUTE_CONTACT_US) }, icon = { Icon(Icons.Default.Call, null, tint = Color.Gray) }, label = { Text("Contact", color = Color.Gray) })
    }
}
