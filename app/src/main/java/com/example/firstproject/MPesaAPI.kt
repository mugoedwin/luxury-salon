package com.example.firstproject

import com.example.firstproject.model.AccessTokenResponse
import com.example.firstproject.model.STKPushRequest
import com.example.firstproject.model.STKPushResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface MPesaAPI {
    @GET("oauth/v1/generate?grant_type=client_credentials")
    fun getAccessToken(@Header("Authorization") authHeader: String): Call<AccessTokenResponse>

    // REMOVED leading slash to prevent double slash with baseUrl
    @POST("mpesa/stkpush/v1/processrequest")
    fun sendSTKPush(
        @Header("Authorization") authHeader: String,
        @Body request: STKPushRequest
    ): Call<STKPushResponse>
}
