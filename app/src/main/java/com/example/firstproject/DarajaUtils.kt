package com.example.firstproject

import android.util.Base64
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DarajaUtils {
    // Generates the timestamp in the exact format Safaricom expects: yyyyMMddHHmmss
    fun getTimestamp(): String {
        return SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(Date())
    }

    // Creates the Base64 password: ShortCode + Passkey + Timestamp
    fun getPassword(shortCode: String, passkey: String, timestamp: String): String {
        val str = shortCode + passkey + timestamp
        return Base64.encodeToString(str.toByteArray(), Base64.NO_WRAP)
    }

    // Encodes your Consumer Key and Secret for the Access Token request
    fun getAuthHeader(consumerKey: String, consumerSecret: String): String {
        val auth = "$consumerKey:$consumerSecret"
        return "Basic " + Base64.encodeToString(auth.toByteArray(), Base64.NO_WRAP)
    }

    // Formats the phone number to 254XXXXXXXXX format
    fun formatPhoneNumber(phone: String): String {
        // 1. Remove all non-numeric characters (spaces, +, dashes)
        val cleanNumber = phone.replace(Regex("[^0-9]"), "")

        return when {
            // Starts with 07 or 01 (local format)
            cleanNumber.startsWith("0") -> "254" + cleanNumber.substring(1)
            
            // Starts with 7 or 1 (shorthand)
            (cleanNumber.startsWith("7") || cleanNumber.startsWith("1")) && cleanNumber.length == 9 -> "254" + cleanNumber
            
            // Already starts with 254
            cleanNumber.startsWith("254") && cleanNumber.length == 12 -> cleanNumber
            
            // Return original if it doesn't match common patterns (let the API handle the error)
            else -> cleanNumber
        }
    }
}
