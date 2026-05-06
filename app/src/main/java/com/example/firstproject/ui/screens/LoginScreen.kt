package com.example.firstproject.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.firstproject.ui.theme.GlowingTextField
import com.example.firstproject.viewmodel.AuthState
import com.example.firstproject.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit = {},
    onLoginSuccess: (String) -> Unit = {},
    viewModel: AuthViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val authState by viewModel.authState

    // Navigate on success
    LaunchedEffect(authState) {
        val currentStatus = authState
        if (currentStatus is AuthState.Success) {
            onLoginSuccess(currentStatus.role)
            viewModel.resetState()
        }
    }

    // Validation
    val isFormValid = email.isNotBlank() && password.length >= 6

    // Color Palette
    val purple = Color(0xFF8E2DE2)
    val deepBlue = Color(0xFF4A00E0)
    val hotPink = Color(0xFFFF0099)
    val neonBlue = Color(0xFF00D2FF)

    val infiniteTransition = rememberInfiniteTransition(label = "login_glow")
    val glowPulse by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "pulse"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(deepBlue, purple, hotPink),
                    start = Offset(0f, 0f),
                    end = Offset.Infinite
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Decorative Elements
        Box(
            modifier = Modifier
                .size(350.dp)
                .offset(x = (-150).dp, y = (-300).dp)
                .background(hotPink.copy(alpha = 0.15f * glowPulse), CircleShape)
                .blur(100.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .shadow(elevation = 40.dp, shape = RoundedCornerShape(32.dp)),
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.08f)),
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f))
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 32.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Login", fontSize = 36.sp, fontWeight = FontWeight.Black, color = Color.White)
                
                Text(
                    "Welcome back to Lux Salon",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.6f),
                    modifier = Modifier.padding(top = 4.dp, bottom = 32.dp)
                )

                GlowingTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = "Email Address",
                    icon = Icons.Default.Email,
                    accentColor = neonBlue,
                    keyboardType = KeyboardType.Email
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                GlowingTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = "Password",
                    icon = Icons.Default.Lock,
                    accentColor = purple,
                    isPassword = true
                )

                Spacer(modifier = Modifier.height(15.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "Forgot Password?",
                        color = neonBlue,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (authState is AuthState.Error) {
                    Text(
                        text = (authState as AuthState.Error).message,
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                Button(
                    onClick = { viewModel.loginUser(email, password) },
                    enabled = isFormValid && authState !is AuthState.Loading,
                    modifier = Modifier.fillMaxWidth().height(60.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(0.dp),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = if (isFormValid) Brush.horizontalGradient(listOf(neonBlue, purple, hotPink)) 
                                        else Brush.horizontalGradient(listOf(Color.Gray, Color.DarkGray)),
                                shape = RoundedCornerShape(18.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (authState is AuthState.Loading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        } else {
                            Text("SIGN IN", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                
                TextButton(onClick = onNavigateToRegister) {
                    Text("Don't have an account? Create One", color = Color.White.copy(alpha = 0.7f))
                }
            }
        }
    }
}
