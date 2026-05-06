package com.example.firstproject.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.firstproject.ui.theme.LuxGold
import com.example.firstproject.ui.theme.LuxDeepGraphite
import kotlinx.coroutines.delay
import kotlinx.coroutines.yield

@Composable
fun LuxStoryScreen(
    onProceed: () -> Unit = {}
) {
    // State to trigger the animation when the screen loads
    var visible by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        visible = true
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // --- BACKGROUND LAYER ---
        // Using one of the stock photos as an abstract background
        AsyncImage(
            model = "file:///android_asset/images/istockphoto-1384893182-2048x2048.jpg",
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .blur(10.dp) // Subtle blur for depth
        )

        // Vertical Scrim Gradient for readability
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.8f)
                        )
                    )
                )
        )

        // --- MAIN CONTENT ---
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(1000)) + 
                    slideInVertically(initialOffsetY = { it / 10 }, animationSpec = tween(1000)),
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 40.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // HEADER: LUX Brand
                    Text(
                        text = "LUX",
                        fontSize = 48.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = LuxGold,
                        letterSpacing = 8.sp
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // ELEMENT: Auto-sliding Header Slider
                    LuxImageSlider()

                    Spacer(modifier = Modifier.height(32.dp))

                    // STORY CARD: Glassmorphism
                    Surface(
                        color = Color.White.copy(alpha = 0.05f),
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(24.dp)) {
                            Text(
                                text = "A New Era of Light",
                                style = MaterialTheme.typography.headlineSmall,
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Text(
                                text = "Experience the intersection of elegance and technology. Our story begins with the simple idea that luxury isn't about what you own, but how you feel in the moment.",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.LightGray,
                                lineHeight = 28.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                // ACTION: Proceed Button
                Button(
                    onClick = { onProceed() },
                    colors = ButtonDefaults.buttonColors(containerColor = LuxGold),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .height(58.dp)
                        .fillMaxWidth(0.85f)
                ) {
                    Text(
                        text = "BEGIN JOURNEY",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
            }
        }
    }
}

@Composable
fun LuxImageSlider() {
    val images = listOf(
        "file:///android_asset/images/image2.jpg",
        "file:///android_asset/images/gold leaf oil.jpg",
        "file:///android_asset/images/radiance serum.jpg"
    )
    
    val pagerState = rememberPagerState(pageCount = { images.size })

    // Auto-scroll logic every 3 seconds
    LaunchedEffect(Unit) {
        while(true) {
            yield()
            delay(3000)
            if (images.isNotEmpty()) {
                val nextPage = (pagerState.currentPage + 1) % images.size
                pagerState.animateScrollToPage(nextPage)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            AsyncImage(
                model = images[page],
                contentDescription = "Lux Visual",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
