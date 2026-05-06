package com.example.firstproject.ui.theme

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.firstproject.viewmodel.ProductState
import com.example.firstproject.viewmodel.ProductViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(
    onProductAdded: () -> Unit = {},
    onBack: () -> Unit = {},
    viewModel: ProductViewModel = viewModel()
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var localError by remember { mutableStateOf<String?>(null) }

    val productState by viewModel.productState
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    LaunchedEffect(productState) {
        if (productState is ProductState.Success) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = "Product added successfully!",
                    duration = SnackbarDuration.Short
                )
                onProductAdded()
            }
            viewModel.resetState()
            // Clear fields
            name = ""
            description = ""
            price = ""
            category = ""
            imageUri = null
            localError = null
        }
    }

    val purple = Color(0xFF8E2DE2)
    val deepBlue = Color(0xFF4A00E0)
    val neonBlue = Color(0xFF00D2FF)
    val golden = Color(0xFFFFD700)

    val infiniteTransition = rememberInfiniteTransition(label = "add_product_glow")
    val glowPulse by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "pulse"
    )

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color.Transparent
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(deepBlue, purple, Color.Black),
                        start = Offset(0f, 0f),
                        end = Offset.Infinite
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            // Loading Overlay
            AnimatedVisibility(
                visible = productState is ProductState.Loading,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier.zIndex(10f)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.6f))
                        .clickable(enabled = false) {}, // Intercept clicks
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = golden, strokeWidth = 4.dp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Uploading to Cloud...",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .size(350.dp)
                    .offset(x = 150.dp, y = (-300).dp)
                    .background(golden.copy(alpha = 0.1f * glowPulse), CircleShape)
                    .blur(100.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .shadow(
                        elevation = 40.dp,
                        shape = RoundedCornerShape(32.dp),
                        ambientColor = golden,
                        spotColor = neonBlue
                    ),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.08f)
                ),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f))
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 24.dp, vertical = 32.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                        }
                        Text(
                            text = "Add New Product",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                    
                    Text(
                        text = "Stock up your salon inventory",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.6f),
                        modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
                    )

                    // Image Selection Area
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White.copy(alpha = 0.1f))
                            .clickable { launcher.launch("image/*") }
                            .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (imageUri != null) {
                            Image(
                                painter = rememberAsyncImagePainter(imageUri),
                                contentDescription = "Selected Image",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.AddPhotoAlternate, contentDescription = null, tint = golden, modifier = Modifier.size(32.dp))
                                Text("Add Photo", color = Color.White.copy(alpha = 0.5f), fontSize = 10.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    GlowingTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = "Product Name",
                        icon = Icons.Default.ShoppingBag,
                        accentColor = golden
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    GlowingTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = "Description",
                        icon = Icons.Default.Description,
                        accentColor = neonBlue
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    GlowingTextField(
                        value = price,
                        onValueChange = { price = it },
                        label = "Price ($)",
                        icon = Icons.Default.AttachMoney,
                        accentColor = golden,
                        keyboardType = KeyboardType.Decimal
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    GlowingTextField(
                        value = category,
                        onValueChange = { category = it },
                        label = "Category (e.g., Hair, Skin)",
                        icon = Icons.Default.Category,
                        accentColor = neonBlue
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    if (localError != null || productState is ProductState.Error) {
                        val message = localError ?: (productState as ProductState.Error).message
                        Text(
                            text = message,
                            color = Color.Red,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }

                    Button(
                        onClick = {
                            localError = null
                            if (imageUri == null) {
                                localError = "Please add an image for the product"
                                return@Button
                            }
                            if (name.isBlank() || description.isBlank() || price.isBlank() || category.isBlank()) {
                                localError = "Please fill in all required fields"
                                return@Button
                            }
                            if (price.toDoubleOrNull() == null) {
                                localError = "Please enter a valid price"
                                return@Button
                            }
                            viewModel.addProduct(name, description, price, category, imageUri)
                        },
                        enabled = productState !is ProductState.Loading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        contentPadding = PaddingValues(0.dp),
                        shape = RoundedCornerShape(18.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.horizontalGradient(listOf(golden, purple, neonBlue)),
                                    shape = RoundedCornerShape(18.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (productState is ProductState.Loading) {
                                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                            } else {
                                Text(
                                    text = "ADD TO SHOP",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White,
                                    letterSpacing = 1.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddProductScreenPreview() {
    AddProductScreen()
}
