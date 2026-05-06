package com.example.firstproject.ui.theme

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DashboardContent() {
    val emerald = Color(0xFF00FF85)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Salon Analytics",
                fontSize = 32.sp,
                fontWeight = FontWeight.Light,
                color = DeepSlate,
                letterSpacing = 2.sp
            )
            Text(
                text = "REAL-TIME APPOINTMENT METRICS",
                fontSize = 11.sp,
                color = BrushedGold,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )
            Spacer(modifier = Modifier.height(32.dp))
        }

        // Main Resource Status
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .shadow(12.dp, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                border = BorderStroke(0.5.dp, SoftStone)
            ) {
                Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.Center) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.EventAvailable, contentDescription = null, tint = BrushedGold, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("DAILY CAPACITY", color = MutedEarth, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("18 / 25 Slots", color = DeepSlate, fontSize = 28.sp, fontWeight = FontWeight.Light)
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Custom Progress Bar
                    Box(modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape).background(SoftStone)) {
                        Box(modifier = Modifier.fillMaxWidth(0.72f).fillMaxHeight().background(Brush.horizontalGradient(listOf(BrushedGold, Plum))))
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("72% Booking rate for today's master sessions", color = DeepSlate.copy(alpha = 0.5f), fontSize = 10.sp)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Detailed Stats Grid
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                AnalyticsCard(label = "ACTIVE TEAM", value = "12 Staff", icon = Icons.Default.Face, color = BrushedGold, modifier = Modifier.weight(1f))
                AnalyticsCard(label = "VIP STATUS", value = "Enabled", icon = Icons.Default.Verified, color = Plum, modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                AnalyticsCard(label = "NEXT SESSION", value = "15 Mins", icon = Icons.Default.Timer, color = Plum, modifier = Modifier.weight(1f))
                AnalyticsCard(label = "PREMIUM", value = "Active", icon = Icons.Default.AutoAwesome, color = BrushedGold, modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(32.dp))
        }

        // System Logs / Recent Activity
        item {
            Text(
                text = "RECENT APPOINTMENTS",
                fontSize = 11.sp,
                color = MutedEarth,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.5.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        val events = listOf(
            LogEvent("Hair Design", "Confirmed: Luxury Color Session", "10:30 AM", BrushedGold),
            LogEvent("Spa Ritual", "Completed: Deep Tissue Therapy", "09:15 AM", Plum),
            LogEvent("Skincare", "New Booking: Luminous Facial", "Yesterday", BrushedGold),
            LogEvent("Account", "Member Profile Integrated", "Yesterday", Plum)
        )

        items(events) { event ->
            LogItem(event)
            Spacer(modifier = Modifier.height(12.dp))
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = { /* Emergency Contact */ },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Plum.copy(alpha = 0.1f)),
                border = BorderStroke(1.dp, Plum.copy(alpha = 0.3f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Phone, contentDescription = null, tint = Plum)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("CONTACT CONCIERGE", color = Plum, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                }
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun AnalyticsCard(label: String, value: String, icon: ImageVector, color: Color, modifier: Modifier) {
    Surface(
        modifier = modifier.height(110.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        border = BorderStroke(0.5.dp, SoftStone),
        shadowElevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.Center) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(12.dp))
            Text(value, color = DeepSlate, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
            Text(label, color = MutedEarth, fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
        }
    }
}

@Composable
fun LogItem(event: LogEvent) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        border = BorderStroke(0.5.dp, SoftStone)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(8.dp).background(event.color, CircleShape))
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(event.title, color = DeepSlate, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text(event.description, color = DeepSlate.copy(alpha = 0.5f), fontSize = 12.sp)
            }
            Text(event.time, color = MutedEarth, fontSize = 10.sp)
        }
    }
}

data class LogEvent(val title: String, val description: String, val time: String, val color: Color)

@Preview(showBackground = true, backgroundColor = 0xFFF9F8F6)
@Composable
fun DashboardPreview() {
    DashboardContent()
}
