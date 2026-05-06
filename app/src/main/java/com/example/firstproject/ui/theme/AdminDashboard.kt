package com.example.firstproject.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.firstproject.viewmodel.AuthViewModel

@Composable
fun AdminDashboardContent(
    onUnauthorized: () -> Unit = {},
    onLogout: () -> Unit = {},
    viewModel: AuthViewModel = viewModel()
) {
    val luxuryGold = Color(0xFFD4AF37)
    val deepCharcoal = Color(0xFF1A1A1A)
    val cardBackground = Color(0xFF252525)

    // BLOCK UNAUTHORIZED ACCESS
    LaunchedEffect(Unit) {
        viewModel.getCurrentUser { user ->
            if (user == null || user.role != "admin") {
                onUnauthorized()
            }
        }
    }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(deepCharcoal)
                    .padding(horizontal = 24.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "IVONNE ORCHARD ADMIN",
                        color = luxuryGold,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                    IconButton(onClick = {
                        viewModel.logout()
                        onLogout()
                    }) {
                        Icon(Icons.Default.Logout, contentDescription = "Logout", tint = luxuryGold)
                    }
                }
            }
        },
        containerColor = deepCharcoal
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            // Analytics Title
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "ANALYTICS OVERVIEW",
                    style = MaterialTheme.typography.labelMedium,
                    color = luxuryGold,
                    letterSpacing = 3.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Business Intelligence",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            // KPI Row (Key Performance Indicators)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard("Revenue", "Ksh 1.2M", luxuryGold, Modifier.weight(1f))
                    StatCard("Bookings", "482", Color.White, Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard("New Clients", "+12%", Color(0xFF4CAF50), Modifier.weight(1f))
                    StatCard("Avg. Spend", "Ksh 4,500", Color.White, Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Sales Visualization Section
            item {
                Text(
                    text = "Monthly Revenue Trend",
                    color = Color.LightGray,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                // Visual Placeholder for Graph
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    colors = CardDefaults.cardColors(containerColor = cardBackground),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Text("Graph Visualization: [May 2026 Sales Data]", color = luxuryGold.copy(alpha = 0.5f))
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Recent Activity Section
            item {
                Text(
                    text = "Recent Transactions",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            items(5) { // Replace with actual transaction list
                TransactionItem(luxuryGold)
            }
            
            item {
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "Tracking performance and growth since brand inception in 2009.",
                    color = Color.Gray,
                    fontSize = 11.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
fun StatCard(title: String, value: String, valueColor: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color(0xFF252525)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, color = Color.Gray, fontSize = 12.sp)
            Text(text = value, color = valueColor, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
        }
    }
}

@Composable
fun TransactionItem(luxuryGold: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF252525))
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text("Signature Bob Cut", color = Color.White, fontWeight = FontWeight.Bold)
            Text("Customer: Elena", color = Color.Gray, fontSize = 12.sp)
        }
        Text("Ksh 5,500", color = luxuryGold, fontWeight = FontWeight.Bold)
    }
}
