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
fun RegisterScreen(
    onBackToLogin: () -> Unit = {},
    onRegisterSuccess: (String, String) -> Unit = { _, _ -> },
    viewModel: AuthViewModel = viewModel()
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }

    val authState by viewModel.authState

    LaunchedEffect(authState) {
        val currentStatus = authState
        if (currentStatus is AuthState.Success) {
            onRegisterSuccess(currentStatus.role, currentStatus.userName)
            viewModel.resetState()
        }
    }

    val isFormValid = fullName.isNotBlank() && 
                     email.isNotBlank() && 
                     password.length >= 6 && 
                     phone.isNotBlank() && 
                     dob.isNotBlank() && 
                     location.isNotBlank()

    Box(modifier = Modifier.fillMaxSize()) {
        // Background Image - Adding rose.jpg here
        // Please make sure rose.jpg is in your res/drawable folder
        Image(
            painter = painterResource(id = R.drawable.rose), 
            contentDescription = "Registration Background",
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
                    "Join Us",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
                
                Text(
                    "Register to Ivonne Orchard",
                    fontSize = 14.sp,
                    color = GoldLuxury,
                    modifier = Modifier.padding(top = 4.dp, bottom = 32.dp)
                )

                GlowingTextField(fullName, { fullName = it }, "Full Name", Icons.Default.Person, GoldLuxury)
                Spacer(modifier = Modifier.height(16.dp))
                GlowingTextField(email, { email = it }, "Email Address", Icons.Default.Email, GoldLuxury, keyboardType = KeyboardType.Email)
                Spacer(modifier = Modifier.height(16.dp))
                GlowingTextField(password, { password = it }, "Password", Icons.Default.Lock, GoldLuxury, isPassword = true)
                Spacer(modifier = Modifier.height(16.dp))
                GlowingTextField(phone, { phone = it }, "Phone Number", Icons.Default.Phone, GoldLuxury, keyboardType = KeyboardType.Phone)
                Spacer(modifier = Modifier.height(16.dp))
                GlowingTextField(dob, { dob = it }, "Date of Birth", Icons.Default.Cake, GoldLuxury)
                Spacer(modifier = Modifier.height(16.dp))
                GlowingTextField(location, { location = it }, "Location", Icons.Default.LocationOn, GoldLuxury)

                Spacer(modifier = Modifier.height(24.dp))

                if (authState is AuthState.Error) {
                    Text((authState as AuthState.Error).message, color = Color.Red, fontSize = 12.sp, modifier = Modifier.padding(bottom = 16.dp))
                }

                Button(
                    onClick = { viewModel.registerUser(fullName, email, password, phone, dob, location) },
                    enabled = isFormValid && authState !is AuthState.Loading,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaroonPrimary),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    if (authState is AuthState.Loading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text("SIGN UP", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                
                TextButton(onClick = onBackToLogin) {
                    Text("Already have an account? Sign In", color = GoldLuxury)
                }
            }
        }
    }
}
