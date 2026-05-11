package com.example.firstproject.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.firstproject.DarajaUtils
import com.example.firstproject.MPesaAPI
import com.example.firstproject.MPesaConfig
import com.example.firstproject.model.AccessTokenResponse
import com.example.firstproject.model.STKPushRequest
import com.example.firstproject.model.STKPushResponse
import com.example.firstproject.ui.screens.BookingManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

sealed class PaymentState {
    object Idle : PaymentState()
    object Loading : PaymentState()
    data class Success(val message: String) : PaymentState()
    data class Error(val message: String) : PaymentState()
}

class PaymentViewModel : ViewModel() {

    private val _paymentState = mutableStateOf<PaymentState>(PaymentState.Idle)
    val paymentState: State<PaymentState> = _paymentState

    private val api: MPesaAPI by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        Retrofit.Builder()
            .baseUrl("https://sandbox.safaricom.co.ke/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(MPesaAPI::class.java)
    }

    fun onPayClicked(inputPhone: String, amount: Int) {
        val formattedPhone = DarajaUtils.formatPhoneNumber(inputPhone)
        
        if (formattedPhone.length != 12) {
            _paymentState.value = PaymentState.Error("Please enter a valid M-Pesa number")
            return
        }
        
        val finalAmount = if (amount <= 0) 1 else amount
        
        initiatePayment(formattedPhone, finalAmount)
    }

    private fun initiatePayment(phoneNumber: String, amount: Int) {
        _paymentState.value = PaymentState.Loading

        val key = MPesaConfig.CONSUMER_KEY.trim()
        val secret = MPesaConfig.CONSUMER_SECRET.trim()
        
        val authHeader = DarajaUtils.getAuthHeader(key, secret)

        api.getAccessToken(authHeader).enqueue(object : Callback<AccessTokenResponse> {
            override fun onResponse(call: Call<AccessTokenResponse>, response: Response<AccessTokenResponse>) {
                if (response.isSuccessful) {
                    val accessToken = response.body()?.accessToken
                    if (accessToken != null) {
                        // After successful initiation, save booking to Firestore
                        saveBookingsToFirestore()
                        performSTKPush(accessToken, phoneNumber, amount)
                    } else {
                        _paymentState.value = PaymentState.Error("Token was empty")
                    }
                } else {
                    _paymentState.value = PaymentState.Error("Auth Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<AccessTokenResponse>, t: Throwable) {
                _paymentState.value = PaymentState.Error("Network failure: ${t.message}")
            }
        })
    }

    private fun saveBookingsToFirestore() {
        val db = FirebaseFirestore.getInstance()
        val user = FirebaseAuth.getInstance().currentUser
        
        BookingManager.cart.forEach { item ->
            val bookingData = hashMapOf(
                "serviceName" to item.title,
                "imageUrl" to item.imageUrl,
                "price" to item.price,
                "duration" to item.duration,
                "appointmentDate" to "Today",
                "appointmentTime" to "Soon",
                "customerName" to (user?.displayName ?: "Guest"),
                "status" to "Pending",
                "timestamp" to FieldValue.serverTimestamp()
            )
            
            db.collection("bookings")
                .add(bookingData)
                .addOnSuccessListener { Log.d("Firestore", "Booking saved!") }
                .addOnFailureListener { e -> Log.e("Firestore", "Error saving", e) }
        }
        BookingManager.clearCart()
    }

    private fun performSTKPush(accessToken: String, phoneNumber: String, amount: Int) {
        val timestamp = DarajaUtils.getTimestamp()
        val password = DarajaUtils.getPassword(MPesaConfig.BUSINESS_SHORT_CODE, MPesaConfig.PASSKEY, timestamp)

        val request = STKPushRequest(
            businessShortCode = MPesaConfig.BUSINESS_SHORT_CODE,
            password = password,
            timestamp = timestamp,
            transactionType = "CustomerPayBillOnline",
            amount = amount,
            partyA = phoneNumber,
            partyB = MPesaConfig.BUSINESS_SHORT_CODE,
            phoneNumber = phoneNumber,
            callBackURL = MPesaConfig.CALLBACK_URL,
            accountReference = "Ivonne Orchard",
            transactionDesc = "Payment for Service"
        )

        api.sendSTKPush("Bearer $accessToken", request).enqueue(object : Callback<STKPushResponse> {
            override fun onResponse(call: Call<STKPushResponse>, response: Response<STKPushResponse>) {
                if (response.isSuccessful) {
                    _paymentState.value = PaymentState.Success(response.body()?.customerMessage ?: "Check your phone!")
                } else {
                    _paymentState.value = PaymentState.Error("STK Push Failed")
                }
            }

            override fun onFailure(call: Call<STKPushResponse>, t: Throwable) {
                _paymentState.value = PaymentState.Error("STK Network error: ${t.message}")
            }
        })
    }

    fun resetState() {
        _paymentState.value = PaymentState.Idle
    }
}
