package com.example.themoviedb.models.auth

import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @SerializedName("expires_at")
    val expiresAt: String,
    @SerializedName("request_token")
    val accessToken: String,
    val success: Boolean
)