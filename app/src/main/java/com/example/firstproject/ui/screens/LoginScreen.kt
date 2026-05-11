package com.example.firstproject.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.firstproject.R
import com.example.firstproject.ui.theme.GlowingTextField
import com.example.firstproject.ui.theme.GoldLuxury
import com.example.firstproject.ui.theme.MaroonPrimary
import com.example.firstproject.viewmodel.AuthState
import com.example.firstproject.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    role: String = "customer",
    onNavigateToRegister: () -> Unit = {},
    onLoginSuccess: (String, String) -> Unit = { _, _ -> },
    viewModel: AuthViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val authState by viewModel.authState

    LaunchedEffect(authState) {
        val currentStatus = authState
        if (currentStatus is AuthState.Success) {
            onLoginSuccess(currentStatus.role, currentStatus.userName)
            viewModel.resetState()
        }
    }

    val isFormValid = email.isNotBlank() && password.length >= 6

    Box(modifier = Modifier.fillMaxSize()) {
        // Background Image using login.jpg
        Image(
            painter = painterResource(id = R.drawable.login),
            contentDescription = "Salon Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Dark Overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
        )

        Card(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .padding(24.dp)
                .shadow(elevation = 20.dp, shape = RoundedCornerShape(32.dp)),
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.15f)),
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.3f))
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 32.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Welcome Back",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
                
                Text(
                    "Login to Ivonne Orchard",
                    fontSize = 14.sp,
                    color = GoldLuxury,
                    modifier = Modifier.padding(top = 4.dp, bottom = 32.dp)
                )

                GlowingTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = "Email Address",
                    icon = Icons.Default.Email,
                    accentColor = GoldLuxury,
                    keyboardType = KeyboardType.Email
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                GlowingTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = "Password",
                    icon = Icons.Default.Lock,
                    accentColor = GoldLuxury,
                    isPassword = true
                )

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
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaroonPrimary),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    if (authState is AuthState.Loading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text("SIGN IN", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                
                TextButton(onClick = onNavigateToRegister) {
                    Text("Don't have an account? Join us", color = GoldLuxury)
                }
            }
        }
    }
}
