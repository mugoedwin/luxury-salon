package com.example.firstproject.ui.theme

import androidx.compose.animation.*
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
import com.example.firstproject.viewmodel.ProductState
import com.example.firstproject.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProductScreen(
    id: String,
    name: String,
    price: String,
    category: String,
    onProductUpdated: () -> Unit = {},
    onBack: () -> Unit = {},
    viewModel: ProductViewModel = viewModel()
) {
    // PREFILL FIELDS from navigation arguments
    var productName by remember { mutableStateOf(name) }
    var productPrice by remember { mutableStateOf(price) }
    var productCategory by remember { mutableStateOf(category) }
    var productDescription by remember { mutableStateOf("") }

    val productState by viewModel.productState

    LaunchedEffect(productState) {
        if (productState is ProductState.Success) {
            onProductUpdated()
            viewModel.resetState()
        }
    }

    val purple = Color(0xFF8E2DE2)
    val deepBlue = Color(0xFF4A00E0)
    val golden = Color(0xFFFFD700)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(deepBlue, purple, Color.Black),
                    start = Offset(0f, 0f),
                    end = Offset.Infinite
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .shadow(20.dp, RoundedCornerShape(32.dp)),
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.08f)),
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f))
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                    Text("Edit Product", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }

                Spacer(modifier = Modifier.height(24.dp))

                GlowingTextField(value = productName, onValueChange = { productName = it }, label = "Name", icon = Icons.Default.ShoppingBag, accentColor = golden)
                Spacer(modifier = Modifier.height(16.dp))
                GlowingTextField(value = productDescription, onValueChange = { productDescription = it }, label = "Description", icon = Icons.Default.Description, accentColor = Color.Cyan)
                Spacer(modifier = Modifier.height(16.dp))
                GlowingTextField(value = productPrice, onValueChange = { productPrice = it }, label = "Price (Ksh)", icon = Icons.Default.AttachMoney, accentColor = golden, keyboardType = KeyboardType.Decimal)
                Spacer(modifier = Modifier.height(16.dp))
                GlowingTextField(value = productCategory, onValueChange = { productCategory = it }, label = "Category", icon = Icons.Default.Category, accentColor = Color.Cyan)

                Spacer(modifier = Modifier.height(32.dp))

                // SAVE BUTTON
                Button(
                    onClick = {
                        val updates = mapOf(
                            "name" to productName,
                            "price" to (productPrice.toDoubleOrNull() ?: 0.0),
                            "category" to productCategory,
                            "description" to productDescription
                        )
                        viewModel.updateProduct(id, updates)
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = golden),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    if (productState is ProductState.Loading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text("Save Changes", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
