package com.example.firstproject

import android.content.Context
import android.net.Uri
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback

object CloudinaryHelper {
    private var isInitialized = false

    // Your credentials
    private const val CLOUD_NAME = "dsv16i7ys"
    const val UPLOAD_PRESET = "mugoed"

    fun init(context: Context) {
        if (!isInitialized) {
            val config = hashMapOf(
                "cloud_name" to CLOUD_NAME
            )
            MediaManager.init(context, config)
            isInitialized = true
        }
    }

    fun uploadToCloudinary(context: Context, imageUri: Uri, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        MediaManager.get().upload(imageUri)
            .option("unsigned", true)
            .option("upload_preset", UPLOAD_PRESET)
            .callback(object : UploadCallback {
                override fun onStart(requestId: String) {
                    // Upload started
                }

                override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {
                    // Track progress if needed
                }

                override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                    val imageUrl = resultData["secure_url"] as String
                    onSuccess(imageUrl)
                }

                override fun onError(requestId: String, error: ErrorInfo) {
                    onError(error.description)
                }

                override fun onReschedule(requestId: String, error: ErrorInfo) {
                    onError("Upload rescheduled: ${error.description}")
                }
            }).dispatch(context)
    }
}
