package com.example.firstproject.ui.theme

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
    val adminPrimary = Color(0xFF1A237E) // Deep Indigo for Admin

    // STEP 4: BLOCK UNAUTHORIZED ACCESS
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
                    .background(adminPrimary)
                    .padding(horizontal = 24.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "ADMIN CONSOLE",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                    IconButton(onClick = {
                        viewModel.logout()
                        onLogout()
                    }) {
                        Icon(Icons.Default.Logout, contentDescription = "Logout", tint = Color.White)
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(BoneWhite)
                .padding(horizontal = 24.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Management Overview",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Light,
                    color = DeepSlate
                )
                Spacer(modifier = Modifier.height(32.dp))
            }

            // High-Level Management Cards
            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    AdminStatCard(
                        label = "TOTAL USERS",
                        value = "1,240",
                        icon = Icons.Default.People,
                        color = adminPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    AdminStatCard(
                        label = "PENDING",
                        value = "45",
                        icon = Icons.Default.PendingActions,
                        color = Color.Red,
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                Text(
                    text = "ADMINISTRATIVE TOOLS",
                    fontSize = 11.sp,
                    color = MutedEarth,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Quick Actions
            item {
                AdminActionRow("Inventory Management", Icons.Default.Inventory, adminPrimary)
                Spacer(modifier = Modifier.height(12.dp))
                AdminActionRow("Staff Scheduling", Icons.Default.CalendarMonth, adminPrimary)
                Spacer(modifier = Modifier.height(12.dp))
                AdminActionRow("Financial Reports", Icons.Default.Payments, adminPrimary)
                Spacer(modifier = Modifier.height(32.dp))
            }
            
            item {
                Button(
                    onClick = { /* Handle logs */ },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = adminPrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("VIEW SYSTEM LOGS", color = Color.White, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
fun AdminStatCard(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color, modifier: Modifier) {
    Surface(
        modifier = modifier.height(120.dp),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.Center) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(28.dp))
            Spacer(modifier = Modifier.height(12.dp))
            Text(value, color = DeepSlate, fontWeight = FontWeight.Bold, fontSize = 22.sp)
            Text(label, color = MutedEarth, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun AdminActionRow(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        border = BorderStroke(1.dp, SoftStone)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(label, color = DeepSlate, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = SoftStone)
        }
    }
}
