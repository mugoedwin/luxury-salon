package com.example.firstproject

import android.net.Uri
import android.util.Log
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback

object CloudinaryAPI {
    fun uploadImage(
        imageUri: Uri,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit,
        onProgress: (Long, Long) -> Unit = { _, _ -> }
    ) {
        // Switching back to explicitly UNSIGNED upload as seen in your dashboard
        MediaManager.get().upload(imageUri)
            .unsigned("kingvon")
            .callback(object : UploadCallback {
                override fun onStart(requestId: String) {
                    Log.d("CloudinaryAPI", "Upload started: $requestId")
                }

                override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {
                    onProgress(bytes, totalBytes)
                }

                override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                    // Step 2: Get the correct URL after upload
                    val imageUrl = resultData["secure_url"].toString()
                    onSuccess(imageUrl)
                }

                override fun onError(requestId: String, error: ErrorInfo) {
                    Log.e("CloudinaryAPI", "Upload error: ${error.description}")
                    onError(error.description ?: "Unknown error")
                }

                override fun onReschedule(requestId: String, error: ErrorInfo) {
                    Log.d("CloudinaryAPI", "Upload rescheduled: ${error.description}")
                }
            })
            .dispatch()
    }
}
