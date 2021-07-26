package com.example.themoviedb.models.auth

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("expires_at")
    val expiresAt: String,
    @SerializedName("request_token")
    val requestToken: String,
    val success: Boolean
)