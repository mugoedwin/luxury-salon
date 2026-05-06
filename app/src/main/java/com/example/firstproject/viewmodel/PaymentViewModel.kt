package com.example.firstproject.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.firstproject.MPesaAPI
import com.example.firstproject.MPesaConfig
import com.example.firstproject.model.AccessTokenResponse
import com.example.firstproject.model.STKPushRequest
import com.example.firstproject.model.STKPushResponse
import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

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

    fun initiatePayment(phoneNumber: String, amount: Int) {
        _paymentState.value = PaymentState.Loading

        val key = MPesaConfig.CONSUMER_KEY.trim()
        val secret = MPesaConfig.CONSUMER_SECRET.trim()
        
        // Use OkHttp's standard Credentials utility for Basic Auth
        val authHeader = Credentials.basic(key, secret)

        api.getAccessToken(authHeader).enqueue(object : Callback<AccessTokenResponse> {
            override fun onResponse(call: Call<AccessTokenResponse>, response: Response<AccessTokenResponse>) {
                if (response.isSuccessful) {
                    val accessToken = response.body()?.accessToken
                    if (accessToken != null) {
                        performSTKPush(accessToken, phoneNumber, amount)
                    } else {
                        _paymentState.value = PaymentState.Error("Token was empty")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("MPesaAuth", "Full Error Body: $errorBody")
                    
                    val errorMessage = when (response.code()) {
                        400 -> "Invalid Credentials. Please check Key/Secret in MPesaConfig."
                        401 -> "Unauthorized. Check if your App is Approved on Daraja."
                        else -> "Auth Error: ${response.code()}"
                    }
                    _paymentState.value = PaymentState.Error(errorMessage)
                }
            }

            override fun onFailure(call: Call<AccessTokenResponse>, t: Throwable) {
                _paymentState.value = PaymentState.Error("Network failure: ${t.message}")
            }
        })
    }

    private fun performSTKPush(accessToken: String, phoneNumber: String, amount: Int) {
        val timestamp = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(Date())
        // Standard M-Pesa Password generation
        val passwordStr = MPesaConfig.BUSINESS_SHORT_CODE + MPesaConfig.PASSKEY + timestamp
        val password = android.util.Base64.encodeToString(passwordStr.toByteArray(), android.util.Base64.NO_WRAP)

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
            accountReference = "Lux Salon",
            transactionDesc = "Payment for services"
        )

        api.sendSTKPush("Bearer $accessToken", request).enqueue(object : Callback<STKPushResponse> {
            override fun onResponse(call: Call<STKPushResponse>, response: Response<STKPushResponse>) {
                if (response.isSuccessful) {
                    _paymentState.value = PaymentState.Success(response.body()?.customerMessage ?: "Check your phone!")
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("MPesaSTK", "Error: $errorBody")
                    _paymentState.value = PaymentState.Error("STK Push Failed: ${response.code()}")
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
