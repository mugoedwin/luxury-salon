package com.example.firstproject.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun AboutUsScreen(onBack: () -> Unit = {}) {
    val luxuryGold = Color(0xFFD4AF37)
    val deepCharcoal = Color(0xFF121212)
    val softMaroon = Color(0xFF4A0404)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(deepCharcoal)
    ) {
        // Hero Branding Section
        item {
            Box(modifier = Modifier.height(450.dp).fillMaxWidth()) {
                AsyncImage(
                    model = "file:///android_asset/images/About image.jpg",
                    contentDescription = "Legacy",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                // Gradient Overlay for readability
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, deepCharcoal),
                                startY = 300f
                            )
                        )
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(24.dp)
                ) {
                    Text(
                        text = "EST. 2009",
                        color = luxuryGold,
                        style = MaterialTheme.typography.labelLarge,
                        letterSpacing = 4.sp
                    )
                    Text(
                        text = "THE LEGACY OF\nIVONNE ORCHARD",
                        color = Color.White,
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            lineHeight = 42.sp
                        )
                    )
                }
            }
        }

        // The "Lux Story" Content
        item {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = "A Story of Refinement",
                    color = luxuryGold,
                    style = MaterialTheme.typography.titleMedium,
                    fontStyle = FontStyle.Italic
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "What began in 2009 as a vision for unparalleled beauty has evolved into Nairobi's most prestigious sanctuary. Ivonne Orchard isn't just a salon; it's a multi-decade commitment to the art of self-care. From our charcoal-maroon interiors to our gold-standard service, we define the pinnacle of Kenyan luxury.",
                    color = Color.LightGray,
                    lineHeight = 24.sp,
                    fontSize = 16.sp
                )
            }
        }

        // Pillars of Excellence (Horizontal Scroll)
        item {
            Text(
                text = "OUR PILLARS",
                modifier = Modifier.padding(start = 24.dp, bottom = 16.dp),
                color = Color.White.copy(alpha = 0.5f),
                letterSpacing = 2.sp
            )
            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                PillarCard("Precision", "Award-winning styling.", luxuryGold)
                PillarCard("Heritage", "Serving since 2009.", Color.White)
                PillarCard("Elegance", "Luxury UI & UX.", luxuryGold)
            }
            Spacer(modifier = Modifier.height(32.dp))
        }

        // Footer Brand Statement
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(softMaroon)
                    .padding(48.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "IVONNE ORCHARD\nPRIVATE COLLECTION",
                    textAlign = TextAlign.Center,
                    color = luxuryGold,
                    style = MaterialTheme.typography.labelSmall,
                    letterSpacing = 5.sp
                )
            }
        }
    }
}

@Composable
fun PillarCard(title: String, desc: String, accentColor: Color) {
    Card(
        modifier = Modifier.width(180.dp).height(120.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, color = accentColor, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(desc, color = Color.Gray, fontSize = 12.sp)
        }
    }
}
