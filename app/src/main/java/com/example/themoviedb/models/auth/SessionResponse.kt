package com.example.themoviedb.models.auth

import com.google.gson.annotations.SerializedName

data class SessionResponse(
    val success: Boolean,
    @SerializedName("session_id")
    val sessionId: String
)