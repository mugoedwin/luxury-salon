package com.example.firstproject

import android.content.Context
import com.cloudinary.android.MediaManager

object CloudinaryHelper {
    private var isInitialized = false

    fun init(context: Context) {
        if (!isInitialized) {
            // Using Signed Upload credentials as per your Core Accounts checklist
            val config = hashMapOf(
                "cloud_name" to "df9dkqh7w",
                "api_key" to "713375836261278",
                "api_secret" to "lV8GvE0G26-Zz_tUfA8R61n_zN0"
            )
            MediaManager.init(context, config)
            isInitialized = true
        }
    }
}

data class CloudinaryResponse(
    val url: String? = null,
    val secureUrl: String? = null,
    val publicId: String? = null
)
