package com.example.firstproject.ui.theme

import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.firstproject.viewmodel.AuthState
import com.example.firstproject.viewmodel.AuthViewModel
import kotlinx.coroutines.delay

@Composable
fun AboutContent(
    onRegistrationSuccess: () -> Unit = {},
    viewModel: AuthViewModel = viewModel()
) {
    val context = LocalContext.current

    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }

    val authState by viewModel.authState

    // Load current user data if logged in
    LaunchedEffect(Unit) {
        viewModel.getCurrentUser { user ->
            user?.let {
                fullName = it.name
                email = it.email
                phone = it.phone
                dob = it.dob
                location = it.location
            }
        }
    }

    // Handle update success
    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            showSuccessDialog = true
            viewModel.resetState()
        } else if (authState is AuthState.Error) {
            Toast.makeText(context, (authState as AuthState.Error).message, Toast.LENGTH_LONG).show()
        }
    }

    val imageBitmap = remember {
        try {
            val inputStream = context.assets.open("images/image5.jpg")
            BitmapFactory.decodeStream(inputStream).asImageBitmap()
        } catch (_: Exception) {
            null
        }
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("Profile Updated", fontWeight = FontWeight.Bold) },
            text = { Text("Your LUX Profile has been updated successfully. You can now proceed with your experience.") },
            confirmButton = {
                TextButton(onClick = { 
                    showSuccessDialog = false
                    onRegistrationSuccess() 
                }) {
                    Text("Continue")
                }
            },
            containerColor = BoneWhite,
            titleContentColor = DeepSlate,
            textContentColor = DeepSlate.copy(alpha = 0.7f)
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "The LUX Story",
                fontSize = 32.sp,
                fontWeight = FontWeight.Light,
                color = DeepSlate,
                letterSpacing = 2.sp
            )
            Text(
                text = "Crafting Beauty Since 2010",
                fontSize = 14.sp,
                color = BrushedGold,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(32.dp))
        }

        // Hero Image Section
        item {
            imageBitmap?.let {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .shadow(12.dp, RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        bitmap = it,
                        contentDescription = "Salon Experience",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }

        item {
            InfoSection(
                title = "OUR PHILOSOPHY",
                description = "We believe that true beauty is a reflection of self-care and inner balance. LUX isn't just a salon; it's a sanctuary designed to provide a holistic experience where every guest leaves feeling renewed, confident, and absolutely radiant.",
                icon = Icons.Default.Favorite,
                color = Plum
            )
            Spacer(modifier = Modifier.height(24.dp))
            
            InfoSection(
                title = "ARTISAN CARE",
                description = "Our team of master stylists and therapists are dedicated to the fine art of beauty. Using only premium, organic products, we tailor every treatment to your unique skin and hair needs, ensuring a bespoke result every single time.",
                icon = Icons.Default.AutoAwesome,
                color = BrushedGold
            )
            Spacer(modifier = Modifier.height(32.dp))
        }

        // Profile Update Section
        item {
            Text(
                text = "MY LUX PROFILE",
                fontSize = 11.sp,
                color = MutedEarth,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.5.sp
            )
            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                border = BorderStroke(0.5.dp, SoftStone)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = "Keep your details updated for personalized beauty updates and seamless payments.",
                        fontSize = 13.sp,
                        color = DeepSlate.copy(alpha = 0.6f),
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    AboutTextField(label = "Full Name", value = fullName, onValueChange = { fullName = it; showError = false }, icon = Icons.Default.Person, accentColor = BrushedGold)
                    Spacer(modifier = Modifier.height(16.dp))
                    AboutTextField(label = "Email Address", value = email, onValueChange = { email = it; showError = false }, icon = Icons.Default.Email, accentColor = Plum)
                    Spacer(modifier = Modifier.height(16.dp))
                    AboutTextField(label = "Phone Number", value = phone, onValueChange = { phone = it; showError = false }, icon = Icons.Default.Phone, accentColor = BrushedGold)
                    Spacer(modifier = Modifier.height(16.dp))
                    AboutTextField(label = "Date of Birth", value = dob, onValueChange = { dob = it; showError = false }, icon = Icons.Default.Cake, accentColor = Plum)
                    Spacer(modifier = Modifier.height(16.dp))
                    AboutTextField(label = "Location", value = location, onValueChange = { location = it; showError = false }, icon = Icons.Default.LocationOn, accentColor = BrushedGold)

                    AnimatedVisibility(visible = showError) {
                        Text(
                            text = "Please complete all fields to update your profile.",
                            color = Color.Red.copy(alpha = 0.8f),
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    val isFormValid = fullName.isNotBlank() && email.isNotBlank() && dob.isNotBlank() && location.isNotBlank() && phone.isNotBlank()

                    Button(
                        onClick = { 
                            if (isFormValid) {
                                viewModel.updateUser(fullName, email, phone, dob, location)
                            } else {
                                showError = true
                            }
                        },
                        enabled = authState !is AuthState.Loading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isFormValid) BrushedGold else SoftStone
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (authState is AuthState.Loading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        } else {
                            Text(
                                text = "UPDATE PROFILE",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isFormValid) Color.White else DeepSlate.copy(alpha = 0.3f),
                                letterSpacing = 1.sp
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
        }

        item {
            Text(
                text = "THE MASTER ARTISANS",
                fontSize = 11.sp,
                color = MutedEarth,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.5.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            ArchitectCard(name = "Mugo E.", role = "Founder & Creative Director", color = Plum)
            Spacer(modifier = Modifier.height(12.dp))
            ArchitectCard(name = "LUX Team", role = "Master Stylist Collective", color = BrushedGold)
            
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun InfoSection(title: String, description: String, icon: ImageVector, color: Color) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = DeepSlate,
                letterSpacing = 1.sp
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = description,
            fontSize = 15.sp,
            color = DeepSlate.copy(alpha = 0.7f),
            lineHeight = 22.sp
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    icon: ImageVector,
    accentColor: Color
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label, color = MutedEarth, fontSize = 12.sp) },
        leadingIcon = { Icon(icon, contentDescription = null, tint = accentColor) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = accentColor,
            unfocusedBorderColor = SoftStone,
            cursorColor = accentColor,
            focusedTextColor = DeepSlate,
            unfocusedTextColor = DeepSlate
        ),
        shape = RoundedCornerShape(12.dp),
        singleLine = true
    )
}

@Composable
fun ArchitectCard(name: String, role: String, color: Color) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        border = BorderStroke(0.5.dp, SoftStone)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(color.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(name.take(1), color = color, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(name, fontWeight = FontWeight.Bold, color = DeepSlate, fontSize = 16.sp)
                Text(role, color = MutedEarth, fontSize = 12.sp)
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF9F8F6)
@Composable
fun AboutPreview() {
    AboutContent()
}
