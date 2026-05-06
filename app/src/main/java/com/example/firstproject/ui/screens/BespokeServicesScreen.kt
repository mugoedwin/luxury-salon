package com.example.firstproject.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun BespokeServicesScreen(
    bespokeServicesList: List<BespokeService>, 
    onServiceClick: (BespokeService) -> Unit
) {
    val luxuryGold = Color(0xFFD4AF37)
    val deepCharcoal = Color(0xFF121212)
    val richMaroon = Color(0xFF2D0A0A)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(richMaroon, deepCharcoal),
                    center = Offset(500f, 0f),
                    radius = 2500f
                )
            )
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(48.dp))
                FloatingAnimationWrapper {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "EST. 2009",
                            color = luxuryGold.copy(alpha = 0.6f),
                            style = MaterialTheme.typography.labelSmall,
                            letterSpacing = 4.sp
                        )
                        Text(
                            text = "BESPOKE SERVICES",
                            color = Color.White,
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold),
                            letterSpacing = 2.sp
                        )
                        Box(
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .height(1.dp)
                                .width(60.dp)
                                .background(luxuryGold)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }

            items(bespokeServicesList) { service ->
                DetailedServiceCard(service, luxuryGold, onServiceClick)
                Spacer(modifier = Modifier.height(20.dp))
            }
            
            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}

@Composable
fun DetailedServiceCard(
    service: BespokeService, 
    gold: Color, 
    onClick: (BespokeService) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick(service) },
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(0.5.dp, gold.copy(alpha = 0.3f)),
        elevation = CardDefaults.cardElevation(12.dp)
    ) {
        Box {
            AsyncImage(
                model = service.imagePath,
                contentDescription = service.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, Color.Black.copy(alpha = 0.9f))
                        )
                    )
                    .padding(20.dp)
            ) {
                Text(
                    text = service.title,
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                
                Text(
                    text = service.description,
                    color = Color.LightGray,
                    fontSize = 13.sp,
                    maxLines = 2,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Schedule, "", tint = gold, modifier = Modifier.size(16.dp))
                        Text(" ${service.duration}", color = gold, fontSize = 12.sp)
                        Text("  |  ${service.benefit}", color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp)
                    }
                    
                    Text(
                        text = "Ksh ${service.price}",
                        color = gold,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}

@Composable
fun FloatingAnimationWrapper(content: @Composable () -> Unit) {
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
