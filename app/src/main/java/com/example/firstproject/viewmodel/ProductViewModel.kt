package com.example.firstproject.viewmodel

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.firstproject.CloudinaryAPI
import com.example.firstproject.model.Product
import com.google.firebase.database.FirebaseDatabase

sealed class ProductState {
    object Idle : ProductState()
    object Loading : ProductState()
    object Success : ProductState()
    data class Error(val message: String) : ProductState()
}

class ProductViewModel : ViewModel() {
    private val mDatabase = FirebaseDatabase.getInstance().getReference("Products")

    private val _productState = mutableStateOf<ProductState>(ProductState.Idle)
    val productState: State<ProductState> = _productState

    fun addProduct(name: String, description: String, price: String, category: String, imageUri: Uri?) {
        if (name.isBlank() || description.isBlank() || price.isBlank() || category.isBlank()) {
            _productState.value = ProductState.Error("Please fill in all fields")
            return
        }

        val priceValue = price.toDoubleOrNull()
        if (priceValue == null) {
            _productState.value = ProductState.Error("Invalid price format")
            return
        }

        _productState.value = ProductState.Loading

        if (imageUri != null) {
            // Upload to Cloudinary first
            CloudinaryAPI.uploadImage(
                imageUri = imageUri,
                onSuccess = { imageUrl ->
                    saveProductToDatabase(name, description, priceValue, category, imageUrl)
                },
                onError = { errorMessage ->
                    _productState.value = ProductState.Error("Image upload failed: $errorMessage")
                }
            )
        } else {
            // Save without image
            saveProductToDatabase(name, description, priceValue, category, "")
        }
    }

    private fun saveProductToDatabase(name: String, description: String, price: Double, category: String, imageUrl: String) {
        val productId = mDatabase.push().key ?: ""
        
        val product = Product(
            id = productId,
            name = name,
            description = description,
            price = price,
            imageUrl = imageUrl,
            category = category
        )

        mDatabase.child(productId).setValue(product)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _productState.value = ProductState.Success
                } else {
                    _productState.value = ProductState.Error(task.exception?.message ?: "Failed to add product")
                }
            }
    }

    fun deleteProduct(productId: String) {
        if (productId.isEmpty()) return
        
        _productState.value = ProductState.Loading
        mDatabase.child(productId).removeValue()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _productState.value = ProductState.Success
                } else {
                    _productState.value = ProductState.Error(task.exception?.message ?: "Failed to delete product")
                }
            }
    }

    // Updated to use updateChildren to avoid overwriting missing fields like imageUrl
    fun updateProduct(id: String, updates: Map<String, Any>) {
        if (id.isEmpty()) return
        
        _productState.value = ProductState.Loading
        mDatabase.child(id).updateChildren(updates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _productState.value = ProductState.Success
                } else {
                    _productState.value = ProductState.Error(task.exception?.message ?: "Failed to update product")
                }
            }
    }

    fun resetState() {
        _productState.value = ProductState.Idle
    }
}
