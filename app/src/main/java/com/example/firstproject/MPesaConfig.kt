package com.example.firstproject

object MPesaConfig {
    // These are Safaricom's official Sandbox Test Credentials
    // You can use these to test the flow without your own portal account
    const val CONSUMER_KEY = "70bq0Xkyy4F0JOFAvR47SIGyA1ckHZNXzpHAZvskCoaspJBG" // Replace with your key from Daraja
    const val CONSUMER_SECRET = "ydbKHpjjAYCheNhaYKw7XqznPEIj0pJYAVLPA65ZdR5wzbmGvgXL301uuI0AWDTu" // Replace with your secret from Daraja
    
    const val BUSINESS_SHORT_CODE = "174379" // Sandbox Paybill
    const val PASSKEY = "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919"
    
    // Replace with your actual callback URL for production
    const val CALLBACK_URL = "https://sandbox.safaricom.co.ke/mpesa/stkpush/v1/query"
}
